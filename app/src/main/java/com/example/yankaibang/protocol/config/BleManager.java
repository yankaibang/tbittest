package com.example.yankaibang.protocol.config;

import com.example.yankaibang.bluetooth.BleClient;
import com.example.yankaibang.bluetooth.RequestDispatcher;
import com.example.yankaibang.protocol.dispatcher.ReceivedPacketDispatcher;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class BleManager {

    private final BleClient mBleClient;
    private final RequestDispatcher mRequestDispatcher;
    private final ReceivedPacketDispatcher mReceivedPacketDispatcher;

    private BleManager(){
        mBleClient = new BleClient();
        mRequestDispatcher = new RequestDispatcher(mBleClient);
        mReceivedPacketDispatcher = new ReceivedPacketDispatcher(mBleClient, mRequestDispatcher);
    }

    private static class Holder {
        public static final BleManager instance = new BleManager();
    }

    public static BleManager getInstance(){
        return Holder.instance;
    }

    public BleClient getBleClient() {
        return mBleClient;
    }

    public RequestDispatcher getRequestDispatcher() {
        return mRequestDispatcher;
    }

    public ReceivedPacketDispatcher getReceivedPacketDispatcher() {
        return mReceivedPacketDispatcher;
    }
}
