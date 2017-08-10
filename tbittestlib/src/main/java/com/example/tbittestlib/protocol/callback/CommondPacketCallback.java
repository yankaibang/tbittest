package com.example.tbittestlib.protocol.callback;


import com.example.tbittestlib.protocol.Packet;
import com.example.tbittestlib.protocol.command.WriteCommand;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface CommondPacketCallback {

    void onPacketReceived(WriteCommand command, Packet packet);

}
