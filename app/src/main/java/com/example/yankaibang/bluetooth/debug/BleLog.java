package com.example.yankaibang.bluetooth.debug;

/**
 * Created by Salmon on 2017/4/13 0013.
 */

public class BleLog {

    private static LogCallback logCallback;

    public static void setLogCallback(LogCallback logCallback) {
        BleLog.logCallback = logCallback;
    }

    public static void log(String key, String value) {
        if (logCallback != null)
            logCallback.onLogReceived(key + "\n" + value);
    }
}
