package com.example.tbittestlib.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;


import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.listener.WriteDescriptorListener;

import java.util.UUID;

/**
 * Created by Salmon on 2017/4/12 0012.
 */

public class NotifyRequest extends BleRequest implements WriteDescriptorListener {

    private UUID service;
    private UUID character;
    private UUID descriptor;
    private boolean isEnable;

    public NotifyRequest(BleResponse response, UUID service, UUID character, UUID descriptor, boolean isEnable) {
        super(response);

        this.service = service;
        this.character = character;
        this.descriptor = descriptor;
        this.isEnable = isEnable;
    }

    @Override
    protected void onPrepare() {
        super.onPrepare();
        bleClient.getListenerManager().addWriteDescriptorListener(this);
    }

    @Override
    protected void onRequest() {
        if (!bleClient.setCharacteristicNotification(service, character, descriptor, isEnable)) {
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
    public void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status) {
        stopTiming();

        if (BluetoothGatt.GATT_SUCCESS == status) {
            response(Code.REQUEST_SUCCESS);
        } else {
            response(Code.REQUEST_FAILED);
        }
    }

    @Override
    protected void onFinish() {
        bleClient.getListenerManager().removeWriteDescriptorListener(this);
    }
}
