package com.example.tbittestlib.bluetooth;


import com.example.tbittestlib.bluetooth.request.BleRequest;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public interface IRequestDispatcher {

    void onRequestFinished(BleRequest request);
}
