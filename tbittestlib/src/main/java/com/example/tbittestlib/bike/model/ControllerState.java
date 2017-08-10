package com.example.tbittestlib.bike.model;


import com.example.tbittestlib.bluetooth.util.ByteUtil;

import java.util.Arrays;

/**
 * Created by Salmon on 2017/2/10 0010.
 */

public class ControllerState {

    private Byte[] rawData = new Byte[]{};
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
    private int[] errCode = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

    public Byte[] getRawData() {
        return rawData;
    }

    public void setRawData(Byte[] rawData) {
        this.rawData = rawData;
    }

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
        return "ControllerState{" +
                "rawData=" + ByteUtil.bytesToHexString(rawData) +
                ", totalMillage=" + totalMillage +
                ", singleMillage=" + singleMillage +
                ", speed=" + speed +
                ", voltage=" + voltage +
                ", electricCurrent=" + electricCurrent +
                ", battery=" + battery +
                ", errCode=" + Arrays.toString(errCode) +
                '}';
    }
}
