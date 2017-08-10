package com.example.tbittestlib.protocol.callback;


import com.example.tbittestlib.protocol.Packet;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface PacketCallback {

    void onPacketReceived(Packet packet);

}
