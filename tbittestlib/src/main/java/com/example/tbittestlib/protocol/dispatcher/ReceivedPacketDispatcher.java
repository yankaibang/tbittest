package com.example.tbittestlib.protocol.dispatcher;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.tbittestlib.bluetooth.IBleClient;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.debug.BleLog;
import com.example.tbittestlib.bluetooth.listener.ChangeCharacterListener;
import com.example.tbittestlib.bluetooth.util.ByteUtil;
import com.example.tbittestlib.protocol.AckSender;
import com.example.tbittestlib.protocol.Packet;
import com.example.tbittestlib.protocol.ProtocolInfo;
import com.example.tbittestlib.protocol.utils.CrcUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public class ReceivedPacketDispatcher implements ChangeCharacterListener, Handler.Callback {
    private static final String TAG = "ReceivedPacketDispatche";

    private static final int HEAD_LENGTH = 8;
    private static final Byte HEAD_FLAG = new Byte((byte) 0xAA);
    private static final int PACKET_LENGTH_INDEX = 5;
    private static final int CRC_START_FLAG = 6;
    private static final int CRC_END_FLAG = 8;

    private IBleClient bleClient;
    private List<Byte> receivedData = Collections.synchronizedList(new LinkedList<Byte>());
    private List<PacketResponseListener> packetResponseList = new LinkedList<>();
    private Handler handler;

    private UUID serviceUuid;
    private UUID txUuid;
    private AckSender ackSender;

    public ReceivedPacketDispatcher(IBleClient bleClient, RequestDispatcher requestDispatcher) {
        this.bleClient = bleClient;
        this.ackSender = new AckSender(requestDispatcher);
        this.handler = new Handler(Looper.myLooper(), this);
        bleClient.getListenerManager().addChangeCharacterListener(this);
    }

    protected final List<PacketResponseListener> getPacketResponseList() {
        return packetResponseList;
    }

    public void setRxUuid(UUID rxUuid) {
        this.ackSender.setRxUuid(rxUuid);
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
        this.ackSender.setServiceUuid(serviceUuid);
    }

    protected final AckSender getAckSender() {
        return ackSender;
    }

    public void setTxUuid(UUID txUuid) {
        this.txUuid = txUuid;
    }

    public void addPacketResponseListener(PacketResponseListener packetResponseListener) {
        packetResponseList.add(0, packetResponseListener);
    }

    public void removePacketResponseListener(PacketResponseListener packetResponseListener) {
        packetResponseList.remove(packetResponseListener);
    }

    public void destroy() {
        packetResponseList.clear();
        bleClient.getListenerManager().removeChangeCharacterListener(this);
    }

    private void tryResolve() {
        //0xAA才是数据包的头
        if (!receivedData.get(0).equals(HEAD_FLAG)) {
            Iterator<Byte> iterator = receivedData.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().equals(HEAD_FLAG)) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        }
        // 等待头长度足够
        if (receivedData.size() < HEAD_LENGTH)
            return;

        // 数据包长度
        int dataPacketLen = receivedData.get(PACKET_LENGTH_INDEX) & 0xFF;

        // 数据包长度不足
        if (receivedData.size() - HEAD_LENGTH < dataPacketLen)
            return;

        // 数据包长度足够，取出数据包
        int totalLength = HEAD_LENGTH + dataPacketLen;
        byte[] data = new byte[totalLength];
        for (int i = 0; i < totalLength; i++) {
            data[i] = receivedData.remove(0);
        }

        // 校验crc
        byte[] crc = Arrays.copyOfRange(data, CRC_START_FLAG, CRC_END_FLAG);
        byte[] value = Arrays.copyOfRange(data, HEAD_LENGTH, data.length);
        if (!checkCrc(crc, value)) {
            BleLog.log("ReceivedDispatcherCheckCrc", "CheckCrc Failed");
            return;
        }

        // 发布数据
        publishData(data);
    }

    private boolean checkCrc(byte[] crc, byte[] value) {
        try {
            short i = CrcUtil.crcTable(ProtocolInfo.packetCrcTable, value);
            byte[] calculatedCrc = ByteUtil.shortToByte(i);
            return Arrays.equals(crc, calculatedCrc);
        } catch (Exception e) {
            BleLog.log("ReceivedDispatcherCheckCrc", "CheckCrc Exception: " + e.getMessage());
            return false;
        }
    }

    protected void publishData(byte[] data) {
        BleLog.log("ReceivedDispatcherPublish", ByteUtil.bytesToHexString(data));
        Packet packet = new Packet(data);

        if (!packet.getHeader().isAck()) {

            // 0x09是板间命令，不做应答和解析
            if (packet.getPacketValue().getCommandId() == 0x09) {
                BleLog.log("ReceivedDispatcherPublish", "drop broad command");
                return;
            }

            ackSender.sendACK(packet.getHeader().getSequenceId(), false);
        }

        for (PacketResponseListener listener : packetResponseList) {
            if (listener.onPacketReceived(packet))
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCharacterChange(BluetoothGattCharacteristic characteristic, final byte[] value) {
        if (!serviceUuid.equals(characteristic.getService().getUuid()))
            return;
        if (!txUuid.equals(characteristic.getUuid()))
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Byte[] data = ByteUtil.byteArrayToBoxed(value);
                receivedData.addAll(Arrays.asList(data));
                tryResolve();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return true;
    }
}
