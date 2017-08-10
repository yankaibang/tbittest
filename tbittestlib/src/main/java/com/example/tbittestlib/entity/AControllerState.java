package com.example.tbittestlib.entity;


import com.example.tbittestlib.bluetooth.util.ByteUtil;

import java.util.Arrays;

import static com.example.tbittestlib.bike.util.StateUpdateHelper.bitResolver;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.byteArrayToInt;


/**
 * Created by Salmon on 2017/4/27 0027.
 */

public class AControllerState {
    // 总里程，单位是 KM
    private int totalMillage;
    // 单次里程，单位是 0.1KM
    private int singleMillage;
    // 速度，单位是 0.1KM/H
    private int speed;
    // 电压，单位是 0.1V
    private int voltage;
    // 电流，单位是 MA
    private int electricCurrent;
    // 电量，单位是 MAH
    private int battery;
    // 故障码
    // BIT7 电机缺相      0: 电机不故障，1: 电机缺相故障
    // BIT6 霍尔故障状态  0: 霍尔无故障，1: 霍尔故障
    // BIT5 转把故障状态  0: 转把无故障，1: 转把故障
    // BIT4 MOS 故障状态   0：MOS 无故障，1：MOS 故障
    // BIT3 欠压状态      0: 不在欠压保护，1: 正在欠压保护
    // BIT2 巡航状态      0: 巡航无效，1: 巡航有效
    // BIT1 刹车状态      0：刹车无效，1：刹车有效
    // BIT0 WALK 状态      0：WALK 无效，1：WALK 有效
    private int[] errCode = new int[]{0,0,0,0,0,0,0,0};

    public int getTotalMillage() {
        return totalMillage;
    }

    public void setTotalMillage(int totalMillage) {
        this.totalMillage = totalMillage;
    }

    public int getSingleMillage() {
        return singleMillage;
    }

    public void setSingleMillage(int singleMillage) {
        this.singleMillage = singleMillage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getElectricCurrent() {
        return electricCurrent;
    }

    public void setElectricCurrent(int electricCurrent) {
        this.electricCurrent = electricCurrent;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int[] getErrCode() {
        return errCode;
    }

    public void setErrCode(int[] errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "AControllerState{" +
                "totalMillage=" + totalMillage +
                ", singleMillage=" + singleMillage +
                ", speed=" + speed +
                ", voltage=" + voltage +
                ", electricCurrent=" + electricCurrent +
                ", battery=" + battery +
                ", errCode=" + Arrays.toString(errCode) +
                '}';
    }

    public static AControllerState resolve(Byte[] data) {
        AControllerState controllerState = new AControllerState();
        if (data == null || data.length != 13)
            return controllerState;

        byte[] originData = ByteUtil.byteArrayToUnBoxed(data);

        controllerState.setTotalMillage(byteArrayToInt(Arrays.copyOfRange(originData, 0, 2)));

        controllerState.setSingleMillage(byteArrayToInt(Arrays.copyOfRange(originData, 2, 4)));

        controllerState.setSpeed(byteArrayToInt(Arrays.copyOfRange(originData, 4, 6)));

        Byte originError = data[6];
        int[] error = controllerState.getErrCode();
        error[0] = bitResolver(originError, 0x01);
        error[1] = bitResolver(originError, 0x02);
        error[2] = bitResolver(originError, 0x04);
        error[3] = bitResolver(originError, 0x08);
        error[4] = bitResolver(originError, 0x10);
        error[5] = bitResolver(originError, 0x20);
        error[6] = bitResolver(originError, 0x40);
        error[7] = bitResolver(originError, 0x80);

        controllerState.setVoltage(byteArrayToInt(Arrays.copyOfRange(originData, 7, 9)));

        controllerState.setElectricCurrent(byteArrayToInt(Arrays.copyOfRange(originData, 9, 11)));

        controllerState.setBattery(byteArrayToInt(Arrays.copyOfRange(originData, 11, 13)));

        return controllerState;
    }
}
