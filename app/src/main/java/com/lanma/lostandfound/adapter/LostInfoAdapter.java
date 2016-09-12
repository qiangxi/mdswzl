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
import com.lanma.lostandfound.view.RoundImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/2 15:51.
 */
public class LostInfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<LostFoundInfo> mList;

    public LostInfoAdapter(Context context, List<LostFoundInfo> list) {
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
        holder.lostDesc.setText(mList.get(position).getThingDesc());
        holder.lostType.setText("物品类型:\n" + mList.get(position).getThingType());
        holder.lostTime.setText("信息发布时间: " + mList.get(position).getCreatedAt());
        //设置头像
        if (null == mList.get(position).getStudentInfo()) {
            holder.lostHeadImage.setImageResource(R.drawable.icon_passenger_man);
        } else {
            Glide.with(mContext).load(mList.get(position).getStudentInfo().getStudentHeadImage()).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                    into(holder.lostHeadImage);
        }
        //物品图片
        if (TextUtils.isEmpty(mList.get(position).getThingImageUrl())) {
            holder.lostImage.setVisibility(View.GONE);
        } else {
            if (null == mList.get(position).getStudentInfo()) {
                holder.lostImage.setVisibility(View.GONE);
            } else {
                holder.lostImage.setVisibility(View.VISIBLE);
                String thingImageUrl = mList.get(position).getThingImageUrl();
                String[] thingImageUrlArray = thingImageUrl.split(",");
                Glide.with(mContext).load(thingImageUrlArray[0]).diskCacheStrategy(DiskCacheStrategy.ALL).
                        centerCrop().placeholder(R.drawable.bg_loading_image).error(R.drawable.bg_load_image_failed).
                        into(holder.lostImage);
            }
        }
        holder.lostWhereFind.setText("备注:" + mList.get(position).getThingMark());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.lostHeadImage)
        RoundImageView lostHeadImage;
        @Bind(R.id.lostDesc)
        TextView lostDesc;
        @Bind(R.id.lostType)
        TextView lostType;
        @Bind(R.id.lostTime)
        TextView lostTime;
        @Bind(R.id.lostImage)
        ImageView lostImage;
        @Bind(R.id.lostWhereFind)
        TextView lostWhereFind;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
