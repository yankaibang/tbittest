package com.example.tbittestlib.protocol.dispatcher;


import com.example.tbittestlib.bluetooth.request.BleResponse;

/**
 * Created by Salmon on 2017/3/24 0024.
 */

public class EmptyResponse implements BleResponse {
    @Override
    public void onResponse(int resultCode) {

    }
}
