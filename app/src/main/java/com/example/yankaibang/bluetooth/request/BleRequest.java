package com.example.yankaibang.bluetooth.request;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.yankaibang.bluetooth.BleGlob;
import com.example.yankaibang.bluetooth.Code;
import com.example.yankaibang.bluetooth.IBleClient;
import com.example.yankaibang.bluetooth.IRequestDispatcher;
import com.example.yankaibang.bluetooth.RequestDispatcher;


/**
 * Created by Salmon on 2017/3/22 0022.
 */

public abstract class BleRequest implements Handler.Callback {
    public static final int DEFAULT_REQUEST_TIMEOUT = 5000;
    private static final int HANDLE_TIMEOUT = 0;

    protected IBleClient bleClient;
    protected IRequestDispatcher requestDispatcher;
    protected Handler handler;
    protected boolean isFinished;
    protected BleResponse bleResponse;

    public BleRequest(BleResponse response) {
        this.handler = new Handler(Looper.getMainLooper(), this);
        this.isFinished = false;
        this.bleResponse = response;
    }

    public void setBleClient(IBleClient bleClient) {
        this.bleClient = bleClient;
    }

    public void setRequestDispatcher(IRequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public boolean isFinished() {
        return isFinished;
    }

    protected int getTimeout() {
        return DEFAULT_REQUEST_TIMEOUT;
    }

    public void process() {
        if (!BleGlob.isBleSupported()) {
            response(Code.BLE_NOT_SUPPORTED);
        } else if (!BleGlob.isBluetoothEnabled()) {
            response(Code.BLE_DISABLED);
        } else {
            onPrepare();
            onRequest();
        }
    }

    public void enqueue(RequestDispatcher requestDispatcher) {
        if (requestDispatcher != null) {
            requestDispatcher.addRequest(this);
        } else {
            process();
        }
    }

    protected void startTiming() {
        handler.sendEmptyMessageDelayed(HANDLE_TIMEOUT, getTimeout());
    }

    protected void stopTiming() {
        handler.removeMessages(HANDLE_TIMEOUT);
    }

    protected void onPrepare() {
    }

    protected abstract void onRequest();

    protected void onFinish() {
    }

    protected void response(final int resultCode) {
        if (isFinished)
            return;
        isFinished = true;
        handler.removeCallbacksAndMessages(null);
        handler.post(new Runnable() {
            @Override
            public void run() {
                bleResponse.onResponse(resultCode);
                if (requestDispatcher != null) {
                    requestDispatcher.onRequestFinished(BleRequest.this);
                }
                onFinish();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLE_TIMEOUT:
                response(Code.REQUEST_TIMEOUT);
                break;
        }
        return true;
    }
}
