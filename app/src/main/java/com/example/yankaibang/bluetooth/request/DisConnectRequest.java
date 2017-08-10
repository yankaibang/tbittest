package com.example.yankaibang.bluetooth.request;

import com.example.yankaibang.bluetooth.BleClient;
import com.example.yankaibang.bluetooth.Code;
import com.example.yankaibang.bluetooth.RequestDispatcher;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class DisConnectRequest extends BleRequest {

    public DisConnectRequest(BleResponse response) {
        super(response);
    }

    @Override
    protected void onRequest() {
        bleClient.disconnect();
        response(Code.REQUEST_SUCCESS);
    }

    @Override
    public void enqueue(RequestDispatcher requestDispatcher) {
        process();
    }
}
