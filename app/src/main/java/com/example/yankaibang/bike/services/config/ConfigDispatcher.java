package com.example.yankaibang.bike.services.config;


import com.example.yankaibang.bike.model.ManufacturerAd;

/**
 * author: Salmon
 * date: 2017-06-27 09:57
 * github: https://github.com/billy96322
 * email: salmonzhg@foxmail.com
 */

public interface ConfigDispatcher {

    BikeConfig dispatch(ManufacturerAd manufacturerAd);
}
