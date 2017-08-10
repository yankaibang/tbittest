package com.example.tbittestlib.bike;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.example.tbittestlib.bike.services.command.callback.StateCallback;
import com.example.tbittestlib.bluetooth.BleClient;
import com.example.tbittestlib.bluetooth.BleGlob;
import com.example.tbittestlib.bluetooth.IBleClient;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.Scanner.ScannerCallback;
import com.example.tbittestlib.bluetooth.debug.BleLog;
import com.example.tbittestlib.bluetooth.debug.LogCallback;
import com.example.tbittestlib.bluetooth.listener.ConnectStateChangeListener;
import com.example.tbittestlib.protocol.ResultCode;
import com.example.tbittestlib.protocol.callback.ResultCallback;
import com.example.tbittestlib.protocol.request.ScannerCommandBuilder;

/**
 * Created by Salmon on 2016/12/5 0005.
 */

class TbitBleInstance implements ConnectStateChangeListener, Handler.Callback, LogCallback {
    private static final String TAG = "TbitBleInstance";
    private IBleClient bleClient;
    private RequestDispatcher requestDispatcher;

    private String deviceId;
    private Byte[] key;

    private ResultCallback connectResultCallback;
    private StateCallback connectStateCallback;

    private Handler handler;

    TbitBleInstance() {
        BleLog.setLogCallback(this);
        handler = new Handler(Looper.getMainLooper(), this);
        bleClient = new BleClient();
        requestDispatcher = new RequestDispatcher(bleClient);
        bleClient.getListenerManager().addConnectStateChangeListener(this);
    }


    int startScan(ScannerCallback callback, long timeout) {
        if (callback == null)
            return ResultCode.FAILED;
        if (!BleGlob.isBluetoothEnabled()) {
            return ResultCode.BLE_NOT_OPENED;
        }
        new ScannerCommandBuilder()
                .setScannerCallback(callback)
                .setStartScan(true)
                .setTimeout(timeout)
                .build()
                .process();
        return ResultCode.SUCCEED;
    }

    void stopScan() {
        if (!BleGlob.isBluetoothEnabled())
            return;
        new ScannerCommandBuilder()
                .setStartScan(false)
                .build()
                .process();
    }

    void destroy() {
        BleLog.setLogCallback(null);

        bleClient.close();
        if (connectResultCallback != null)
            connectResultCallback = null;
        if (connectStateCallback != null)
            connectStateCallback = null;
    }

    private boolean baseCheck(ResultCallback resultCallback) {
        boolean result;
        if (!BleGlob.isBluetoothEnabled()) {
            result = false;
            resultCallback.onResult(ResultCode.BLE_NOT_OPENED);
        } else {
            result = true;
        }
        return result;
    }

    private boolean isKeyLegal(Byte[] key) {
        if (key == null || key.length == 0)
            return false;
        return true;
    }

    private boolean isDeviceIdLegal(String deviceID) {
        return !TextUtils.isEmpty(deviceID);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onLogReceived(String msg) {

    }

    @Override
    public void onConnectionStateChange(int status, int newState) {

    }
}
