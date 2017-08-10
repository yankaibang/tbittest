package com.example.yankaibang.protocol;

import java.util.Arrays;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public class Packet {

    private PacketHeader header;

    private PacketValue packetValue;

    public Packet(PacketHeader header, PacketValue packetValue) {
        this.packetValue = packetValue;
        this.header = header;
    }

    public Packet(byte[] raw) {
        this.header = new PacketHeader(Arrays.copyOfRange(raw, 0, 8));
        this.packetValue = new PacketValue(Arrays.copyOfRange(raw, 8, raw.length));
    }

    public Packet(PacketHeader header) {
        this.header = header;
    }

    public PacketHeader getHeader() {
        return header;
    }

    public PacketValue getPacketValue() {
        return packetValue;
    }

    public byte[] toByteArray() {
        if (packetValue == null || packetValue.getSize() == 0)
            return header.getData();

        byte[] headData = header.getData();
        byte[] valueData = packetValue.toArray();

        byte[] result = new byte[headData.length + valueData.length];

        System.arraycopy(headData, 0, result, 0, headData.length);
        System.arraycopy(valueData, 0, result, headData.length, valueData.length);

        return result;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "header=" + header.toString() +
                ", value=" + packetValue.toString() +
                '}';
    }
}
