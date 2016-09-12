package com.lanma.lostandfound.utils;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 作者 任强强 on 2016/8/29 14:12.
 * view初始化完毕之后，建议在onCreate方法里调用 ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.xx));即可
 */
public class ImageViewTintUtil {

    public static void setImageViewtint(final ImageView imageView, final int color) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setColorFilter(color);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setColorFilter(null);
                        break;
                }
                //这里一定要return false，不然该方法会拦截事件，造成不能响应点击等操作
                return false;
            }
        });
    }

    public static void setImageViewTint(final ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setColorFilter(Color.parseColor("#89DBFA"));
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setColorFilter(null);
                        break;
                }
                //这里一定要return false，不然该方法会拦截事件，造成不能响应点击等操作
                return false;
            }
        });
    }
}
