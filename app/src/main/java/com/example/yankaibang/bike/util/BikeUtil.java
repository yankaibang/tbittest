package com.example.yankaibang.bike.util;


import android.text.TextUtils;

import com.example.yankaibang.bike.model.ManufacturerAd;
import com.example.yankaibang.bike.model.ParsedAd;
import com.example.yankaibang.bluetooth.Code;
import com.example.yankaibang.bluetooth.util.ByteUtil;
import com.example.yankaibang.protocol.ResultCode;

import java.io.File;

/**
 * Created by Salmon on 2017/3/9 0009.
 */

public class BikeUtil {
    private static final int MAX_ENCRYPT_COUNT = 95;
    private static char[] szKey = {
            0x35, 0x41, 0x32, 0x42, 0x33, 0x43, 0x36, 0x44, 0x39, 0x45,
            0x38, 0x46, 0x37, 0x34, 0x31, 0x30};

    public static String encryptStr(String in_str) {
        int count = 0;

        StringBuilder builder = new StringBuilder();
        if (in_str == null || in_str.length() == 0) {
            return null;
        }

        count = in_str.length();
        if (count > MAX_ENCRYPT_COUNT) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            builder.append(szKey[in_str.charAt(i) - 0x2A]);
        }
        return builder.toString();
    }

    public static String decryptStr(String in_str) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        int j = 0;
        if (in_str == null || in_str.length() == 0) {
            return null;
        }

        count = in_str.length();
        if (count > MAX_ENCRYPT_COUNT) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            for (j = 0; j < MAX_ENCRYPT_COUNT; j++) {
                if (in_str.charAt(i) == szKey[j]) {
                    break;
                }
            }
            builder.append((char) (0x2A + j));
        }
        return builder.toString();
    }

    /**
     * http://s2is.org/Issues/v1/n2/papers/paper14.pdf
     */
    public static double calcDistByRSSI(int rssi) {
        int iRssi = Math.abs(rssi);
        double power = (iRssi - 59) / (10 * 2.0);
        return Math.pow(10, power);
    }

    public static String resolveMachineIdByAdData(byte[] data) {
        try {
            ParsedAd ad = ParsedAd.parseData(data);
            byte[] manufacturerData = ad.getManufacturer();
            ManufacturerAd manufacturerAd = ManufacturerAd.resolveManufacturerAd(manufacturerData);
            String encryptedId = manufacturerAd.getMachineId();
            return decryptStr(encryptedId);
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }

    public static Byte[] resolveKey(String keyStr, int length) {
        if (TextUtils.isEmpty(keyStr)) return null;
        length = length * 2;
        Byte[] result = new Byte[]{};
        keyStr = keyStr.replace(" ", "");
        if (keyStr.length() != length)
            return result;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j += 2) {
            sb.append(keyStr.substring(j, j + 2));
            if (j == length - 2)
                continue;
            sb.append(" ");
        }
        try {
            result = ByteUtil.stringToBytes(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isOtaFileLegal(File file) {
        if (file == null)
            return false;
        if (!file.exists()) {
            return false;
        }
        String filename = file.getName();
        if (TextUtils.isEmpty(filename))
            return false;
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            String extName = filename.substring(dot + 1);
            if (!TextUtils.equals(extName, "img")) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static int parseResultCode(int bleCode) {
        int parsedResultCode;
        switch (bleCode) {
            case Code.BLE_NOT_CONNECTED:
                parsedResultCode = ResultCode.DISCONNECTED;
                break;
            case Code.BLE_DISABLED:
                parsedResultCode = ResultCode.BLE_NOT_OPENED;
                break;
            case Code.REQUEST_SUCCESS:
                parsedResultCode = ResultCode.SUCCEED;
                break;
            case Code.REQUEST_TIMEOUT:
                parsedResultCode = ResultCode.TIMEOUT;
                break;
            case Code.BLE_NOT_SUPPORTED:
                parsedResultCode = ResultCode.BLE_NOT_SUPPORTED;
                break;
            default:
                parsedResultCode = ResultCode.FAILED;
                break;
        }
        return parsedResultCode;
    }
}
