package com.example.yankaibang.bike.services.config;


import com.example.yankaibang.bike.services.command.comparator.CommandComparator;
import com.example.yankaibang.bike.services.resolver.Resolver;

/**
 * Created by Salmon on 2017/3/20 0020.
 */

public interface BikeConfig {

    Uuid getUuid();

    CommandComparator getComparator();

    Resolver getResolver();
}
