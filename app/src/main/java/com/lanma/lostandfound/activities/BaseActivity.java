package com.lanma.lostandfound.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.dialog.NetworkSettingDialog;
import com.lanma.lostandfound.receiver.NetConnectedReceiver;
import com.lanma.lostandfound.swipeback.SwipeBackActivity;
import com.lanma.lostandfound.swipeback.SwipeBackLayout;
import com.lanma.lostandfound.utils.AppManager;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends SwipeBackActivity implements NetConnectedReceiver.NetConnection {
    public static NetConnectedReceiver.NetConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mConnection = this;
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        AppManager.newInstance().addActivity(this);
        configSwipeBackLayout();
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 配置侧滑返回
     */
    private void configSwipeBackLayout() {
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEnableGesture(true);//禁止各种方向的侧滑返回
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//边缘检测方向为左边
        swipeBackLayout.setScrollThresHold(0.5f);//滑动距离大于屏幕的一半时进行finish操作
    }

    /**
     * 展示一个Toast
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 错误码转String
     *
     * @param errorCode 错误码
     */
    public String errorCodeToString(String errorCode) {
        int id = getResources().getIdentifier(errorCode, "string", getPackageName());
        if (id != 0) {
            return getString(id);
        }
        return "";
    }

    /**
     * 实时监控是否有网络连接
     */
    @Override
    public void isNetConnected(boolean isNetConnected) {
        if (!isNetConnected) {
            NetworkSettingDialog dialog = new NetworkSettingDialog(BaseActivity.this);
            dialog.show();
            SharedPreferences preferences = getSharedPreferences("ReceiveNetChange", Context.MODE_PRIVATE);
            preferences.edit().putBoolean("isFirstReceive", false).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
