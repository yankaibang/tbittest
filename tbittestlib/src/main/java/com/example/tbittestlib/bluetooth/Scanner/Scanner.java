package com.example.tbittestlib.bluetooth.Scanner;

/**
 * Created by Salmon on 2017/3/3 0003.
 */

public interface Scanner {

    public static final class ResultCode{
        public static final int SUCCESS = 0;
        public static final int NOT_SUPPROT = -1;
        public static final int ILLEGAL_STATE = -2;
    }

    void start(ScannerCallback callback, long timeout);

    void cancel();

    boolean isScanning();
}
