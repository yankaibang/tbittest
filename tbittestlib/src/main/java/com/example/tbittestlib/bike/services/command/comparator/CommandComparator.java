package com.example.tbittestlib.bike.services.command.comparator;


import com.example.tbittestlib.protocol.Packet;
import com.example.tbittestlib.protocol.command.Command;

/**
 * Created by Salmon on 2017/3/16 0016.
 */

public interface CommandComparator {

    boolean compare(Command command, Packet receivedPacket);

}
