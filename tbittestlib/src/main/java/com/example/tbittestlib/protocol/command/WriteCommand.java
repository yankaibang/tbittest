package com.example.tbittestlib.protocol.command;

import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.tbittestlib.bluetooth.BleClient;
import com.example.tbittestlib.bluetooth.Code;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.debug.BleLog;
import com.example.tbittestlib.bluetooth.listener.ConnectStateChangeListener;
import com.example.tbittestlib.bluetooth.request.BleRequest;
import com.example.tbittestlib.bluetooth.request.BleResponse;
import com.example.tbittestlib.bluetooth.request.WriterRequest;
import com.example.tbittestlib.protocol.Packet;
import com.example.tbittestlib.protocol.ResultCode;
import com.example.tbittestlib.protocol.callback.AckCallback;
import com.example.tbittestlib.protocol.callback.CommondPacketCallback;
import com.example.tbittestlib.protocol.callback.ResultCallback;
import com.example.tbittestlib.protocol.config.Uuid;
import com.example.tbittestlib.protocol.dispatcher.PacketResponseListener;
import com.example.tbittestlib.protocol.dispatcher.ReceivedPacketDispatcher;

/**
 * Created by yankaibang on 2017/8/7.
 */

public class WriteCommand extends Command implements Handler.Callback, BleResponse, PacketResponseListener, ConnectStateChangeListener {

    protected int state;
    private int mTimeout;
    private int mRetryCount;
    private int mRetryTimes;
    private CommandComparator mCommandComparator;
    private ReceivedPacketDispatcher mReceivedPacketDispatcher;
    protected RequestDispatcher mRequestDispatcher;
    private ResultCallback mResultCallback;
    private CommondPacketCallback mPacketCallback;
    private AckCallback mAckCallback;
    private Packet mPacket;
    private BleClient mBleClient;
    protected Handler mHandler;
    private Uuid mUuid;

    public WriteCommand(int timeout, int retryTimes, CommandComparator commandComparator, RequestDispatcher requestDispatcher, ReceivedPacketDispatcher receivedPacketDispatcher, ResultCallback resultCallback, CommondPacketCallback packetCallback, AckCallback ackCallback, BleClient bleClient, Uuid uuid, Packet packet) {
        mTimeout = timeout;
        mRetryTimes = retryTimes;
        mCommandComparator = commandComparator;
        mRequestDispatcher = requestDispatcher;
        mReceivedPacketDispatcher = receivedPacketDispatcher;
        mResultCallback = resultCallback;
        mPacketCallback = packetCallback;
        mAckCallback = ackCallback;
        mBleClient = bleClient;
        mUuid = uuid;
        mPacket = packet;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public int getState() {
        return state;
    }

    public void cancel() {
        if (isProcessable()) {
            response(ResultCode.CANCELED);
        } else {
            if (mResultCallback != null)
                mResultCallback.onResult(ResultCode.CANCELED);
            state = FINISHED;
            mResultCallback = null;
        }
    }

    @Override
    public boolean process() {
        if (state != NOT_EXECUTE_YET)
            return false;
        state = PROCESSING;

        mReceivedPacketDispatcher.addPacketResponseListener(this);
        mBleClient.getListenerManager().addConnectStateChangeListener(this);
        if (mBleClient.getConnectionState() < 3) {
            response(ResultCode.DISCONNECTED);
            return true;
        }

        sendCommand();
        startTiming();
        return true;
    }

    protected void sendCommand() {
        BleRequest writeRequest = new WriterRequest(mUuid.SPS_SERVICE_UUID,
                mUuid.SPS_RX_UUID, mPacket.toByteArray(), false, this);
        writeRequest.enqueue(mRequestDispatcher);
        startAckTiming();
    }

    public void response(int resultCode) {
        if (!isProcessable())
            return;
        if (mResultCallback != null)
            mResultCallback.onResult(resultCode);
        onFinish();
    }

    protected boolean isProcessable() {
        return state == PROCESSING;
    }

    protected void onFinish() {
        state = FINISHED;
        mHandler.removeCallbacksAndMessages(null);
        mReceivedPacketDispatcher.removePacketResponseListener(this);
        mBleClient.getListenerManager().removeConnectStateChangeListener(this);
        mResultCallback = null;
    }

    protected void startTiming() {
        BleLog.log("StartTiming", "Timeout: " + getTimeout());
        mHandler.sendEmptyMessageDelayed(HANDLE_TIMEOUT, getTimeout());
    }

    protected void stopTiming() {
        BleLog.log("StopTiming", "StopTiming");
        mHandler.removeMessages(HANDLE_TIMEOUT);
    }

    protected void startAckTiming() {
        BleLog.log("StartAckTiming", "Timeout: 3000");
        mHandler.sendEmptyMessageDelayed(HANDLE_ACK_TIMEOUT, DEFAULT_ACK_TIMEOUT);
    }

    protected void stopAckTiming() {
        BleLog.log("StopAckTiming", "StopAckTiming");
        mHandler.removeMessages(HANDLE_ACK_TIMEOUT);
    }

    private int getTimeout() {
        return mTimeout;
    }

    private void timeout() {
        if (!isProcessable())
            return;
        onTimeout();
    }

    protected void onTimeout() {
        response(ResultCode.TIMEOUT);
    }

    public void retry() {
        BleLog.log("Command", "retry-" + isProcessable() + "-" + mRetryCount);
        if (!isProcessable())
            return;
        if (mRetryCount < getRetryTimes()) {
            sendCommand();
            mRetryCount++;
        } else {
            response(ResultCode.FAILED);
        }
    }

    private int getRetryTimes() {
        return mRetryTimes;
    }

    @Override
    public void onResponse(int resultCode) {
        if (resultCode != Code.REQUEST_SUCCESS && mRetryCount < getRetryTimes()) {
            retry();
            return;
        }
        switch (resultCode) {
            case Code.BLE_DISABLED:
                response(ResultCode.BLE_NOT_OPENED);
                break;
            case Code.REQUEST_FAILED:
                response(ResultCode.FAILED);
                break;
            case Code.REQUEST_TIMEOUT:
                response(ResultCode.TIMEOUT);
                break;
            case Code.REQUEST_SUCCESS:

                break;
            default:
                response(ResultCode.FAILED);
                break;
        }
    }

    @Override
    public void onConnectionStateChange(int status, int newState) {
        if (!isProcessable())
            return;
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    response(ResultCode.DISCONNECTED);
                }
            });
        }
    }

    @Override
    public boolean onPacketReceived(Packet packet) {
        if (packet.getHeader().isAck()) {
            boolean isMyAck = packet.getHeader().getSequenceId() == mPacket.getHeader().getSequenceId();
            if (isMyAck)
                onAck(packet);
            return isMyAck;
        }
        if (mCommandComparator.compare(mPacket, packet)) {
            onResult(packet);
            return true;
        }
        return false;
    }

    private void onResult(Packet packet) {
        if (mPacketCallback != null) {
            mPacketCallback.onPacketReceived(this, packet);
        } else {
            response(ResultCode.FAILED);
        }
    }

    private void onAck(Packet packet) {
        stopAckTiming();
        if (packet.getHeader().isError())
            onAckFailed();
        else
            onAckSuccess();
    }

    private void onAckSuccess() {
        if (mAckCallback != null) {
            mAckCallback.onAckSuccess(this);
        }
    }

    private void onAckFailed() {
        retry();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (!isProcessable())
            return true;
        switch (msg.what) {
            case HANDLE_TIMEOUT:
                timeout();
                break;
            case HANDLE_ACK_TIMEOUT:
                retry();
                break;
        }
        return true;
    }
}
