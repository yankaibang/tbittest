package com.example.tbittestlib.bluetooth.request;

import com.example.tbittestlib.bluetooth.BleClient;
import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.RequestDispatcher;

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
