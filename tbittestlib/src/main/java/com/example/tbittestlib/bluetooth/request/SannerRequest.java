package com.example.tbittestlib.bluetooth.request;

import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.Scanner.ScanHelper;
import com.example.tbittestlib.bluetooth.Scanner.Scanner;
import com.example.tbittestlib.bluetooth.Scanner.ScannerCallback;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class SannerRequest extends BleRequest {

    private final Scanner mScanner;
    private final boolean mStartScan;
    private final ScannerCallback mScannerCallback;
    private final long mTimeout;

    public SannerRequest(ScannerCallback scannerCallback, BleResponse response, boolean startScan, long timeout) {
        super(response);
        mScanner = ScanHelper.getScanner();
        mStartScan = startScan;
        mTimeout = timeout;
        mScannerCallback = scannerCallback;
    }

    @Override
    protected void onRequest() {
        int resultCode;
        if(mStartScan){
            resultCode = startScan(mScannerCallback, mTimeout);
        } else {
            resultCode = stopScan();
        }

        if(resultCode != Code.REQUEST_SUCCESS){
            response(resultCode);
        }
    }

    private int startScan(ScannerCallback callback, long timeout) {
        if (callback == null)
            return Code.REQUEST_FAILED;
        if (mScanner.isScanning()) {
            return Code.PROCESSING;
        }
        mScanner.start(callback, timeout);
        return Code.REQUEST_SUCCESS;
    }

    private int stopScan() {
        if (mScanner.isScanning())
            mScanner.cancel();
        return Code.REQUEST_SUCCESS;
    }
}
