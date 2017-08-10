package com.example.tbittestlib.bluetooth.Scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yankaibang on 2017/8/7.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class AndroidLScanner extends BaseScanner {

    private static final String TAG = "AndroidLScanner";

    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback bleCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            Log.d(TAG, "onScanResult: ");
            if (!isScanning())
                return;
            BluetoothDevice device = result.getDevice();
            Integer rssi = result.getRssi();
            byte[] bytes = result.getScanRecord().getBytes();
            if (mScannerCallback != null)
                mScannerCallback.onDeviceFounded(device, rssi, bytes);
        }

    };

    private AndroidLScanner(){}

    private static final class Holder{
        public static final AndroidLScanner instance = new AndroidLScanner();
    }

    public static AndroidLScanner getInstance(){
        return Holder.instance;
    }

    @Override
    protected void stopScan() {
        mBluetoothLeScanner.stopScan(bleCallback);
    }

    @Override
    protected void startScan() {
        mBluetoothLeScanner.startScan(getFilters(),getSettings(),bleCallback);
    }

    @Override
    protected boolean preperAndCheckState() {
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        return super.preperAndCheckState() && mBluetoothLeScanner != null;
    }

    private List<ScanFilter> getFilters() {
        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter.Builder builder = new ScanFilter.Builder();
        filters.add(builder.build());
        return filters;
    }

    private ScanSettings getSettings() {
        ScanSettings.Builder settingBuilder = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        return settingBuilder.build();
    }
}
