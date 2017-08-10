package com.example.tbittestlib.bluetooth.listener;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public interface ChangeCharacterListener {

    void onCharacterChange(BluetoothGattCharacteristic characteristic, byte[] value);

}
