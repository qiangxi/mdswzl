package com.lanma.lostandfound.utils;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;


/**
 * 作者 任强强 on 2016/8/10 15:16.
 */
public class EmptyViewUtil {
    public static View getEmptyView(Context context, AbsListView listView) {
//        View view = LayoutInflater.from(context).inflate(R.layout.empty_view_layout, null);
//        if (listView.getParent() instanceof ViewGroup) {
//            ViewGroup parentView = (ViewGroup) listView.getParent();
//            parentView.addView(view, new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            return view;
//        }
        return null;
    }
}
