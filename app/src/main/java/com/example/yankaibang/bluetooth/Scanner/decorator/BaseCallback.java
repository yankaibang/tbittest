package com.example.yankaibang.bluetooth.Scanner.decorator;


import com.example.yankaibang.bluetooth.Scanner.ScannerCallback;

/**
 * Created by Salmon on 2017/3/8 0008.
 */

public abstract class BaseCallback implements ScannerCallback {

    protected ScannerCallback callback;

    public BaseCallback(ScannerCallback callback) {
        this.callback = callback;
    }
}
