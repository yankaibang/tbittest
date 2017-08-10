package com.example.yankaibang.bluetooth.Scanner.decorator;

import android.bluetooth.BluetoothDevice;


import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;
import com.example.yankaibang.bluetooth.debug.BleLog;
import com.example.yankaibang.bluetooth.model.SearchResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Salmon on 2017/3/8 0008.
 */

public class LogCallback extends BaseCallback {
    private Map<String, SearchResult> results = new ConcurrentHashMap<>();
    private StringBuilder sb;

    public LogCallback(ScannerCallback callback) {
        super(callback);
    }

    @Override
    public void onScanStart() {
        results.clear();
        printLogStart();
        callback.onScanStart();
    }

    @Override
    public void onScanStop() {
        printLogScannedLog();
        printLogTimeout();
        callback.onScanStop();
    }

    @Override
    public void onScanCanceled() {
        printLogScannedLog();
        printLogCanceled();
        callback.onScanCanceled();
    }

    @Override
    public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        if (!results.containsKey(bluetoothDevice.getAddress()))
            results.put(bluetoothDevice.getAddress(), new SearchResult(bluetoothDevice, i, bytes));
        callback.onDeviceFounded(bluetoothDevice, i, bytes);
    }

    private void printLogScannedLog() {
        sb = new StringBuilder();
        sb.append("#####################################\n");
        for (Map.Entry<String, SearchResult> entry : results.entrySet()) {
            sb.append("mac: " + entry.getKey())
                    .append("|")
                    .append(" rssi : " + entry.getValue().getRssi())
                    .append("|")
                    .append(" name : " + entry.getValue().getDevice().getName())
                    .append("|")
                    .append("\n");
        }
        sb.append("#####################################");
        BleLog.log("ScanLog", sb.toString());
    }

    private void printLogStart() {
        BleLog.log("ScanLog", "Scan Started");
    }

    private void printLogTimeout() {
        BleLog.log("ScanLog", "Scan Timeout");
    }

    protected void printLogCanceled() {
        BleLog.log("ScanLog", "Scan Canceled");
    }
}
