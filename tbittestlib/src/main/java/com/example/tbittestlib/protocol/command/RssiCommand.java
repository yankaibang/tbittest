package com.example.tbittestlib.protocol.command;

import com.example.tbittestlib.bluetooth.BleClient;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.request.RssiRequest;
import com.example.tbittestlib.bluetooth.request.RssiResponse;
import com.example.tbittestlib.protocol.callback.ResultCallback;

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
