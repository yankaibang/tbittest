package com.example.yankaibang.protocol;


import com.example.yankaibang.bike.services.config.ConfigDispatcher;
import com.example.yankaibang.bike.services.config.DefaultConfigDispatcher;

/**
 * Created by Salmon on 2017/4/12 0012.
 */

public abstract class ProtocolAdapter {

    public abstract int[] getPacketCrcTable();

    public abstract char[] getAdKey();

    public abstract int getMaxAdEncryptedCount();

    public ConfigDispatcher getConfigDispatcher() {
        return new DefaultConfigDispatcher();
    }

}
