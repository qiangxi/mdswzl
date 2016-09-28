package com.lanma.lostandfound.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.SuggestionPresenter;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class SuggestActivity extends BaseActivity implements SuggestionPresenter {

    @Bind(R.id.suggestContent)
    EditText mSuggestContent;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
        initToolBar();
        mDialog = new LoadingDialog(this);
        YouMiAdUtils.showBannerAd(this, (LinearLayout) findViewById(R.id.ll_banner));
    }

    private void initToolBar() {
        mToolBar.setTitle("反馈建议");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.suggestSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.suggestSubmit:
                //提交建议
                String suggest = mSuggestContent.getText().toString().trim();
                if (TextUtils.isEmpty(suggest)) {
                    showToast("再多说点吧!");
                    return;
                }
                ServerConnection.submitSuggestion(suggest, BmobUser.getCurrentUser(StudentInfo.class), this);
                break;
        }
    }

    @Override
    public void SuggestSuccessful() {
        mSuggestContent.setText("");
        mDialog.dismiss();
        showToast("吐槽成功");
    }

    @Override
    public void SuggestFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
