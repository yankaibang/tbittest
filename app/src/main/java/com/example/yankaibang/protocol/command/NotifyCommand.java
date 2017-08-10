package com.example.yankaibang.protocol.command;

import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.bluetooth.request.BleResponse;
import com.example.yankaibang.bluetooth.request.NotifyRequest;
import com.example.yankaibang.protocol.callback.ResultCallback;

import java.util.UUID;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class NotifyCommand extends Command{
    private UUID mService;
    private UUID mCharacter;
    private UUID mDescriptor;
    private boolean mIsEnable;
    private RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;

    public NotifyCommand(RequestDispatcher requestDispatcher, ResultCallback resultCallback, UUID service, UUID character, UUID descriptor, boolean isEnable) {
        mRequestDispatcher = requestDispatcher;
        mResultCallback = resultCallback;
        mService = service;
        mCharacter = character;
        mDescriptor = descriptor;
        mIsEnable = isEnable;
    }

    @Override
    public boolean process() {
        new NotifyRequest(new BleResponse() {
            @Override
            public void onResponse(int resultCode) {
                if(mResultCallback != null){
                    mResultCallback.onResult(resultCode);
                }
            }
        }, mService, mCharacter, mDescriptor, mIsEnable).enqueue(mRequestDispatcher);
        return true;
    }
}
