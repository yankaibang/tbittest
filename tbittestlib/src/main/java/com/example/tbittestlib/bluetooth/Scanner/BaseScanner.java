package com.example.tbittestlib.bluetooth.Scanner;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.tbittestlib.bluetooth.BleGlob;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yankaibang on 2017/8/7.
 */

public abstract class BaseScanner implements Scanner {
    private static final int HANDLE_TIMEOUT = 0;
    protected ScannerCallback mScannerCallback;
    protected BluetoothAdapter mBluetoothAdapter;
    protected Handler mHandler;
    private AtomicBoolean mIsProcessing;

    public BaseScanner() {
        this.mBluetoothAdapter = BleGlob.getBluetoothAdapter();
        mHandler = new BaseScannerHandler(this);
        mIsProcessing = new AtomicBoolean(false);
    }

    @Override
    public void start(final ScannerCallback callback, long timeoutMillis) {
        if(!preperAndCheckState()){
            return;
        }
        cancel();
        this.mScannerCallback = callback;
        if(mIsProcessing.compareAndSet(false, true)) {
            if (callback != null)
                callback.onScanStart();
            mHandler.sendEmptyMessageDelayed(HANDLE_TIMEOUT, timeoutMillis);
            startScan();
        }
    }

    @Override
    public void cancel() {
        if(!preperAndCheckState()){
            return;
        }
        if (!mIsProcessing.compareAndSet(true ,false))
            return;
        if (mScannerCallback != null)
            mScannerCallback.onScanCanceled();
        mHandler.removeCallbacksAndMessages(null);
        stopScan();
        mScannerCallback = null;
    }

    private void timeUp() {
        if(!preperAndCheckState()){
            return;
        }
        if (!mIsProcessing.compareAndSet(true ,false))
            return;
        if (mScannerCallback != null)
            mScannerCallback.onScanStop();
        mHandler.removeCallbacksAndMessages(null);
        stopScan();
        mScannerCallback = null;
    }

    @Override
    public boolean isScanning() {
        return mIsProcessing.get();
    }

    protected boolean preperAndCheckState(){
        return BleGlob.isBluetoothEnabled();
    }

    protected abstract void stopScan();
    protected abstract void startScan();

    private static class BaseScannerHandler extends Handler {
        WeakReference<BaseScanner> scannerReference;

        public BaseScannerHandler(BaseScanner baseScanner) {
            super(Looper.getMainLooper());
            scannerReference = new WeakReference<>(baseScanner);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseScanner scanner = scannerReference.get();
            if (scanner == null)
                return;
            switch (msg.what) {
                case HANDLE_TIMEOUT:
                    scanner.timeUp();
                    break;
                default:
                    break;
            }
        }
    }
}
