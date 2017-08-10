package com.example.yankaibang.protocol.command;

import com.example.yankaibang.bluetooth.BleClient;
import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.bluetooth.request.RssiRequest;
import com.example.yankaibang.bluetooth.request.RssiResponse;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.callback.RssiCallback;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class RssiCommand extends Command {

    private final RequestDispatcher mRequestDispatcher;
    private final ResultCallback mResultCallback;
    private final BleClient mBleClient;
    private final RssiResponse mRssiResponse;

    public RssiCommand(RequestDispatcher requestDispatcher, RssiResponse rssiResponse, ResultCallback resultCallback, BleClient bleClient) {
        mRequestDispatcher = requestDispatcher;
        mResultCallback = resultCallback;
        mBleClient = bleClient;
        mRssiResponse = rssiResponse;
    }

    @Override
    public boolean process() {
        readRssi(mResultCallback, mRssiResponse);
        return true;
    }

    private void readRssi(final ResultCallback resultCallback, final RssiResponse rssiResponse) {
        new RssiRequest(rssiResponse).enqueue(mRequestDispatcher);
    }
}
