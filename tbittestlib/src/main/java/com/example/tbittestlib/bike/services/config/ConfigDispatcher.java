package com.example.tbittestlib.bike.services.config;


import com.example.tbittestlib.bike.model.ManufacturerAd;

/**
 * author: Salmon
 * date: 2017-06-27 09:57
 * github: https://github.com/billy96322
 * email: salmonzhg@foxmail.com
 */

public interface ConfigDispatcher {

    BikeConfig dispatch(ManufacturerAd manufacturerAd);
}
