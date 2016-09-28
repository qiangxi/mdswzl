package com.lanma.lostandfound.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户须知
 */
public class UserNotesActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        ButterKnife.bind(this);
        initToolBar();
        YouMiAdUtils.showBannerAd(this, (LinearLayout) findViewById(R.id.ll_banner));
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("用户须知");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
