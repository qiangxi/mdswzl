package com.lanma.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.MessageInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 任强强 on 2016/9/10 15:35.
 */
public class MessageInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageInfo> mList;

    public MessageInfoAdapter(Context context, List<MessageInfo> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_info_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mMessageTitle.setText(mList.get(position).getMessageTitle());
        holder.mMessageContent.setText(mList.get(position).getMessageContent());
        holder.mJourneyOrderTime.setText(mList.get(position).getCreatedAt());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.messageTitle)
        TextView mMessageTitle;
        @Bind(R.id.messageContent)
        TextView mMessageContent;
        @Bind(R.id.journeyOrderTime)
        TextView mJourneyOrderTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
