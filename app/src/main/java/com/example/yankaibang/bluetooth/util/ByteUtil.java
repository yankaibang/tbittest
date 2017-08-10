package com.example.yankaibang.bluetooth.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Salmon on 2016/4/26 0026.
 */
public class ByteUtil {
    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        if (byte_1 == null) {
            return byte_2;
        }
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    //截取数组的一部分
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++)
            bs[i - begin] = src[i];
        return bs;
    }

    public static byte[] byteArrayUnBox(Byte[] ByteArray) {
        byte[] byteArray = new byte[ByteArray.length];
        int i = 0;
        for (Byte B : ByteArray) {
            byteArray[i++] = B.byteValue();
        }
        return byteArray;
    }

    public static byte[] byteToByte(byte value) {
        byte[] abyte = new byte[1];
        abyte[0] = (byte) ((0x00ff & value) >> 0);
        return abyte;
    }

    public static byte[] shortToByte(short value) {
        byte[] abyte = new byte[2];
        abyte[0] = (byte) ((0xff00 & value) >> 8);
        abyte[1] = (byte) ((0x00ff & value) >> 0);
        return abyte;
    }

    public static byte[] intToByte(int value) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) ((0xff000000 & value) >> 24);
        abyte[1] = (byte) ((0x00ff0000 & value) >> 16);
        abyte[2] = (byte) ((0x0000ff00 & value) >> 8);
        abyte[3] = (byte) ((0x000000ff & value) >> 0);
        return abyte;
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = (int) ( ((src[0] & 0xFF)<<24)
                |((src[1] & 0xFF)<<16)
                |((src[2] & 0xFF)<<8)
                |(src[3] & 0xFF));
        return value;
    }

    public static byte[] longToByte(long value) {
        byte[] abyte = new byte[8];
        abyte[0] = (byte) ((0xff00000000000000L & value) >> 56);
        abyte[1] = (byte) ((0x00ff000000000000L & value) >> 48);
        abyte[2] = (byte) ((0x0000ff0000000000L & value) >> 40);
        abyte[3] = (byte) ((0x000000ff00000000L & value) >> 32);
        abyte[4] = (byte) ((0x00000000ff000000L & value) >> 24);
        abyte[5] = (byte) ((0x0000000000ff0000L & value) >> 16);
        abyte[6] = (byte) ((0x000000000000ff00L & value) >> 8);
        abyte[7] = (byte) ((0x00000000000000ffL & value) >> 0);
        return abyte;
    }

    public static Byte[] stringToBytes(String str) {
        String[] ss = str.split(" ");

        List<Byte> list = new ArrayList<>();

        for (String s1 : ss) {
            list.add((byte) Integer.parseInt(s1, 16));
        }

        Byte[] byteArray = list.toArray(new Byte[]{});
        return byteArray;
    }

    public static byte[] byteArrayToUnBoxed(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for(int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }
        return bytes;
    }

    public static Byte[] byteArrayToBoxed(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing

        return bytes;
    }

    public static final byte[] adv_report_parse(short type, byte[] adv_data) {
        int index = 0;
        int length;

        byte[] data;

        byte field_type = 0;
        byte field_length = 0;

        length = adv_data.length;
        while (index < length) {
            try {
                field_length = adv_data[index];
                field_type = adv_data[index + 1];
            } catch (Exception e) {
                return null;
            }

            if (field_type == (byte) type) {
                data = new byte[field_length - 1];

                byte i;
                for (i = 0; i < field_length - 1; i++) {
                    data[i] = adv_data[index + 2 + i];
                }
                return data;
            }
            index += field_length + 1;
            if (index >= adv_data.length) {
                return null;
            }
        }
        return null;
    }

    public static String bytesToHexStringWithoutSpace(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X ", b));
        }
        return builder.toString();
    }

    public static String bytesToHexString(Byte[] bytes) {
        byte[] array = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i];
        }
        return bytesToHexString(array);
    }
    /**
     * 获取经纬度
     */
    public static double[] getPoint(Byte[] data) {
        double[] result = new double[]{0, 0};
        if (data == null)
            return result;
        byte[] converted = new byte[data.length];
        int length = data.length;
        for (int i = 0; i < length; i++) {
            converted[i] = data[i];
        }
        try {
            result[0] = ParseUInt32(Arrays.copyOfRange(converted, 4, 8)) / 1800000.0;
            result[1] = ParseUInt32(Arrays.copyOfRange(converted, 0, 4)) / 1800000.0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long ParseUInt32(byte[] data) {
        int i = 0;
        long l = 0;
        l |= ((data[i++] & 0xff) << 24);
        l |= ((data[i++] & 0xff) << 16);
        l |= ((data[i++] & 0xff) << 8);
        l |= (data[i++] & 0xff);
        return l;
    }
}
