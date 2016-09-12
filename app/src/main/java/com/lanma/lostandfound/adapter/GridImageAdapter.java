package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.constants.AppConstants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/3 15:06.
 */
public class GridImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mList;
    private String loadType;//加载图片的类型(网络加载/本地加载)

    public GridImageAdapter(Context context, ArrayList<String> list, String loadType) {
        this.mContext = context;
        this.mList = list;
        this.loadType = loadType;
    }

    @Override
    public int getCount() {

        return checkListData().size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (loadType) {
            //本地加载
            case AppConstants.LocalLoadType:
                if (null == mList.get(position)) {
                    holder.mItemImage.setImageResource(R.drawable.bg_add_image);
                } else {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inSampleSize = 4;
                    option.inDither = false;    /*不进行图片抖动处理*/
                    option.inPreferredConfig = null;  /*设置让解码器以最佳方式解码*/
                    /* 下面两个字段需要组合使用 */
                    option.inPurgeable = true;
                    option.inInputShareable = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(mList.get(position), option);
                    if (null != bitmap) {
                        holder.mItemImage.setImageBitmap(bitmap);
                    } else {
                        holder.mItemImage.setImageResource(R.drawable.bg_add_image);
                    }
                }
                break;
            //网络加载
            case AppConstants.NetWorkLoadType:
                Glide.with(mContext).load(mList.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).
                        centerCrop().placeholder(R.drawable.bg_loading_image).error(R.drawable.bg_load_image_failed).
                        into(holder.mItemImage);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.itemImage)
        ImageView mItemImage;//图片

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private ArrayList<String> checkListData() {
        for (int i = 0; i < mList.size(); i++) {
            if (TextUtils.isEmpty(mList.get(i))) {
                mList.remove(i);
            }
        }

        if (mList.size() == 0) {
            for (int i = 0; i < 3; i++) {
                mList.add(null);
            }
        }

        if (mList.size() == 1) {
            for (int i = 0; i < 2; i++) {
                mList.add(null);
            }
        }
        if (mList.size() == 2) {
            mList.add(null);
        }
        return mList;
    }
}
