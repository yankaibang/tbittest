package com.example.yankaibang.bluetooth.Scanner.decorator;

import android.bluetooth.BluetoothDevice;


import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Salmon on 2017/3/8 0008.
 */

public class NoneRepeatCallback extends BaseCallback {
    private List<String> addressList = Collections.synchronizedList(new ArrayList<String>());

    public NoneRepeatCallback(ScannerCallback callback) {
        super(callback);
    }

    @Override
    public void onScanStart() {
        addressList.clear();
        callback.onScanStart();
    }

    @Override
    public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        String address = bluetoothDevice.getAddress();
        if (!addressList.contains(address)) {
            addressList.add(address);
            callback.onDeviceFounded(bluetoothDevice, i, bytes);
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
