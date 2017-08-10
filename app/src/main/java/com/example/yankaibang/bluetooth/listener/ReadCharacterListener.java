package com.example.yankaibang.bluetooth.listener;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public interface ReadCharacterListener {

    void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status, byte[] value);
}
