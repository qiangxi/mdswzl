package com.lanma.lostandfound.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.dialog.NetworkSettingDialog;
import com.lanma.lostandfound.receiver.NetConnectedReceiver;
import com.lanma.lostandfound.swipeback.SwipeBackActivity;
import com.lanma.lostandfound.swipeback.SwipeBackLayout;
import com.lanma.lostandfound.utils.AppManager;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends SwipeBackActivity implements NetConnectedReceiver.NetConnection {
    public static NetConnectedReceiver.NetConnection mConnection;
    private CoordinatorLayout mBaseContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mBaseContainer = (CoordinatorLayout) findViewById(R.id.baseContainer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        AppManager.newInstance().addActivity(this);
        configSwipeBackLayout();

    }

    /**
     * 配置侧滑返回
     */
    private void configSwipeBackLayout() {
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEnableGesture(false);//禁止各种方向的侧滑返回
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//边缘检测方向为左边
        swipeBackLayout.setScrollThresHold(0.5f);//滑动距离大于屏幕的一半时进行finish操作
    }

    /**
     * 展示一个SnackBar
     */
    public void showSnackBar(String message) {
        //去掉虚拟按键
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏虚拟按键栏
                | View.SYSTEM_UI_FLAG_IMMERSIVE //防止点击屏幕时,隐藏虚拟按键栏又弹了出来
        );
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,和难看
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }).show();
    }

    /**
     * 展示一个SnackBar
     */
    public void showSnackBar(String message, @NonNull String actionName) {
        //去掉虚拟按键
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏虚拟按键栏
                | View.SYSTEM_UI_FLAG_IMMERSIVE //防止点击屏幕时,隐藏虚拟按键栏又弹了出来
        );
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(actionName, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,和难看
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }).show();
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
