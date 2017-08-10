package com.example.tbittestlib.protocol;


import com.example.tbittestlib.bluetooth.util.ByteUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Salmon on 2016/5/17 0017.
 */
public class PacketValue implements Cloneable {
    List<Byte> aPacketValue = new LinkedList<>();
    List<DataBean> mData = new LinkedList<>();

    private final String TAG = "PacketValue";

    public PacketValue() {

    }

    public int getSize() {
        return aPacketValue.size();
    }

    public PacketValue(byte[] data) {
        aPacketValue.addAll(Arrays.asList(ByteUtil.byteArrayToBoxed(data)));
        initData();
    }

    private void initData() {
        if (aPacketValue.size() < 5) {
            return;
        }

        int cursor = 2;
        int size = aPacketValue.size();
        try {
            while (cursor < size) {
                byte key = aPacketValue.get(cursor);
                cursor ++;
                byte length = aPacketValue.get(cursor);
                cursor ++;
                Byte[] value = aPacketValue.subList(cursor, cursor + length).toArray(new Byte[length]);
                DataBean d = new DataBean(key, value);
                mData.add(d);

                cursor += length;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte getCommandId() {
        return aPacketValue.get(0);
    }

    public void setCommandId(byte commandId) {
        aPacketValue.add(commandId);
        aPacketValue.add((byte) 0x00);
    }

    public void addData(DataBean d) {
        mData.add(d);
        aPacketValue.add(d.key);
        if (d.value != null) {
            aPacketValue.add((byte) d.value.length);
            for (byte b : d.value) {
                aPacketValue.add(b);
            }
        } else {
            aPacketValue.add((byte) 0x00);
        }
    }

    public void addData(DataBean... dataBeens) {
        for (DataBean bean : dataBeens) {
            addData(bean);
        }
    }

    public List<DataBean> getData() {
        return mData;
    }

    public List<Byte> toList() {
        return aPacketValue;
    }

    public byte[] toArray() {
        Byte[] value = new Byte[aPacketValue.size()];
        aPacketValue.toArray(value);
        return ByteUtil.byteArrayToUnBoxed(value);
    }

    @Override
    public String toString() {
        return ByteUtil.bytesToHexString(toArray());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static class DataBean {
        public byte key;
        public Byte[] value;

        public DataBean(byte key, Byte[] value) {
            this.key = key;
            this.value = value;
        }
    }
}
