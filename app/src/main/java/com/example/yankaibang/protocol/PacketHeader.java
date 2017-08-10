package com.example.yankaibang.protocol;


import com.example.yankaibang.bluetooth.util.ByteUtil;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public class PacketHeader {

    private byte[] header = {(byte) 0xAA, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    public PacketHeader() {
    }

    public PacketHeader(byte[] header) {
        this.header = header;
    }

    public byte getSystemState() {
        return header[2];
    }

    public void setSystemState(byte systemState) {
        header[2] = systemState;
    }

    public short getLength() {
        int value;
        value = header[4] & 0x000000ff;
        value = value << 8;
        value |= header[5] & 0x000000ff;
        return (short) value;

    }

    public void setLength(short length) {
        byte[] len = ByteUtil.shortToByte(length);
        header[4] = len[0];
        header[5] = len[1];
    }

    public short getCRC16() {
        int value;
        value = header[6] & 0x000000ff;
        value = value << 8;
        value |= header[7] & 0x000000ff;
        return (short) value;
    }

    public void setCRC16(short crc16) {
        byte[] crc = ByteUtil.shortToByte(crc16);
        header[6] = crc[0];
        header[7] = crc[1];
    }

    public byte getSequenceId() {
        return header[3];
    }

    public void setSequenceId(byte sequenceId) {
        header[3] = sequenceId;
    }

    public boolean isAck() {
        return ((header[1] & 0x10) == 0x10);
    }

    public void setACK(boolean ack) {
        byte sta = header[1];
        if (ack == true) {
            sta |= (byte) 0x10;
        } else {
            sta &= ~(byte) 0x10;
        }
        header[1] = sta;
    }

    public boolean isError() {
        return ((header[1] & 0x20) == 0x20);
    }

    public void setError(boolean err) {
        byte sta = header[1];
        if (err == true) {
            sta |= (byte) 0x20;
        } else {
            sta &= ~(byte) 0x20;
        }
        header[1] = sta;
    }

    public byte[] getData() {
        return header;
    }

    @Override
    public String toString() {
        return ByteUtil.bytesToHexString(header);
    }

    public static class HeaderBuilder {
        private PacketHeader header = new PacketHeader();

        public HeaderBuilder setSystemState(byte systemState) {
            header.setSystemState(systemState);
            return this;
        }

        public HeaderBuilder setAck(boolean isAck) {
            header.setACK(isAck);
            return this;
        }

        public HeaderBuilder setCRC16(short crc16) {
            header.setCRC16(crc16);
            return this;
        }

        public HeaderBuilder setError(boolean err) {
            header.setError(err);
            return this;
        }

        public HeaderBuilder setLength(short length) {
            header.setLength(length);
            return this;
        }

        public HeaderBuilder setSequenceId(byte sequenceId) {
            header.setSequenceId(sequenceId);
            return this;
        }

        public PacketHeader build() {
            return header;
        }
    }
}
