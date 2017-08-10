package com.example.tbittestlib.protocol.utils;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public class CrcUtil {
    public static short crcTable(int[] table, byte[] bytes) {
        int mCrc = 0xffff;
        for (byte b : bytes) {
            mCrc = (mCrc >>> 8) ^ table[(mCrc ^ b) & 0xff];
        }
        return (short) ((~mCrc) & 0xffff);
    }
}
