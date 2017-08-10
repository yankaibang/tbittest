package com.example.yankaibang.protocol.command;


import com.example.yankaibang.protocol.Packet;

/**
 * Created by Salmon on 2017/3/16 0016.
 */

public interface CommandComparator {

    boolean compare(Packet sendPacket, Packet receivedPacket);

}
