package com.example.yankaibang.bike.util;


import com.example.yankaibang.protocol.Packet;
import com.example.yankaibang.protocol.PacketHeader;
import com.example.yankaibang.protocol.PacketValue;
import com.example.yankaibang.protocol.ProtocolInfo;
import com.example.yankaibang.protocol.utils.CrcUtil;

/**
 * Created by Salmon on 2017/3/14 0014.
 */

public class PacketUtil {

    public static Packet createPacket(int sequenceId, byte commandId, byte key, Byte[] data) {
        return createPacket(sequenceId, commandId, new PacketValue.DataBean(key, data));
    }

    public static Packet createPacket(int sequenceId, byte commandId, PacketValue.DataBean... dataBeans) {
        PacketValue packetValue = new PacketValue();
        packetValue.setCommandId(commandId);
        if (dataBeans != null)
            packetValue.addData(dataBeans);

        short valueLength = (short) packetValue.getSize();
        int crc16 = CrcUtil.crcTable(ProtocolInfo.packetCrcTable, packetValue.toArray());

        PacketHeader header = new PacketHeader.HeaderBuilder()
                .setLength(valueLength)
                .setSystemState((byte) 0x00)
                .setSequenceId((byte) sequenceId)
                .setAck(false)
                .setError(false)
                .setCRC16((short) crc16)
                .build();


        Packet newPacket = new Packet(header, packetValue);

        return newPacket;
    }

    public static Packet createAck(int sequenceId, boolean error) {
        PacketHeader packetHeader = new PacketHeader.HeaderBuilder()
                .setAck(true)
                .setError(error)
                .setSequenceId((byte) sequenceId)
                .setSystemState((byte) 0x00)
                .setLength((short) 0)
                .setCRC16((short) 0)
                .build();

        return new Packet(packetHeader);
    }

    public static Packet createAck(int sequenceId) {
       return createAck(sequenceId, false);
    }

    public static boolean compareCommandId(Packet packet1, Packet packet2) {
        int commandId1 = packet1.getPacketValue().getCommandId();
        int commandId2 = packet2.getPacketValue().getCommandId();
        return commandId1 == commandId2;
    }

    public static boolean checkPacketValueContainKey(Packet packet, int key) {
        boolean result = false;
        for (PacketValue.DataBean b : packet.getPacketValue().getData()) {
            int packetKey = b.key & 0xff;
            if (packetKey == key)
                result = true;
        }
        return  result;
    }

}

