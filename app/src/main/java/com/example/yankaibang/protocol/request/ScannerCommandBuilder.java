package com.example.yankaibang.protocol.request;

import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;
import com.example.yankaibang.bluetooth.request.BleResponse;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.command.ScannerCommand;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class ScannerCommandBuilder {
    private ResultCallback mResultCallback;
    private ScannerCallback mScannerCallback;
    private long mTimeout;
    private boolean mStartScan;

    public ScannerCommandBuilder setResultCallback(ResultCallback resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    public ScannerCommandBuilder setScannerCallback(ScannerCallback scannerCallback) {
        mScannerCallback = scannerCallback;
        return this;
    }

    public ScannerCommandBuilder setTimeout(long timeout) {
        mTimeout = timeout;
        return this;
    }

    public ScannerCommandBuilder setStartScan(boolean startScan) {
        mStartScan = startScan;
        return this;
    }

    public ScannerCommand build() {
        return new ScannerCommand(mResultCallback, mScannerCallback, mTimeout, mStartScan);
    }
}
