package com.example.yankaibang.protocol.callback;


import com.example.yankaibang.protocol.Packet;
import com.example.yankaibang.protocol.command.Command;
import com.example.yankaibang.protocol.command.WriteCommand;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface CommondPacketCallback {

    void onPacketReceived(WriteCommand command, Packet packet);

}
