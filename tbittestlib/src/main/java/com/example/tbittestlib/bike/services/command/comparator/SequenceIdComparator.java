package com.example.tbittestlib.bike.services.command.comparator;


import com.example.tbittestlib.protocol.Packet;
import com.example.tbittestlib.protocol.command.Command;

/**
 * Created by Salmon on 2017/3/16 0016.
 */

public class SequenceIdComparator implements CommandComparator {

    @Override
    public boolean compare(Command command, Packet receivedPacket) {
        /*int sendSequenceId = command.getSendPacket().getHeader().getSequenceId();
        int receivedSequenceId = receivedPacket.getHeader().getSequenceId();
        return sendSequenceId == receivedSequenceId;*/
        return false;
    }
}
