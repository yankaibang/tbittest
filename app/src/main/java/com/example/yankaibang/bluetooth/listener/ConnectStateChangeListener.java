package com.example.yankaibang.bluetooth.listener;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public interface ConnectStateChangeListener {

    void onConnectionStateChange(int status, int newState);
}
