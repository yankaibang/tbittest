package com.example.yankaibang.protocol.request;

import com.example.yankaibang.bluetooth.BleClient;
import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.bluetooth.request.RssiResponse;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.callback.RssiCallback;
import com.example.yankaibang.protocol.command.RssiCommand;
import com.example.yankaibang.protocol.config.BleManager;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class RssiRequestBuilder {
    private RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;
    private BleClient mBleClient;
    private RssiResponse mRssiResponse;

    public RssiRequestBuilder() {
        mBleClient = BleManager.getInstance().getBleClient();
    }

    public RssiRequestBuilder setRequestDispatcher(RequestDispatcher requestDispatcher) {
        mRequestDispatcher = requestDispatcher;
        return this;
    }

    public RssiRequestBuilder setResultCallback(ResultCallback resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    public RssiRequestBuilder setRssiCallback(RssiResponse rssiResponse) {
        mRssiResponse = rssiResponse;
        return this;
    }

    public RssiCommand build() {
        return new RssiCommand(mRequestDispatcher, mRssiResponse, mResultCallback, mBleClient);
    }
}
