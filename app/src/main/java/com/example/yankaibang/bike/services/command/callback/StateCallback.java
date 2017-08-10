package com.example.yankaibang.bike.services.command.callback;


import com.example.yankaibang.bike.model.BikeState;

/**
 * Created by Salmon on 2017/3/17 0017.
 */

public interface StateCallback {

    void onStateUpdated(BikeState bikeState);

}
