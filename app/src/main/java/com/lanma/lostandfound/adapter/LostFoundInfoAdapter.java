package com.lanma.lostandfound.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.view.RoundImageView;

import java.util.List;

/**
 * 作者 任强强 on 2016/9/2 15:51.
 */
public class LostFoundInfoAdapter extends BaseQuickAdapter<LostFoundInfo> {
    public LostFoundInfoAdapter(List<LostFoundInfo> list) {
        super(R.layout.item_lost_layout, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, LostFoundInfo info) {
        holder.setText(R.id.lostDesc, info.getThingDesc())//描述
                .setText(R.id.lostType, info.getThingType())//类型
                .setText(R.id.lostTime, info.getCreatedAt())//发布时间
                .setText(R.id.lostMark, info.getThingMark())//备注
                .setVisible(R.id.lostHeadImage, null != info.getStudentInfo())
                .setVisible(R.id.lostImage, !TextUtils.isEmpty(info.getThingImageUrl()));
        //头像
        if (null != info.getStudentInfo()) {
            Glide.with(mContext).load(info.getStudentInfo().getStudentHeadImage()).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                    into((RoundImageView) holder.getView(R.id.lostHeadImage));
        }
        //物品图片
        if (!TextUtils.isEmpty(info.getThingImageUrl())) {
            String thingImageUrl = info.getThingImageUrl();
            String[] thingImageUrlArray = thingImageUrl.split(",");
            Glide.with(mContext).load(thingImageUrlArray[0]).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.bg_loading_image).error(R.drawable.bg_load_image_failed).
                    into((ImageView) holder.getView(R.id.lostImage));
        }
    }
}
