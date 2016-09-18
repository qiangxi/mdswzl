package com.lanma.lostandfound.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.lanma.lostandfound.R;

import net.youmi.android.AdManager;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

public class StartActivity extends BaseActivity implements SpotListener {
    @Bind(R.id.adContainer)
    RelativeLayout mAdContainer;//广告容器
    private CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        //Bmob初始化
        Bmob.initialize(this, "0fd42f33574319cf39864c7ee043482d");
        //有米广告初始化
        AdManager.getInstance(this).init("16125092afdec464", "29a780a031bc7bd9", false, false);
        setIsFirstReceiveToTrue();
        showStartAd();
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 展示启动页过渡广告
     */
    private void showStartAd() {
        // 实例化开屏广告设置类
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        // 设置开屏结束后跳转的窗口
        splashViewSettings.setTargetClass(MainActivity.class);
        // 使用默认布局参数
        splashViewSettings.setSplashViewContainer(mAdContainer);
        // 展示开屏广告
        SpotManager.getInstance(this).showSplash(this, splashViewSettings, this);
    }

    private void CountDownTime() {
        SharedPreferences preferences = getSharedPreferences("isFirstStart", Context.MODE_PRIVATE);
        boolean isFirstStart = preferences.getBoolean("isFirstStart", true);
        if (isFirstStart) {
            mAdContainer.setBackgroundResource(R.drawable.pic01);
            preferences.edit().putBoolean("isFirstStart", false).apply();
            downTimer = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            downTimer.start();
        }
    }

    /**
     * 该方法用来为解决网络状态变化时发送两次广播的问题,需要在startActivity中进行初始化为true才行,
     * 因为有可能当网络未连接的提示框弹出还没有dismiss时,用户强制退出了软件,此时isFirstReceive是false,
     * 这样会造成以后网络再次未连接时,由于isFirstReceive是false,导致不弹框的问题,所以需要在每次启动时把
     * isFirstReceive强制置为true,然后在网络未连接提示框的两个按钮点击事件中以及onKeyDown事件中再次把
     * isFirstReceive强制置为true,而只在网络未连接提示框弹出后把isFirstReceive置为false.
     */

    private void setIsFirstReceiveToTrue() {
        SharedPreferences preferences = getSharedPreferences("ReceiveNetChange", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isFirstReceive", true).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onShowSuccess() {
        //展示成功
    }

    @Override
    public void onShowFailed(int errorCode) {
        // 展示失败
        //mAdContainer.setBackgroundResource(R.drawable.pic01);
    }

    @Override
    public void onSpotClosed() {
        //插播被关闭

    }

    @Override
    public void onSpotClicked(boolean b) {
        //插播被点击
    }
}
