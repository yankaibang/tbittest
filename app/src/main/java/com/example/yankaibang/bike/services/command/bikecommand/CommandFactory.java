package com.example.yankaibang.bike.services.command.bikecommand;

import android.util.Log;
import android.util.Pair;

import com.example.yankaibang.bike.model.BikeState;
import com.example.yankaibang.bike.services.command.callback.StateCallback;
import com.example.yankaibang.bike.services.config.BikeConfig;
import com.example.yankaibang.bike.util.StateUpdateHelper;
import com.example.yankaibang.protocol.Packet;
import com.example.yankaibang.protocol.PacketValue;
import com.example.yankaibang.protocol.ResultCode;
import com.example.yankaibang.protocol.callback.AckCallback;
import com.example.yankaibang.protocol.callback.CommondPacketCallback;
import com.example.yankaibang.protocol.callback.ResultCallback;
import com.example.yankaibang.protocol.command.Command;
import com.example.yankaibang.protocol.command.CommandComparator;
import com.example.yankaibang.protocol.command.WriteCommand;
import com.example.yankaibang.protocol.request.WriteRequestBuilder;
import com.example.yankaibang.protocol.utils.PacketUtil;

import java.util.List;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class CommandFactory {
    public static Command newLockCommand(ResultCallback resultCallback) {
        return new WriteRequestBuilder()
                .setRetryTimes(3)
                .setTimeout(15000)
                .setCommondId((byte) 0x03)
                .addData((byte) 0x01, new Byte[]{0x01})
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return PacketUtil.compareCommandId(receivedPacket, sendPacket)
                                && PacketUtil.checkPacketValueContainKey(receivedPacket, 0x81);
                    }
                })
                .setCommondPacketCallback(new CommondPacketCallback() {
                    @Override
                    public void onPacketReceived(WriteCommand command, Packet packet) {
                        PacketValue packetValue = packet.getPacketValue();
                        List<PacketValue.DataBean> resolvedData = packetValue.getData();

                        // 0x00：成功  0x01：指令非法 0x02：运动状态 0x03：非绑定状态
                        for (PacketValue.DataBean b : resolvedData) {
                            int key = b.key & 0xff;
                            Byte[] value = b.value;
                            if (key == 0x81) {
                                if (value[0] != 0) {
                                    command.retry();
                                    break;
                                }
                                int resultCode = ResultCode.FAILED;
                                switch (value[0]) {
                                    case 0x00:
                                        resultCode = ResultCode.SUCCEED;
                                        break;
                                    case 0x01:
                                        resultCode = ResultCode.ILLEGAL_COMMAND;
                                        break;
                                    case 0x02:
                                        resultCode = ResultCode.MOTION_STATE;
                                        break;
                                    case 0x03:
                                        resultCode = ResultCode.NOT_BINDING;
                                        break;
                                }
                                command.response(resultCode);
                                break;
                            }
                        }
                    }
                })
                .build();
    }

    public static Command newUnlockCommand(ResultCallback resultCallback) {
        return new WriteRequestBuilder()
                .setRetryTimes(3)
                .setTimeout(15000)
                .setCommondId((byte) 0x03)
                .addData((byte) 0x02, new Byte[]{0x00})
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return PacketUtil.compareCommandId(receivedPacket, sendPacket)
                                && PacketUtil.checkPacketValueContainKey(receivedPacket, 0x82);
                    }
                })
                .setCommondPacketCallback(new CommondPacketCallback() {
                    @Override
                    public void onPacketReceived(WriteCommand command, Packet packet) {
                        PacketValue packetValue = packet.getPacketValue();
                        List<PacketValue.DataBean> resolvedData = packetValue.getData();
                        // 0x00：成功  0x01：指令非法 0x02：运动状态 0x03：非绑定状态
                        int resultCode = ResultCode.FAILED;
                        for (PacketValue.DataBean b : resolvedData) {
                            int key = b.key & 0xff;
                            Byte[] value = b.value;
                            if (key == 0x82) {
                                switch (value[0]) {
                                    case 0x00:
                                        resultCode = ResultCode.SUCCEED;
                                        break;
                                    case 0x01:
                                        resultCode = ResultCode.ILLEGAL_COMMAND;
                                        break;
                                    case 0x02:
                                        resultCode = ResultCode.MOTION_STATE;
                                        break;
                                    case 0x03:
                                        resultCode = ResultCode.NOT_BINDING;
                                        break;
                                }
                                break;
                            }
                        }

                        command.response(resultCode);
                    }
                })
                .build();
    }

    public Command newUpdateCommand(ResultCallback resultCallback, final BikeConfig bikeConfig, final StateCallback stateCallback, final BikeState bikeState) {
        return new WriteRequestBuilder()
                .setCommondId((byte) 0x04)
                .addData((byte) 0x05, null)
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return PacketUtil.compareCommandId(receivedPacket, sendPacket)
                                && PacketUtil.checkPacketValueContainKey(receivedPacket, 0x85);
                    }
                })
                .setCommondPacketCallback(new CommondPacketCallback() {
                    @Override
                    public void onPacketReceived(WriteCommand command, Packet packet) {
                        PacketValue packetValue = packet.getPacketValue();
                        List<PacketValue.DataBean> resolvedData = packetValue.getData();
                        for (PacketValue.DataBean b : resolvedData) {
                            int key = b.key & 0xff;
                            Byte[] value = b.value;
                            if (key == 0x85) {
                                bikeConfig.getResolver().resolveAll(bikeState, value);
                                if (stateCallback != null)
                                    stateCallback.onStateUpdated(bikeState);
                                command.response(ResultCode.SUCCEED);
                                break;
                            }
                        }
                    }
                })
                .build();
    }

    public Command newConnectCommand(Byte[] key, ResultCallback resultCallback, final BikeConfig bikeConfig, final StateCallback stateCallback, final BikeState bikeState) {
        return new WriteRequestBuilder()
                .setCommondId((byte) 0x02)
                .addData((byte) 0x01, key)
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return 0x05 == receivedPacket.getPacketValue().getCommandId()
                                && PacketUtil.checkPacketValueContainKey(receivedPacket, 0x02);
                    }
                })
                .setCommondPacketCallback(new CommondPacketCallback() {
                    @Override
                    public void onPacketReceived(WriteCommand command, Packet packet) {
                        Log.d("ConnectCommand", "onResult: " + packet.toString());
                        PacketValue packetValue = packet.getPacketValue();
                        List<PacketValue.DataBean> resolvedData = packetValue.getData();

                        // 0x00：成功  0x01：指令非法 0x02：运动状态 0x03：非绑定状态
                        int resultCode = ResultCode.FAILED;
                        for (PacketValue.DataBean b : resolvedData) {
                            int key = b.key & 0xff;
                            Byte[] value = b.value;
                            switch (key) {
                                case 0x02:
                                    //用户连接返回
                                    if (value[0] == (byte) 0x01) {
                                        resultCode = ResultCode.SUCCEED;
                                    }
                                    break;
                                case 0x81:
                                    StateUpdateHelper.updateVoltage(bikeState, value);
                                    break;
                                case 0x82: {
                                    switch (value[0]) {
                                        case 0x00:
                                            resultCode = ResultCode.CONNECT_FAILED_UNKNOWN;
                                            break;
                                        case 0x01:
                                            resultCode = ResultCode.CONNECT_FAILED_ILLEGAL_KEY;
                                            break;
                                        case 0x02:
                                            resultCode = ResultCode.CONNECT_DATA_VERIFICATION_FAILED;
                                            break;
                                        case 0x03:
                                            resultCode = ResultCode.CONNECT_COMMAND_NOT_SUPPORT;
                                            break;
                                        default:
                                            resultCode = ResultCode.FAILED;
                                    }
                                    break;
                                }
                                case 0x83:
                                    StateUpdateHelper.updateDeviceFault(bikeState, value);
                                    break;
                                case 0x84:
                                    bikeConfig.getResolver().resolveLocations(bikeState, value);
                                    break;
                                case 0x85:
                                    StateUpdateHelper.updateBaseStation(bikeState, value);
                                    break;
                                case 0x86:
                                    StateUpdateHelper.updateSignal(bikeState, value);
                                    break;
                                case 0x88:
                                    bikeConfig.getResolver().resolveControllerState(bikeState, value);
                                    break;
                                case 0xff:
                                    break;
                            }
                        }

                        if (resultCode == ResultCode.SUCCEED) {
                            if (stateCallback != null)
                                stateCallback.onStateUpdated(bikeState);
                        }
                        command.response(resultCode);
                    }
                })
                .build();
    }

    public static Command newCommonCommand(byte commandId, ResultCallback resultCallback, Pair<Byte, Byte[]> datas) {
        return new WriteRequestBuilder()
                .setCommondId(commandId)
                .addDatas(datas)
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return PacketUtil.compareCommandId(receivedPacket, sendPacket);
                    }
                })
                .setCommondPacketCallback(new CommondPacketCallback() {
                    @Override
                    public void onPacketReceived(WriteCommand command, Packet packet) {
                        try {
                            PacketValue packetValue = packet.getPacketValue();
                            List<PacketValue.DataBean> resolvedData = packetValue.getData();

                            Byte[] value = resolvedData.get(0).value;

                            switch (value[0]) {
                                case 0x00:
                                    command.response(ResultCode.SUCCEED);
                                    break;
                                case 0x01:
                                    command.response(ResultCode.ILLEGAL_COMMAND);
                                    break;
                                case 0x02:
                                    command.response(ResultCode.MOTION_STATE);
                                    break;
                                case 0x03:
                                    command.response(ResultCode.NOT_BINDING);
                                    break;
                                default:
                                    command.response(ResultCode.FAILED);
                            }
                        } catch (Exception e) {
                            command.response(ResultCode.FAILED);
                            e.printStackTrace();
                        }
                    }
                })
                .build();
    }

    public static Command newAckOnlyCommand(byte commandId, ResultCallback resultCallback, Pair<Byte, Byte[]> datas) {
        return new WriteRequestBuilder()
                .setCommondId(commandId)
                .addDatas(datas)
                .setResultCallback(resultCallback)
                .setPacketComparator(new CommandComparator() {
                    @Override
                    public boolean compare(Packet sendPacket, Packet receivedPacket) {
                        return PacketUtil.compareCommandId(receivedPacket, sendPacket);
                    }
                })
                .setAckCallback(new AckCallback() {
                    @Override
                    public void onAckSuccess(WriteCommand command) {
                        command.response(ResultCode.SUCCEED);
                    }
                })
                .build();
    }
}
