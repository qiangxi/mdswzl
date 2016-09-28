package com.lanma.lostandfound.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lanma.lostandfound.R;

/**
 * 作者 任强强 on 2016/9/28 11:22.
 */
public class ToolBarHelper {
    private Context mContext;
    private Toolbar toolBar;

    public ToolBarHelper(Context context) {
        this.mContext = context;
        if (null == toolBar) {
            View toolBarLayout = LayoutInflater.from(context).inflate(R.layout.tool_bar_layout, null);
            toolBar = (Toolbar) toolBarLayout.findViewById(R.id.toolBar);
        }
    }

    public Toolbar getToolBar() {
        return toolBar;
    }
}
