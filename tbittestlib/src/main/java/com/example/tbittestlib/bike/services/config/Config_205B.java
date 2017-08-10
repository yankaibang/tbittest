package com.example.tbittestlib.bike.services.config;


import com.example.tbittestlib.bike.services.command.comparator.CommandComparator;
import com.example.tbittestlib.bike.services.command.comparator.CommandInsideComparator;
import com.example.tbittestlib.bike.services.resolver.BeforeW207Resolver;
import com.example.tbittestlib.bike.services.resolver.Resolver;

import java.util.UUID;

/**
 * Created by Salmon on 2017/3/20 0020.
 */

public class Config_205B implements BikeConfig {

    private Uuid uuid;
    private CommandComparator commandComparator;
    private Resolver resolver;

    public Config_205B() {
        uuid = new Uuid();
        uuid.SPS_SERVICE_UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7");
        uuid.SPS_TX_UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8");
        uuid.SPS_RX_UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba");
        uuid.SPS_NOTIFY_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        uuid.SPS_CTRL_UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb9");

        commandComparator = new CommandInsideComparator();
        resolver = new BeforeW207Resolver();
    }

    @Override
    public Uuid getUuid() {
        return uuid;
    }

    @Override
    public CommandComparator getComparator() {
        return commandComparator;
    }

    @Override
    public Resolver getResolver() {
        return resolver;
    }
}
