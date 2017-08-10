package com.example.tbittestlib.bike.util;


import com.example.tbittestlib.bike.model.BikeState;
import com.example.tbittestlib.bike.model.ControllerState;
import com.example.tbittestlib.bluetooth.util.ByteUtil;

import java.util.Arrays;

/**
 * Created by Salmon on 2017/3/16 0016.
 */

public class StateUpdateHelper {

    public static void updateControllerState(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length != 13)
            return;
        ControllerState controllerState = bikeStates.getControllerState();

        controllerState.setRawData(data);

        byte[] originData = ByteUtil.byteArrayToUnBoxed(data);

        controllerState.setTotalMillage(byteArrayToInt(Arrays.copyOfRange(originData, 0, 2)));

        controllerState.setSingleMillage(byteArrayToInt(Arrays.copyOfRange(originData, 2, 4)));

        controllerState.setSpeed(byteArrayToInt(Arrays.copyOfRange(originData, 4, 6)));

        Byte originError = data[6];
        int[] error = controllerState.getErrCode();
        error[0] = bitResolver(originError, 0x01);
        error[1] = bitResolver(originError, 0x02);
        error[2] = bitResolver(originError, 0x04);
        error[3] = bitResolver(originError, 0x08);
        error[4] = bitResolver(originError, 0x10);
        error[5] = bitResolver(originError, 0x20);
        error[6] = bitResolver(originError, 0x40);
        error[7] = bitResolver(originError, 0x80);

        controllerState.setVoltage(byteArrayToInt(Arrays.copyOfRange(originData, 7, 9)));

        controllerState.setElectricCurrent(byteArrayToInt(Arrays.copyOfRange(originData, 9, 11)));

        controllerState.setBattery(byteArrayToInt(Arrays.copyOfRange(originData, 11, 13)));
    }

    public static void updateBaseStation(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length != 8)
            return;
        try {
            byte[] temp = ByteUtil.byteArrayToUnBoxed(data);
            byte[] mcc = ByteUtil.subBytes(temp, 0, 2);
            byte[] mnc = ByteUtil.subBytes(temp, 2, 1);
            byte[] lac = ByteUtil.subBytes(temp, 3, 2);
            byte[] cell = ByteUtil.subBytes(temp, 5, 3);

            int[] result = new int[4];
            result[0] = byteArrayToInt(mcc);
            result[1] = byteArrayToInt(mnc);
            result[2] = byteArrayToInt(lac);
            result[3] = byteArrayToInt(cell);

            bikeStates.setBaseStation(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int bitResolver(Byte state, int flag) {
        boolean isFlagged = (state & flag) == flag;
        return isFlagged ? 1 : 0;
    }
    
    public static void byteToBitArray(Byte b, int[] array) {
        array[0] = bitResolver(b, 0x01);
        array[1] = bitResolver(b, 0x02);
        array[2] = bitResolver(b, 0x04);
        array[3] = bitResolver(b, 0x08);
        array[4] = bitResolver(b, 0x10);
        array[5] = bitResolver(b, 0x20);
        array[6] = bitResolver(b, 0x40);
        array[7] = bitResolver(b, 0x80);
    }

    public static int byteArrayToInt(byte[] data) {
//        return ByteUtil.bytesToInt(data);
        String s = ByteUtil.bytesToHexString(data);
        s = s.replace(" ", "");
        return Integer.parseInt(s, 16);
    }

    public static void updateAll(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length == 0)
            return;

        bikeStates.setRawData(data);

        if (data.length >= 10) {
            Byte[] locationData = Arrays.copyOfRange(data, 0, 10);
            updateLocation(bikeStates, locationData);
        }
        if (data.length >= 13) {
            Byte[] signalData = Arrays.copyOfRange(data, 10, 13);
            updateSignal(bikeStates, signalData);
        }
        if (data.length >= 15) {
            Byte[] batteryData = Arrays.copyOfRange(data, 13, 15);
            updateVoltage(bikeStates, batteryData);
        }
        if (data.length >= 23) {
            Byte[] baseStationData = Arrays.copyOfRange(data, 15, 23);
            updateBaseStation(bikeStates, baseStationData);
        }
        if (data.length >= 36) {
            Byte[] controllerInfoData = Arrays.copyOfRange(data, 23, 36);
            updateControllerState(bikeStates, controllerInfoData);
        }

    }

    public static void updateVoltage(BikeState bikeStates, Byte[] data) {
        int result = 0;
        try {
            result = byteArrayToInt(ByteUtil.byteArrayToUnBoxed(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bikeStates.setBattery(result);
    }

    public static void updateDeviceFault(BikeState bikeStates, Byte[] data) {
        int result = 0;
        try {
            result = byteArrayToInt(ByteUtil.byteArrayToUnBoxed(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bikeStates.setDeviceFaultCode(result);
    }

    public static void updateLocation(BikeState bikeStates, Byte[] data) {
        double[] result = ByteUtil.getPoint(data);
        bikeStates.setLocation(result);
    }

    public static void updateSignal(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length != 3)
            return;
        int[] result = new int[3];
        try {
            for (int i = 0; i < data.length; i++) {
                result[i] = Integer.valueOf(data[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }
        bikeStates.setSignal(result);
    }

    public static void updateVerifyFailed(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length == 0)
            return;
        bikeStates.setVerifyFailedCode(data[data.length - 1]);
    }

    public static void updateSysState(BikeState bikeState, Byte state) {
        int[] result = new int[8];

        result[0] = bitResolver(state, 0x01);
        result[1] = bitResolver(state, 0x02);
        result[2] = bitResolver(state, 0x04);
        result[3] = bitResolver(state, 0x08);
        result[4] = bitResolver(state, 0x10);
        result[5] = bitResolver(state, 0x20);
        result[6] = bitResolver(state, 0x40);
        result[7] = bitResolver(state, 0x80);

        bikeState.setSystemState(result);
    }
}
