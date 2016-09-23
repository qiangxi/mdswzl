package com.lanma.lostandfound.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * 作者 任强强 on 2016/9/22 13:37.
 */
public class DownLoadService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        Logger.e("onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.e("onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downloadPath = intent.getStringExtra("downloadPath");
        Logger.e("onStartCommand,下载地址:" + downloadPath);
        return super.onStartCommand(intent, flags, startId);
    }
}
