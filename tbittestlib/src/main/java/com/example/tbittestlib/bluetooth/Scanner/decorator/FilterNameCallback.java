package com.example.tbittestlib.bluetooth.Scanner.decorator;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.example.tbittestlib.bluetooth.Scanner.ScannerCallback;


/**
 * Created by Salmon on 2017/3/8 0008.
 */

public class FilterNameCallback extends BaseCallback {
    private String deviceName;
    private boolean isTotalMatch;

    public FilterNameCallback(String deviceName, ScannerCallback callback) {
        this(deviceName, callback, true);
    }

    public FilterNameCallback(String deviceName, ScannerCallback callback, boolean isTotalMatch) {
        super(callback);
        this.deviceName = deviceName;
        this.isTotalMatch = isTotalMatch;
    }

    @Override
    public void onScanStart() {
        callback.onScanStart();
    }

    @Override
    public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        String name = bluetoothDevice.getName();
        if (TextUtils.isEmpty(name)) return;
        if (isTotalMatch) {
            if (TextUtils.equals(deviceName, name)) {
                callback.onDeviceFounded(bluetoothDevice, i, bytes);
            }
        } else {
            if (name.contains(deviceName)) {
                callback.onDeviceFounded(bluetoothDevice, i, bytes);
            }
        }
    }

    @Override
    public void onScanStop() {
        callback.onScanStop();
    }

    @Override
    public void onScanCanceled() {
        callback.onScanCanceled();
    }
}
