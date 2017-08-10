package com.example.tbittestlib.protocol;


import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.request.WriterRequest;
import com.example.tbittestlib.protocol.dispatcher.EmptyResponse;
import com.example.tbittestlib.protocol.utils.PacketUtil;

import java.util.UUID;

/**
 * Created by Salmon on 2017/4/21 0021.
 */

public class AckSender {

    private UUID serviceUuid;
    private UUID rxUuid;
    private RequestDispatcher requestDispatcher;

    public AckSender(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public void setRxUuid(UUID rxUuid) {
        this.rxUuid = rxUuid;
    }

    public void sendACK(int sequenceId, boolean error) {
        Packet packet = PacketUtil.createAck(sequenceId, error);

        final byte[] data = packet.toByteArray();

        requestDispatcher.addRequest(new WriterRequest(serviceUuid,
                rxUuid, data, false, new EmptyResponse()));
    }
}
