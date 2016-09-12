package com.lanma.lostandfound.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.MessageInfoAdapter;
import com.lanma.lostandfound.beans.MessageInfo;
import com.lanma.lostandfound.beans.YouMiAdUtils;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.MessageInfoPresenter;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity implements MessageInfoPresenter {

    @Bind(R.id.messageListView)
    ListView mMessageListView;

    private LoadingDialog mDialog;
    private MessageInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.messageBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        ServerConnection.getMessageInfo(this);
    }

    @OnClick(R.id.messageBack)
    public void onClick() {
        finish();
    }

    @Override
    public void getMessageInfoSuccess(List<MessageInfo> list) {
        mDialog.dismiss();
        if (null != list && list.size() > 0) {
            mAdapter = new MessageInfoAdapter(this, list);
            mMessageListView.setAdapter(mAdapter);
        } else {
            showSnackBar("还没有任何消息呦~~");
        }
    }

    @Override
    public void getMessageInfoFailure(String failureMessage) {
        mDialog.dismiss();
        showSnackBar(failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
