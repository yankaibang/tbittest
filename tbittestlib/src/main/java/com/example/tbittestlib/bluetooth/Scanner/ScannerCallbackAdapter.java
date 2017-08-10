package com.example.tbittestlib.bluetooth.Scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Salmon on 2017/3/8 0008.
 */

public abstract class ScannerCallbackAdapter implements ScannerCallback {
    @Override
    public void onScanStart() {

    }

    @Override
    public void onScanStop() {

    }

    @Override
    public void onScanCanceled() {

    }

    @Override
    public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

    }
}
