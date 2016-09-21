package com.lanma.lostandfound.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.spot.SpotManager;

/**
 * 作者 任强强 on 2016/9/12 13:50.
 */
public class YouMiAdUtils {
    private YouMiAdUtils() {
    }

    /**
     * 广告生命周期方法onPause
     */
    public static void onAdPause(Context context) {
        SpotManager.getInstance(context).onPause();
    }

    /**
     * 广告生命周期方法onStop
     */
    public static void onAdStop(Context context) {
        SpotManager.getInstance(context).onStop();

    }

    /**
     * 广告生命周期方法onDestroy
     */
    public static void onAdDestroy(Context context) {
        SpotManager.getInstance(context).onDestroy();
    }

    /**
     * 展示一个广告条
     */
    public static void showBannerAd(Context context, LinearLayout layout) {
        // 获取广告条
        View bannerView = BannerManager.getInstance(context)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {

                    }

                    @Override
                    public void onSwitchBanner() {

                    }

                    @Override
                    public void onRequestFailed() {

                    }
                });
        // 将广告条加入到布局中
        layout.addView(bannerView);
    }

    /**
     * 在屏幕底部展示一个悬浮广告条
     */
    public static void showSuspendBannerAdInBottom(Activity context) {
        // 实例化 LayoutParams（重要）
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
        // 获取广告条
        View bannerView = BannerManager.getInstance(context)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {

                    }

                    @Override
                    public void onSwitchBanner() {

                    }

                    @Override
                    public void onRequestFailed() {

                    }
                });
        // 调用 Activity 的 addContentView 函数
        context.addContentView(bannerView, layoutParams);
    }

}
