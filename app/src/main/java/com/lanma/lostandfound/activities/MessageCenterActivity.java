package com.lanma.lostandfound.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.MessageInfoAdapter;
import com.lanma.lostandfound.beans.MessageInfo;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.MessageInfoPresenter;
import com.lanma.lostandfound.utils.EmptyViewUtil;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity implements MessageInfoPresenter {

    @Bind(R.id.messageListView)
    ListView mMessageListView;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    private LoadingDialog mDialog;
    private MessageInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("消息中心");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        ServerConnection.getMessageInfo(this);
    }

    @Override
    public void getMessageInfoSuccess(List<MessageInfo> list) {
        mDialog.dismiss();
        if (null != list && list.size() > 0) {
            mAdapter = new MessageInfoAdapter(this, list);
            mMessageListView.setAdapter(mAdapter);
        } else {
            showToast("还没有任何消息呦~~");
            mMessageListView.setEmptyView(EmptyViewUtil.getEmptyView(this, mMessageListView, "宝宝没用\n没能查到您的消息记录"));
        }
    }

    @Override
    public void getMessageInfoFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
        mMessageListView.setEmptyView(EmptyViewUtil.getEmptyView(this, mMessageListView, "宝宝没用\n没能查到您的消息记录"));
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
