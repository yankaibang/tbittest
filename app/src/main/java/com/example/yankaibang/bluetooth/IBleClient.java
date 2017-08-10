package com.example.yankaibang.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public interface IBleClient {

    void connect(BluetoothDevice device, boolean autoConnect);

    void disconnect();

    void close();

    int getConnectionState();

    ListenerManager getListenerManager();

    boolean setCharacteristicNotification(UUID service, UUID character, UUID descriptor, boolean enable);

    boolean write(UUID serviceUUID, UUID characteristicUUID, byte[] value, boolean withResponse);

    boolean readRssi();

    boolean requestConnectionPriority(int connectionPriority);
}
