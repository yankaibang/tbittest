package com.example.yankaibang.protocol.request;

import android.util.Pair;

import com.example.yankaibang.bluetooth.BleClient;
import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.protocol.callback.AckCallback;
import com.example.yankaibang.protocol.callback.CommondPacketCallback;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.Packet;
import com.example.yankaibang.protocol.PacketValue;
import com.example.yankaibang.protocol.command.Command;
import com.example.yankaibang.protocol.command.CommandComparator;
import com.example.yankaibang.protocol.command.WriteCommand;
import com.example.yankaibang.protocol.config.BleManager;
import com.example.yankaibang.protocol.config.Uuid;
import com.example.yankaibang.protocol.dispatcher.ReceivedPacketDispatcher;
import com.example.yankaibang.protocol.utils.PacketUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yankaibang on 2017/8/7.
 */

public class WriteRequestBuilder {

    private static final int SEQUENCE_ID_START = 128;
    private AtomicInteger mSequenceId;
    private int mTimeout = Command.DEFAULT_COMMAND_TIMEOUT;
    private byte mCommondId;
    private int mRetryTimes;
    private boolean mIsAck;
    private boolean mErr;
    private List<PacketValue.DataBean> mDataBeanList = new ArrayList<>();
    private CommandComparator mPacketComparator;
    private RequestDispatcher mRequestDispatcher;
    private ReceivedPacketDispatcher mReceivedPacketDispatcher;
    private ResultCallback mResultCallback;
    private CommondPacketCallback mCommondPacketCallback;
    private AckCallback mAckCallback;
    private BleClient mBleClient;
    private Uuid mUuid;

    public WriteRequestBuilder() {
        mTimeout = Command.DEFAULT_COMMAND_TIMEOUT;
        mDataBeanList = new ArrayList<>();
        mBleClient = BleManager.getInstance().getBleClient();
        mRequestDispatcher = BleManager.getInstance().getRequestDispatcher();
        mReceivedPacketDispatcher = BleManager.getInstance().getReceivedPacketDispatcher();
        mSequenceId = new AtomicInteger(SEQUENCE_ID_START);
    }

    public WriteRequestBuilder setCommondId(byte commondId) {
        this.mCommondId = commondId;
        return this;
    }

    public WriteRequestBuilder setTimeout(int timeout) {
        mTimeout = timeout;
        return this;
    }

    public WriteRequestBuilder setRetryTimes(int retryTimes) {
        this.mRetryTimes = retryTimes;
        return this;
    }

    public WriteRequestBuilder setAck(boolean isAck) {
        this.mIsAck = isAck;
        return this;
    }

    public WriteRequestBuilder setError(boolean err) {
        this.mErr = err;
        return this;
    }

    public WriteRequestBuilder addData(byte key, Byte[] value) {
        mDataBeanList.add(new PacketValue.DataBean(key, value));
        return this;
    }

    public WriteRequestBuilder addDatas(Pair<Byte, Byte[]>... datas) {
        if (datas != null && datas.length != 0) {
            for (Pair<Byte, Byte[]> data : datas) {
                mDataBeanList.add(new PacketValue.DataBean(data.first, data.second));
            }
        }
        return this;
    }

    public WriteRequestBuilder setPacketComparator(CommandComparator commandComparator) {
        mPacketComparator = commandComparator;
        return this;
    }

    public WriteRequestBuilder setResultCallback(ResultCallback resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    public WriteRequestBuilder setCommondPacketCallback(CommondPacketCallback packetCallback) {
        mCommondPacketCallback = packetCallback;
        return this;
    }

    public WriteRequestBuilder setAckCallback(AckCallback ackCallback) {
        mAckCallback = ackCallback;
        return this;
    }

    public WriteRequestBuilder setUuid(Uuid uuid) {
        mUuid = uuid;
        return this;
    }

    public Command build() {
        Packet packet;
        if (mIsAck) {
            packet = PacketUtil.createAck(getSequenceId(), mErr);
        } else {
            packet = PacketUtil.createPacket(getSequenceId(), mCommondId, mDataBeanList.toArray(new PacketValue.DataBean[0]));
        }
        return new WriteCommand(mTimeout, mRetryTimes, mPacketComparator, mRequestDispatcher, mReceivedPacketDispatcher, mResultCallback, mCommondPacketCallback, mAckCallback, mBleClient, mUuid, packet);
    }

    private int getSequenceId() {
        int oldId;
        int newId;
        do {
            oldId = mSequenceId.get();
            newId = oldId >= 255 ? SEQUENCE_ID_START : ++oldId;
        } while (!mSequenceId.compareAndSet(oldId, newId));
        return oldId;
    }
}
