package com.lanma.lostandfound.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanma.lostandfound.R;


/**
 * 作者 任强强 on 2016/8/10 15:16.
 */
public class EmptyViewUtil {
    public static View getEmptyView(Context context, AbsListView listView) {
        View view = LayoutInflater.from(context).inflate(R.layout.empty_view_layout, null);
        if (listView.getParent() instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) listView.getParent();
            parentView.addView(view, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        return null;
    }

    public static View getEmptyView(Context context, AbsListView listView, @NonNull String emptyText) {
        View view = LayoutInflater.from(context).inflate(R.layout.empty_view_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.emptyViewText);
        textView.setText(emptyText + "");
        if (listView.getParent() instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) listView.getParent();
            parentView.addView(view, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        return null;
    }
}
