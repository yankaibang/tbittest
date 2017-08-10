package com.example.tbittestlib.protocol.command;

import android.bluetooth.BluetoothDevice;

import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.request.BleResponse;
import com.example.tbittestlib.bluetooth.request.ConnectRequest;
import com.example.tbittestlib.bluetooth.request.DisConnectRequest;
import com.example.tbittestlib.protocol.callback.ResultCallback;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class ConnectCommand extends Command{
    private RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;
    private int mMaxRetryTimes;
    private BluetoothDevice mBluetoothDevice;
    private BleResponse mBleResponse;
    private boolean mConnect;

    public ConnectCommand(RequestDispatcher requestDispatcher, ResultCallback resultCallback, int maxRetryTimes, BluetoothDevice bluetoothDevice, boolean connect) {
        mRequestDispatcher = requestDispatcher;
        mResultCallback = resultCallback;
        mMaxRetryTimes = maxRetryTimes;
        mBluetoothDevice = bluetoothDevice;
        mConnect = connect;
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
        if(mConnect){
            connect();
        } else {
            disConnect();
        }
        return true;
    }

    private void connect(){
        if(mMaxRetryTimes > 0) {
            new ConnectRequest(mBluetoothDevice, mBleResponse, mMaxRetryTimes).enqueue(mRequestDispatcher);
        } else {
            new ConnectRequest(mBluetoothDevice, mBleResponse).enqueue(mRequestDispatcher);
        }
    }

    private void disConnect(){
        new DisConnectRequest(mBleResponse).enqueue(mRequestDispatcher);
    }
}
