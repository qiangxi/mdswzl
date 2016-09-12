package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanma.lostandfound.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/5 13:39.
 */
public class ThingTypeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] thingTypeArray;

    public ThingTypeAdapter(Context context, String[] thingTypeArray) {
        this.mContext = context;
        this.thingTypeArray = thingTypeArray;
    }

    @Override
    public int getCount() {
        return thingTypeArray.length;
    }

    @Override
    public Object getItem(int position) {
        return thingTypeArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_thing_type_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemThingType.setText(thingTypeArray[position]);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.itemThingType)
        TextView mItemThingType;//物品类型

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
