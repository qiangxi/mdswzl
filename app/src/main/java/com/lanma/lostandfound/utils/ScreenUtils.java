package com.lanma.lostandfound.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 获取屏幕宽高,像素值,px/dp/sp之间的相互转换,屏幕截图等
 *
 * @author 任强强
 *         创建于2016/1/5 20:12
 */
public class ScreenUtils {
    /**
     * 获取屏幕宽高时用，屏幕宽度的单位为px
     */
    public static final int TYPE_PX = 0;
    /**
     * 获取屏幕宽高时用,屏幕宽度的单位为dp
     */
    public static final int TYPE_DP = 1;
    /**
     * 获取当前屏幕截图时用,截图包含状态栏
     */
    public static final int TYPE_HAVE_STATUS_BAR = 2;
    /**
     * 获取当前屏幕截图时用,截图不包含状态栏
     */
    public static final int TYPE_NO_STATUS_BAR = 3;

    private ScreenUtils() {
    }

    /**
     * dp转px
     */
    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int spToPx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     */
    public static int pxToSp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param type 指定type为TYPE_DP,返回单位为dp的屏幕宽度,指定type为TYPE_PX,返回单位为px的屏幕宽度,填写其他值则抛出异常
     * @return 返回当前屏幕宽度
     */
    public static int getScreenWidth(Context context, int type) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        if (type == TYPE_DP) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (metrics.widthPixels / scale);
        } else if (type == TYPE_PX) {
            return metrics.widthPixels;
        } else {
            throw new IllegalArgumentException("type的值只能是TYPE_DP或TYPE_PX");
        }

    }

    /**
     * 获取屏幕高度
     *
     * @param type 指定type为TYPE_DP,返回单位为dp的屏幕高度,指定type为TYPE_PX,返回单位为px的屏幕高度,填写其他值则抛出异常
     * @return 返回当前屏幕高度
     */
    public static int getScreenHeight(Context context, int type) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        if (type == TYPE_DP) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (metrics.heightPixels / scale);
        } else if (type == TYPE_PX) {
            return metrics.heightPixels;
        } else {
            throw new IllegalArgumentException("type的值只能是TYPE_DP或TYPE_PX");
        }
    }

    /**
     * 获取当前屏幕截图
     *
     * @param activity 上下文的Activity
     * @param type     type为TYPE_NO_STATUS_BAR：截图不包含状态栏，type为TYPE_HAVE_STATUS_BAR：截图包含状态栏
     * @return 返回当前的屏幕截图
     */
    public static Bitmap getScreenShot(Activity activity, int type) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity, TYPE_PX);
        int height = getScreenHeight(activity, TYPE_PX);
        Bitmap bitmap;
        if (type == TYPE_HAVE_STATUS_BAR) {
            bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);
            view.destroyDrawingCache();
            return bitmap;
        } else if (type == TYPE_NO_STATUS_BAR) {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            bitmap = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                    - statusBarHeight);
            view.destroyDrawingCache();
            return bitmap;
        } else {
            throw new IllegalArgumentException("type的值只能是TYPE_NO_STATUS_BAR或TYPE_HAVE_STATUS_BAR");
        }
    }

    /**
     * 获取状态栏高度
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏高度＋标题栏(ActionBar)高度
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 设置沉浸式状态栏
     * 需在顶部控件布局中加入以下属性让内容出现在状态栏之下
     * android:clipToPadding="true"
     * android:fitsSystemWindows="true"
     */
    public static void setTranslucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 给Activity添加透明度
     */
    public static void setActivityBackgroundAlpha(Activity activity, float alpha) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    public static int getRawScreenHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取屏幕高度
     *
     * @return 返回当前屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取虚拟按键栏的高度(有虚拟按键栏时有值,没有虚拟按键栏时返回0)
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getRawScreenHeight(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }
}
