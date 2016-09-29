package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.DrawerLayoutBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qiang_xi on 2016/9/29 21:54 23:24.
 */

public class DrawerLayoutAdapter extends BaseAdapter {
    private Context context;
    private List<DrawerLayoutBean> list;

    public DrawerLayoutAdapter(Context context, List<DrawerLayoutBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drawer_list_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemIcon.setImageResource(list.get(position).getResId());
        holder.itemDesc.setText(list.get(position).getDesc());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.itemIcon)
        ImageView itemIcon;
        @Bind(R.id.itemDesc)
        TextView itemDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
