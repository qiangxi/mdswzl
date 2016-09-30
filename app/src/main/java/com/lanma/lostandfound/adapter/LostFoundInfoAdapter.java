package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.LostFoundInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/2 15:51.
 */
public class LostFoundInfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<LostFoundInfo> mList;

    public LostFoundInfoAdapter(Context context, List<LostFoundInfo> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lost_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mLostDesc.setText(mList.get(position).getThingDesc());//描述
        holder.mLostType.setText(mList.get(position).getThingType());//类型
        holder.mLostTime.setText(mList.get(position).getCreatedAt());//发布时间
        holder.mLostMark.setText(mList.get(position).getThingMark());//备注
        //物品图片
        if (!TextUtils.isEmpty(mList.get(position).getThingImageUrl())) {
            String thingImageUrl = mList.get(position).getThingImageUrl();
            String[] thingImageUrlArray = thingImageUrl.split(",");
            Glide.with(mContext).load(thingImageUrlArray[0]).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.bg_load_failed).error(R.drawable.bg_load_failed).
                    into(holder.mLostImage);
        } else {
            Glide.with(mContext).load(R.drawable.bg_load_failed).centerCrop().into(holder.mLostImage);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.lostImage)
        ImageView mLostImage;
        @Bind(R.id.lostDesc)
        TextView mLostDesc;
        @Bind(R.id.lostType)
        TextView mLostType;
        @Bind(R.id.lostTime)
        TextView mLostTime;
        @Bind(R.id.lostMark)
        TextView mLostMark;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
