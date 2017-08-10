package com.example.yankaibang.entity;

import java.util.Arrays;

/**
 * Created by Salmon on 2017/5/4 0004.
 */

public class W207State {

    // 纬度坐标，度值
    private double latitudeDegree;

    // 纬度坐标，分值
    private double latitudeMinute;

    // 经度坐标，度值
    private double longitudeDegree;

    // 经度坐标，分值
    private double longitudeMinute;

    // GPS 卫星数
    private int satellite;

    // 行驶形式总里程
    private int totalMileage;

    // 电池剩余电量
    private int battery;

    // 电池充电总循环数
    private int chargeCount;

    // 充电状态
    private boolean charging;

    // 故障码
    private int[] errorCode = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

    //控制器工作状态
    private int ctrlState;

    //脚撑
    private boolean sustained;

    // 定位方式
    private int gpsState = 0;

    public int getGpsState() {
        return gpsState;
    }

    public void setGpsState(int gpsState) {
        this.gpsState = gpsState;
    }

    public boolean isSustained() {
        return sustained;
    }

    public void setSustained(boolean sustained) {
        this.sustained = sustained;
    }

    public double getLatitudeDegree() {
        return latitudeDegree;
    }

    public void setLatitudeDegree(double latitudeDegree) {
        this.latitudeDegree = latitudeDegree;
    }

    public double getLatitudeMinute() {
        return latitudeMinute;
    }

    public void setLatitudeMinute(double latitudeMinute) {
        this.latitudeMinute = latitudeMinute;
    }

    public double getLongitudeDegree() {
        return longitudeDegree;
    }

    public void setLongitudeDegree(double longitudeDegree) {
        this.longitudeDegree = longitudeDegree;
    }

    public double getLongitudeMinute() {
        return longitudeMinute;
    }

    public void setLongitudeMinute(double longitudeMinute) {
        this.longitudeMinute = longitudeMinute;
    }

    public int getSatellite() {
        return satellite;
    }

    public void setSatellite(int satellite) {
        this.satellite = satellite;
    }

    public int getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(int totalMileage) {
        this.totalMileage = totalMileage;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(int chargeCount) {
        this.chargeCount = chargeCount;
    }

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public int[] getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int[] errorCode) {
        this.errorCode = errorCode;
    }

    public int getCtrlState() {
        return ctrlState;
    }

    public void setCtrlState(int ctrlState) {
        this.ctrlState = ctrlState;
    }

    @Override
    public String toString() {
        return "W207State{" +
                "latitudeDegree=" + latitudeDegree +
                ", latitudeMinute=" + latitudeMinute +
                ", longitudeDegree=" + longitudeDegree +
                ", longitudeMinute=" + longitudeMinute +
                ", satellite=" + satellite +
                ", totalMileage=" + totalMileage +
                ", battery=" + battery +
                ", chargeCount=" + chargeCount +
                ", charging=" + charging +
                ", errorCode=" + Arrays.toString(errorCode) +
                ", ctrlState=" + ctrlState +
                ", sustained=" + sustained +
                ", gpsState=" + gpsState +
                '}';
    }
}
