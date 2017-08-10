package com.example.tbittestlib.bluetooth.listener;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public interface WriteCharacterListener {

    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status, byte[] value);

}
