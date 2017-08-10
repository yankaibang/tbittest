package com.example.tbittestlib.bluetooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Salmon on 2017/3/20 0020.
 */

public class SearchResult {
    private BluetoothDevice device;

    private int rssi;

    private byte[] broadcastData;

    public SearchResult(BluetoothDevice device, int rssi, byte[] broadcastData) {
        this.device = device;
        this.rssi = rssi;
        this.broadcastData = broadcastData;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getBroadcastData() {
        return broadcastData;
    }

    public void setBroadcastData(byte[] broadcastData) {
        this.broadcastData = broadcastData;
    }
}
