package com.example.yankaibang.protocol.dispatcher;


import com.example.yankaibang.protocol.Packet;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public interface PacketResponseListener {

    boolean onPacketReceived(Packet packet);

}
