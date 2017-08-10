package com.example.yankaibang.protocol.command;

import android.bluetooth.BluetoothDevice;

import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.bluetooth.request.BleResponse;
import com.example.yankaibang.bluetooth.request.ConnectRequest;
import com.example.yankaibang.bluetooth.request.DisConnectRequest;
import com.example.yankaibang.bluetooth.request.NotifyRequest;
import com.example.yankaibang.protocol.callback.ResultCallback;

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
