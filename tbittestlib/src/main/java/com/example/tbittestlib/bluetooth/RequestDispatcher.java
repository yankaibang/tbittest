package com.example.tbittestlib.bluetooth;


import com.example.tbittestlib.bluetooth.request.BleRequest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Salmon on 2017/3/23 0023.
 */

public class RequestDispatcher implements IRequestDispatcher {

    private IBleClient bleClient;
    private List<BleRequest> bleRequests;
    private BleRequest curRequest;

    public RequestDispatcher(IBleClient bleClient) {
        this.bleClient = bleClient;
        this.bleRequests = new LinkedList<>();
    }

    public void addRequest(BleRequest bleRequest) {
        bleRequest.setBleClient(bleClient);
        bleRequest.setRequestDispatcher(this);
        bleRequests.add(bleRequest);
        notifyNextRequest();
    }

    private void notifyNextRequest() {
        if (bleRequests.size() == 0)
            return;
        if (curRequest != null && !curRequest.isFinished())
            return;
        curRequest = bleRequests.remove(0);
        curRequest.process();
    }

    @Override
    public void onRequestFinished(BleRequest request) {
        notifyNextRequest();
    }
}
