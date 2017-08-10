package com.example.tbittestlib.protocol.dispatcher;


import com.example.tbittestlib.protocol.Packet;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public interface PacketResponseListener {

    boolean onPacketReceived(Packet packet);

}
