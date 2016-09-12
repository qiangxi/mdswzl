package com.lanma.lostandfound.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.SuggestionPresenter;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class SuggestActivity extends BaseActivity implements SuggestionPresenter {

    @Bind(R.id.suggestContent)
    EditText mSuggestContent;

    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.suggestBack));
        getSwipeBackLayout().setEnableGesture(true);
        mDialog = new LoadingDialog(this);
    }

    @OnClick({R.id.suggestBack, R.id.suggestSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回上一层
            case R.id.suggestBack:
                finish();
                break;
            case R.id.suggestSubmit:
                //提交建议
                String suggest = mSuggestContent.getText().toString().trim();
                if (TextUtils.isEmpty(suggest)) {
                    showSnackBar("再多说点吧!", "好吧");
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
        showSnackBar("吐槽成功");
    }

    @Override
    public void SuggestFailure(String failureMessage) {
        mDialog.dismiss();
        showSnackBar(failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
