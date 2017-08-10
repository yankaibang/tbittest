package com.example.yankaibang.protocol.callback;


import com.example.yankaibang.protocol.Packet;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface PacketCallback {

    void onPacketReceived(Packet packet);

}
