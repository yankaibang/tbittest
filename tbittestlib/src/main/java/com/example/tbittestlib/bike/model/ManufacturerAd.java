package com.example.tbittestlib.bike.model;

import android.text.TextUtils;


import com.example.tbittestlib.bluetooth.util.ByteUtil;

import java.util.Arrays;

/**
 * Created by Salmon on 2017/3/9 0009.
 */

public class ManufacturerAd {
    private byte[] manuId = new byte[1];
    private byte[] maskId = new byte[1];
    private String machineId = "";
    private byte[] reverse = new byte[1];
    private int hardwareVersion;
    private int softwareVersion;
    private int type;

    public byte[] getManuId() {
        return manuId;
    }

    public void setManuId(byte[] manuId) {
        this.manuId = manuId;
    }

    public byte[] getMaskId() {
        return maskId;
    }

    public void setMaskId(byte[] maskId) {
        this.maskId = maskId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public byte[] getReverse() {
        return reverse;
    }

    public void setReverse(byte[] reverse) {
        this.reverse = reverse;
    }

    public int getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(int hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public int getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(int softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ManufacturerAd{" +
                "manuId=" + Arrays.toString(manuId) +
                ", maskId=" + Arrays.toString(maskId) +
                ", machineId='" + machineId + '\'' +
                ", reverse=" + Arrays.toString(reverse) +
                ", hardwareVersion=" + hardwareVersion +
                ", softwareVersion=" + softwareVersion +
                ", type=" + type +
                '}';
    }

    public static ManufacturerAd resolveManufacturerAd(byte[] data) {
        ManufacturerAd manufacturerAd = new ManufacturerAd();
        if (data == null || data.length != 12)
            return manufacturerAd;
        manufacturerAd.setManuId(new byte[]{data[0]});
        manufacturerAd.setMaskId(new byte[]{data[1]});
        String machineId = ByteUtil.bytesToHexStringWithoutSpace(Arrays.copyOfRange(data, 2, 8));
        // 由于终端未标识设备编号长度，此处只截取9位数，后期会在maskId中作标识
        if (!TextUtils.isEmpty(machineId) && machineId.length() >= 9)
            machineId = machineId.substring(0, 9);
        manufacturerAd.setMachineId(machineId);
        manufacturerAd.setReverse(new byte[]{data[8]});
        manufacturerAd.setHardwareVersion(data[9]);
        manufacturerAd.setSoftwareVersion(data[10]);
        manufacturerAd.setType(data[11]);
        return manufacturerAd;
    }
}
