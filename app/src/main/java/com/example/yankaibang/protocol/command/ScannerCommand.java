package com.example.yankaibang.protocol.command;

import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;
import com.example.yankaibang.bluetooth.request.BleResponse;
import com.example.yankaibang.bluetooth.request.SannerRequest;
import com.example.yankaibang.protocol.callback.ResultCallback;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class ScannerCommand extends Command {
    private ResultCallback mResultCallback;
    private ScannerCallback mScannerCallback;
    private long mTimeout;
    private boolean mStartScan;
    private BleResponse mBleResponse;

    public ScannerCommand(ResultCallback resultCallback, ScannerCallback scannerCallback, long timeout, boolean startScan) {
        mResultCallback = resultCallback;
        mScannerCallback = scannerCallback;
        mTimeout = timeout;
        mStartScan = startScan;
        mBleResponse = new BleResponse() {
            @Override
            public void onResponse(int resultCode) {
                if(mResultCallback != null){
                    mResultCallback.onResult(resultCode);
                }
            }
        };
    }

    @Override
    public boolean process() {
        new SannerRequest(mScannerCallback, mBleResponse, mStartScan, mTimeout).enqueue(null);
        return true;
    }
}
