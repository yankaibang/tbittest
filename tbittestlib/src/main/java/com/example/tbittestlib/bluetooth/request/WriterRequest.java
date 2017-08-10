package com.example.tbittestlib.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.listener.WriteCharacterListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public class WriterRequest extends BleRequest implements WriteCharacterListener {

    private static final int MAX_LENGTH = 20;

    private UUID serviceUuid;
    private UUID characterUuid;
    private byte[] value;
    private boolean withResponse;
    private int timeout;
    private List<byte[]> valueList;

    public WriterRequest(UUID serviceUuid, UUID characterUuid, byte[] value, boolean withResponse,
                         BleResponse bleResponse) {
        this(serviceUuid, characterUuid, value, withResponse, bleResponse, DEFAULT_REQUEST_TIMEOUT);
    }

    public WriterRequest(UUID serviceUuid, UUID characterUuid, byte[] value, boolean withResponse,
                         BleResponse bleResponse, int timeout) {
        super(bleResponse);

        this.serviceUuid = serviceUuid;
        this.characterUuid = characterUuid;
        this.value = value;
        this.withResponse = withResponse;
        this.timeout = timeout;
        this.valueList = new LinkedList<>();

        if (value == null || value.length == 0)
            response(Code.REQUEST_FAILED);

        prepareList();
    }

    @Override
    protected void onPrepare() {
        super.onPrepare();
        bleClient.getListenerManager().addWriteCharacterListener(this);
    }

    private void prepareList() {
        int start = 0;
        int end = value.length;
        while ((end - start) > MAX_LENGTH) {
            byte[] bytes = Arrays.copyOfRange(value, start, start + MAX_LENGTH);
            valueList.add(bytes);
            start += MAX_LENGTH;
        }

        byte[] bytes = Arrays.copyOfRange(value, start, end);
        valueList.add(bytes);
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    protected void onRequest() {
        doWrite();

        startTiming();
    }

    private void doWrite() {
        if (valueList.size() == 0) {
            stopTiming();
            response(Code.REQUEST_SUCCESS);
            return;
        }
        byte[] value = valueList.remove(0);
        if (!bleClient.write(serviceUuid, characterUuid, value, withResponse))
            response(Code.REQUEST_FAILED);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic,
                                      int status, byte[] value) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            doWrite();
        } else {
            response(Code.REQUEST_FAILED);
        }
    }

    @Override
    protected void onFinish() {
        bleClient.getListenerManager().removeWriteCharacterListener(this);
    }
}
