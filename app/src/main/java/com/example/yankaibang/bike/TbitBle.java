package com.example.yankaibang.bike;


import android.content.Context;

import com.example.yankaibang.bluetooth.BleGlob;
import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;
import com.example.yankaibang.protocol.ProtocolAdapter;
import com.example.yankaibang.protocol.ProtocolInfo;
import com.example.yankaibang.protocol.callback.ResultCallback;

/**
 * Created by Salmon on 2016/12/5 0005.
 */

public class TbitBle {

    private static TbitBleInstance instance;

    private TbitBle() {
    }

    public static int startScan(ScannerCallback callback, long timeout) {
        checkInstanceNotNull();
        return instance.startScan(callback, timeout);
    }

    public static void stopScan() {
        checkInstanceNotNull();
        instance.stopScan();
    }


    public static void destroy() {
        checkInstanceNotNull();
        instance.destroy();
        instance = null;
    }

    public static void initialize(Context context, ProtocolAdapter adapter) {
        if (instance == null) {
            instance = new TbitBleInstance();
            BleGlob.setContext(context);

            ProtocolInfo.packetCrcTable = adapter.getPacketCrcTable();
            ProtocolInfo.adKey = adapter.getAdKey();
            ProtocolInfo.maxEncryptCount = adapter.getMaxAdEncryptedCount();
            ProtocolInfo.configDispatcher = adapter.getConfigDispatcher();
        }
    }

    public static boolean hasInitialized() {
        return instance != null;
    }

    private static void checkInstanceNotNull() {
        if (instance == null)
            throw new RuntimeException("have you 'initialize' on TbitBle ? ");
    }
}
