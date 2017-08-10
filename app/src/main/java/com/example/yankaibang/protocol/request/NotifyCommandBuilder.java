package com.example.yankaibang.protocol.request;

import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.command.NotifyCommand;
import com.example.yankaibang.protocol.config.BleManager;

import java.util.UUID;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class NotifyCommandBuilder {
    private UUID mService;
    private UUID mCharacter;
    private UUID mDescriptor;
    private boolean mIsEnable;
    private RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;

    public NotifyCommandBuilder() {
        mRequestDispatcher = BleManager.getInstance().getRequestDispatcher();
    }

    public NotifyCommandBuilder setService(UUID service) {
        mService = service;
        return this;
    }

    public NotifyCommandBuilder setCharacter(UUID character) {
        mCharacter = character;
        return this;
    }

    public NotifyCommandBuilder setDescriptor(UUID descriptor) {
        mDescriptor = descriptor;
        return this;
    }

    public NotifyCommandBuilder setEnable(boolean enable) {
        mIsEnable = enable;
        return this;
    }

    public NotifyCommandBuilder setResultCallback(ResultCallback resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    public NotifyCommand build() {
        return new NotifyCommand(mRequestDispatcher, mResultCallback, mService, mCharacter, mDescriptor, mIsEnable);
    }
}
