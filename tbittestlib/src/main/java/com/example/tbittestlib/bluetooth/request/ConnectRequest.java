package com.example.tbittestlib.bluetooth.request;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;

import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.debug.BleLog;
import com.example.tbittestlib.bluetooth.listener.ConnectStateChangeListener;
import com.example.tbittestlib.bluetooth.listener.ServiceDiscoverListener;


/**
 * Created by Salmon on 2017/3/24 0024.
 */

public class ConnectRequest extends BleRequest implements ConnectStateChangeListener,
        ServiceDiscoverListener {

    private static final int DEFAULT_RETRY_TIME = 0;

    private int retryCount;
    private int maxRetryTimes;
    private BluetoothDevice bluetoothDevice;

    public ConnectRequest(BluetoothDevice bluetoothDevice, BleResponse response) {
        this(bluetoothDevice, response, DEFAULT_RETRY_TIME);
    }

    public ConnectRequest(BluetoothDevice bluetoothDevice, BleResponse response, int maxRetryTimes) {
        super(response);

        this.maxRetryTimes = maxRetryTimes;
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    protected void onPrepare() {
        super.onPrepare();
        bleClient.getListenerManager().addConnectStateChangeListener(this);
        bleClient.getListenerManager().addServiceDiscoverListener(this);
    }

    @Override
    protected void onRequest() {
        doConnect();

        startTiming();
    }

    private void doConnect() {
        bleClient.connect(bluetoothDevice, false);
    }

    @Override
    protected void onFinish() {
        bleClient.getListenerManager().removeConnectStateChangeListener(this);
        bleClient.getListenerManager().removeServiceDiscoverListener(this);
    }

    private void tryReconnect() {
        BleLog.log("ConnectRequest", "tryReconnect");
        if (retryCount < maxRetryTimes) {
            doConnect();
            retryCount++;
        } else {
            response(Code.REQUEST_FAILED);
        }
    }

    @Override
    public void onConnectionStateChange(int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED)
            tryReconnect();
    }

    @Override
    public void onServicesDiscovered(final int status) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (status != BluetoothGatt.GATT_SUCCESS)
                    tryReconnect();
                else {
                    stopTiming();
                    response(Code.REQUEST_SUCCESS);
                }
            }
        });
    }
}
