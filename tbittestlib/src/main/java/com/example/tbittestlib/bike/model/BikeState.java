package com.example.tbittestlib.bike.model;

import java.util.Arrays;

/**
 * Created by Salmon on 2016/12/5 0005.
 */

public class BikeState {
    // 电量
    private float battery;
    // 位置,location[0]是经度，location[1]是纬度
    private double[] location = new double[]{0, 0};
    // 信号量，数组的三位分别为如下
    // GSM：用于标识 GSM 信号强度，范围 0~9
    // GPS：用于标识 GPS 信号强度，范围 0~9
    // BAT：用于标识定位器后备电池电量，范围 0~9
    private int[] signal = new int[]{0, 0, 0};
    // 校验失败原因
    // 0：未知原因 1：连接密钥非法 2：数据校验失败 3：指令不支持
    private int verifyFailedCode;
    // 车辆故障 电机类故障 中控类故障 通信类故障 其它故障 详见常用数据类型说明
    private int deviceFaultCode;
    // 数组每一位的解析如下
    // 0 | 0：车辆撤防模式开启 1：车辆设防模式开启
    // 1 | 0：车辆处于静止状态 1：车辆处于运动状态
    // 2 | 0：车辆锁电机为关闭状态 1：车辆锁电机为打开状态
    // 3 | 0：车辆 ACC 为关闭状态 1：车辆 ACC 为打开状态
    // 4 | 0：车辆不处于休眠模式 1：车辆处于休眠模式
    // 5 | 0：车辆蓝牙处于非连接状态 1：车辆蓝牙处于连接状态
    // 6~7 | 11：断电告警  10：震动告警  01：低电告警  00：无告警信息
    private int[] systemState = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    // 0x00：成功  0x01：指令非法 0x02：运动状态 0x03：非绑定状态
    private int operateFaultCode;
    // 0:MCC 1:MNC 2:LAC 3.Cell ID
    private int[] baseStation = new int[]{0, 0, 0, 0};
    // 版本号 0：硬件版本 1：软件版本
    private int[] version = new int[]{0, 0};
    // 控制器信息
    private ControllerState controllerState = new ControllerState();
    // 原始信息
    private Byte[] rawData = new Byte[]{};
    // 定位方式
    private int gpsState = 0;

    public int getGpsState() {
        return gpsState;
    }

    public void setGpsState(int gpsState) {
        this.gpsState = gpsState;
    }

    public Byte[] getRawData() {
        return rawData;
    }

    public void setRawData(Byte[] rawData) {
        this.rawData = rawData;
    }

    public ControllerState getControllerState() {
        return controllerState;
    }

    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
    }

    public int[] getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(int[] baseStation) {
        this.baseStation = baseStation;
    }

    public int[] getVersion() {
        return version;
    }

    public void setVersion(int[] version) {
        this.version = version;
    }

    public int[] getSystemState() {
        return systemState;
    }

    public void setSystemState(int[] systemState) {
        this.systemState = systemState;
    }

    public int getOperateFaultCode() {
        return operateFaultCode;
    }

    public void setOperateFaultCode(int operateFaultCode) {
        this.operateFaultCode = operateFaultCode;
    }

    public float getBattery() {
        return battery;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public int[] getSignal() {
        return signal;
    }

    public void setSignal(int[] signal) {
        this.signal = signal;
    }

    public int getVerifyFailedCode() {
        return verifyFailedCode;
    }

    public void setVerifyFailedCode(int verifyFailedCode) {
        this.verifyFailedCode = verifyFailedCode;
    }

    public int getDeviceFaultCode() {
        return deviceFaultCode;
    }

    public void setDeviceFaultCode(int deviceFaultCode) {
        this.deviceFaultCode = deviceFaultCode;
    }

    @Override
    public String toString() {
        return "BikeState{" +
                "battery=" + battery +
                ", location=" + Arrays.toString(location) +
                ", signal=" + Arrays.toString(signal) +
                ", verifyFailedCode=" + verifyFailedCode +
                ", deviceFaultCode=" + deviceFaultCode +
                ", systemState=" + Arrays.toString(systemState) +
                ", operateFaultCode=" + operateFaultCode +
                ", baseStation=" + Arrays.toString(baseStation) +
                ", version=" + Arrays.toString(version) +
                ", gpsState=" + gpsState +
                '}';
    }
}
