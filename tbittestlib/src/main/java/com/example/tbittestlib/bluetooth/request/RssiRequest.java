package com.example.tbittestlib.bluetooth.request;

import android.bluetooth.BluetoothGatt;

import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.listener.ReadRssiListener;


/**
 * Created by Salmon on 2017/4/10 0010.
 */

public class RssiRequest extends BleRequest implements ReadRssiListener {
    private RssiResponse rssiResponse;
    public RssiRequest(RssiResponse rssiResponse) {
        super(rssiResponse);

        this.rssiResponse = rssiResponse;
    }

    @Override
    protected void onPrepare() {
        super.onPrepare();
        bleClient.getListenerManager().addReadRssiListener(this);
    }

    @Override
    protected void onRequest() {
        if (!bleClient.readRssi()) {
            response(Code.REQUEST_FAILED);
            return;
        }

        startTiming();
    }

    @Override
    protected int getTimeout() {
        return 3000;
    }

    @Override
    public void onReadRemoteRssi(final int rssi, int status) {
        stopTiming();

        if (BluetoothGatt.GATT_SUCCESS == status) {
            response(Code.REQUEST_SUCCESS);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    rssiResponse.onRssi(rssi);
                }
            });
        } else {
            response(Code.REQUEST_FAILED);
        }
    }

    @Override
    protected void onFinish() {
        bleClient.getListenerManager().removeReadRssiListener(this);
    }
}
