package com.example.yankaibang.app.application;

import android.app.Application;

import com.example.yankaibang.app.SecretProtocolAdapter;
import com.example.yankaibang.bike.TbitBle;

/**
 * Created by yankaibang on 2017/8/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TbitBle.initialize(getApplicationContext(), new SecretProtocolAdapter());
    }
}
