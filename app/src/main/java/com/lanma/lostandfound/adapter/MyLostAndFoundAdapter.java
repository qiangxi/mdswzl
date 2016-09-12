package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.LostFoundInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/11 14:14.
 */
public class MyLostAndFoundAdapter extends BaseAdapter {
    private Context mContext;
    private List<LostFoundInfo> mList;

    public MyLostAndFoundAdapter(Context context, List<LostFoundInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_lost_found_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemMyLostAndFoundDesc.setText(mList.get(position).getThingDesc());
        holder.mItemMyLostAndFoundType.setText(mList.get(position).getThingType());
        holder.mItemMyLostAndFoundTime.setText(mList.get(position).getCreatedAt());
        holder.mItemMyLostAndFoundDescDetail.setText(mList.get(position).getThingMark());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.itemMyLostAndFoundDesc)
        TextView mItemMyLostAndFoundDesc;
        @Bind(R.id.itemMyLostAndFoundType)
        TextView mItemMyLostAndFoundType;
        @Bind(R.id.itemMyLostAndFoundTime)
        TextView mItemMyLostAndFoundTime;
        @Bind(R.id.itemMyLostAndFoundDescDetail)
        TextView mItemMyLostAndFoundDescDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
