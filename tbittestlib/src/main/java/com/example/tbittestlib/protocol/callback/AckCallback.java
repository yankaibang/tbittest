package com.example.tbittestlib.protocol.callback;


import com.example.tbittestlib.protocol.command.WriteCommand;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface AckCallback {
    void onAckSuccess(WriteCommand command);
}
