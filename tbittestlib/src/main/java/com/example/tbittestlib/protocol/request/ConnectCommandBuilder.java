package com.example.tbittestlib.protocol.request;

import android.bluetooth.BluetoothDevice;

import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.request.BleResponse;
import com.example.tbittestlib.protocol.callback.ResultCallback;
import com.example.tbittestlib.protocol.command.ConnectCommand;
import com.example.tbittestlib.protocol.config.BleManager;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class ConnectCommandBuilder {
    private RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;
    private int mMaxRetryTimes;
    private BluetoothDevice mBluetoothDevice;
    private BleResponse mBleResponse;
    private boolean mConnect;

    public ConnectCommandBuilder() {
        mRequestDispatcher = BleManager.getInstance().getRequestDispatcher();
    }

    public ConnectCommandBuilder setResultCallback(ResultCallback resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    public ConnectCommandBuilder setMaxRetryTimes(int maxRetryTimes) {
        mMaxRetryTimes = maxRetryTimes;
        return this;
    }

    public ConnectCommandBuilder setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
        return this;
    }

    public ConnectCommandBuilder setBleResponse(BleResponse bleResponse) {
        mBleResponse = bleResponse;
        return this;
    }

    public ConnectCommandBuilder setConnect(boolean connect) {
        mConnect = connect;
        return this;
    }

    public ConnectCommand build() {
        return new ConnectCommand(mRequestDispatcher, mResultCallback, mMaxRetryTimes, mBluetoothDevice, mConnect);
    }
}
