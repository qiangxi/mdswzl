package com.lanma.lostandfound.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * 作者 qiang_xi on 2016/9/3 18:03.
 */
public class DirectionListView extends ListView {
    private OnScrollDirectionListener mListener;
    private float startY = 0;//按下时y值
    private int mTouchSlop;//系统值

    public DirectionListView(Context context) {
        this(context, null);
    }

    public DirectionListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DirectionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY() - startY) > mTouchSlop) {
                    if (ev.getY() - startY >= 0) {
                        mListener.onScrollDown();
                    } else {
                        mListener.onScrollUp();
                    }
                }
                startY = ev.getY();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnScrollDirectionListener(OnScrollDirectionListener listener) {
        this.mListener = listener;
    }

    public interface OnScrollDirectionListener {
        //向上滑动
        void onScrollUp();

        //向下滑动
        void onScrollDown();
    }
}
