package com.example.yankaibang.bike.services.command.comparator;

import com.example.yankaibang.protocol.Packet;
import com.example.yankaibang.protocol.command.Command;

/**
 * Created by Salmon on 2017/3/16 0016.
 */

public class CommandInsideComparator implements CommandComparator {

    @Override
    public boolean compare(Command command, Packet receivedPacket) {
//        return command.compare(receivedPacket);
        return false;
    }
}
