package com.example.yankaibang.bluetooth.Scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Salmon on 2016/12/23 0023.
 */

public interface ScannerCallback {

    void onScanStart();

    void onScanStop();

    void onScanCanceled();

    void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes);
}
