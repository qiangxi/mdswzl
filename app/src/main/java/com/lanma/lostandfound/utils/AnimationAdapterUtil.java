package com.lanma.lostandfound.utils;

import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

/**
 * 作者 任强强 on 2016/8/9 17:01.
 * 用于给AbsListView的子类添加item动画效果
 */
public class AnimationAdapterUtil {

    private AnimationAdapterUtil() {
    }

    /**
     * 透明度动画效果
     */
    public static AlphaInAnimationAdapter getAnimationInAdapter(BaseAdapter adapter, AbsListView listView) {
        AlphaInAnimationAdapter mAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        mAnimationAdapter.setAbsListView(listView);
        return mAnimationAdapter;
    }

    /**
     * item从底部进入动画效果
     */
    public static SwingBottomInAnimationAdapter getSwingBottomInAnimationAdapter(BaseAdapter adapter, AbsListView listView) {
        SwingBottomInAnimationAdapter animationAdapter =new SwingBottomInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        return animationAdapter;
    }



}
