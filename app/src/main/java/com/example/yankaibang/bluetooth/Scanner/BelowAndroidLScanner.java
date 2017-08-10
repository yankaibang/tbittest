package com.example.yankaibang.bluetooth.Scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

/**
 * Created by yankaibang on 2017/8/7.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class BelowAndroidLScanner extends BaseScanner {

    private BluetoothAdapter.LeScanCallback bleCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, final int i, final byte[] bytes) {
            if (!isScanning())
                return;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mScannerCallback != null)
                        mScannerCallback.onDeviceFounded(bluetoothDevice, i, bytes);
                }
            });
        }
    };

    private BelowAndroidLScanner(){}

    private static final class Holder{
        public static final BelowAndroidLScanner instance = new BelowAndroidLScanner();
    }

    public static BelowAndroidLScanner getInstance(){
        return Holder.instance;
    }

    @Override
    protected void stopScan() {
        mBluetoothAdapter.stopLeScan(bleCallback);
    }

    @Override
    protected void startScan() {
        mBluetoothAdapter.startLeScan(bleCallback);
    }
}
