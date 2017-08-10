package com.example.yankaibang.bluetooth;


import com.example.yankaibang.bluetooth.listener.ChangeCharacterListener;
import com.example.yankaibang.bluetooth.listener.ConnectStateChangeListener;
import com.example.yankaibang.bluetooth.listener.ReadCharacterListener;
import com.example.yankaibang.bluetooth.listener.ReadDescriptorListener;
import com.example.yankaibang.bluetooth.listener.ReadRssiListener;
import com.example.yankaibang.bluetooth.listener.ServiceDiscoverListener;
import com.example.yankaibang.bluetooth.listener.WriteCharacterListener;
import com.example.yankaibang.bluetooth.listener.WriteDescriptorListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Salmon on 2017/3/22 0022.
 */

public class ListenerManager {

    public List<ConnectStateChangeListener> connectStateChangeListeners = new LinkedList<>();
    public List<ChangeCharacterListener> changeCharacterListeners = new LinkedList<>();
    public List<WriteCharacterListener> writeCharacterListeners = new LinkedList<>();
    public List<WriteDescriptorListener> writeDescriptorListeners = new LinkedList<>();
    public List<ReadCharacterListener> readCharacterListeners = new LinkedList<>();
    public List<ReadDescriptorListener> readDescriptorListeners = new LinkedList<>();
    public List<ServiceDiscoverListener> serviceDiscoverListeners = new LinkedList<>();
    public List<ReadRssiListener> readRssiListeners = new LinkedList<>();

    public void addReadRssiListener(ReadRssiListener readRssiListener) {
        readRssiListeners.add(readRssiListener);
    }

    public void removeReadRssiListener(ReadRssiListener readRssiListener) {
        readRssiListeners.remove(readRssiListener);
    }

    public void addServiceDiscoverListener(ServiceDiscoverListener serviceDiscoverListener) {
        serviceDiscoverListeners.add(serviceDiscoverListener);
    }

    public void removeServiceDiscoverListener(ServiceDiscoverListener serviceDiscoverListener) {
        serviceDiscoverListeners.remove(serviceDiscoverListener);
    }

    public void addChangeCharacterListener(ChangeCharacterListener changeCharacterListener) {
        changeCharacterListeners.add(changeCharacterListener);
    }

    public void addConnectStateChangeListener(ConnectStateChangeListener connectStateChangeListener) {
        connectStateChangeListeners.add(connectStateChangeListener);
    }

    public void removeChangeCharacterListener(ChangeCharacterListener changeCharacterListener) {
        changeCharacterListeners.remove(changeCharacterListener);
    }

    public void removeConnectStateChangeListener(ConnectStateChangeListener connectStateChangeListener) {
        connectStateChangeListeners.remove(connectStateChangeListener);
    }

    public void addWriteCharacterListener(WriteCharacterListener writeCharacterListener) {
        writeCharacterListeners.add(writeCharacterListener);
    }

    public void removeWriteCharacterListener(WriteCharacterListener writeCharacterListener) {
        writeCharacterListeners.remove(writeCharacterListener);
    }

    public void addReadCharacterListener(ReadCharacterListener readCharacterListener) {
        readCharacterListeners.add(readCharacterListener);
    }

    public void removeReadCharacterListener(ReadCharacterListener readCharacterListener) {
        readCharacterListeners.remove(readCharacterListener);
    }

    public void addReadDescriptorListener(ReadDescriptorListener readDescriptorListener) {
        readDescriptorListeners.add(readDescriptorListener);
    }

    public void removeReadDescriptorListener(ReadDescriptorListener readDescriptorListener) {
        readDescriptorListeners.remove(readDescriptorListener);
    }

    public void addWriteDescriptorListener(WriteDescriptorListener writeDescriptorListener) {
        writeDescriptorListeners.add(writeDescriptorListener);
    }

    public void removeWriteDescriptorListener(WriteDescriptorListener writeDescriptorListener) {
        writeDescriptorListeners.remove(writeDescriptorListener);
    }

    public void removeAll() {
        readRssiListeners.clear();
        serviceDiscoverListeners.clear();
        writeCharacterListeners.clear();
        writeDescriptorListeners.clear();
        readCharacterListeners.clear();
        readDescriptorListeners.clear();
        connectStateChangeListeners.clear();
        changeCharacterListeners.clear();
    }
}
