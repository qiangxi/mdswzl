package com.lanma.lostandfound.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.lanma.lostandfound.activities.BaseActivity;
import com.lanma.lostandfound.utils.NetWorkUtils;


/**
 * 作者 任强强 on 2016/7/29 14:06.
 * 监听网络状态的receiver
 */
public class NetConnectedReceiver extends BroadcastReceiver {
    private NetConnection mConnection = BaseActivity.mConnection;
    private boolean isFirstReceive = true;
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (null != mConnection) {
                if (null == preferences) {
                    preferences = context.getSharedPreferences("ReceiveNetChange", Context.MODE_PRIVATE);
                }
                isFirstReceive = preferences.getBoolean("isFirstReceive",true);
                Log.e("tag", "网络状态发生变化" + isFirstReceive);
                if (isFirstReceive) {
                    mConnection.isNetConnected(NetWorkUtils.hasNetConnection(context));
                    Log.e("tag", "网络状态发生变化" + intent.toString());
                }
            }
        }
    }

    public interface NetConnection {
        void isNetConnected(boolean isNetConnected);
    }
}
