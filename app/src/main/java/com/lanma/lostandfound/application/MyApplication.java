package com.lanma.lostandfound.application;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者 任强强 on 2016/9/9 16:56.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送初始化
        JPushInterface.init(this);
        //友盟统计初始化
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this,
                "57d4d66be0f55a77c6001624", "Xiaomi", MobclickAgent.EScenarioType.E_UM_NORMAL, true);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.startWithConfigure(config);
    }
}
