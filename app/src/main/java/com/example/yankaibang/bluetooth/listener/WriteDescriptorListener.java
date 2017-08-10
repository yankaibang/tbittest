package com.example.yankaibang.bluetooth.listener;

import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public interface WriteDescriptorListener {

    void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status);

}
