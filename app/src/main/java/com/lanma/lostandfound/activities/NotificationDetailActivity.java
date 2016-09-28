package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationDetailActivity extends BaseActivity {

    @Bind(R.id.notificationDetailTitle)
    TextView mNotificationDetailTitle;
    @Bind(R.id.notificationDetailTime)
    TextView mNotificationDetailTime;
    @Bind(R.id.notificationDetailContent)
    TextView mNotificationDetailContent;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("通知详情");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String notificationTitle = intent.getStringExtra("notificationTitle");
        String notificationContent = intent.getStringExtra("notificationContent");
        mNotificationDetailTitle.setText(notificationTitle);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mNotificationDetailTime.setText("发布时间:" + sdf.format(System.currentTimeMillis()));
        mNotificationDetailContent.setText(notificationContent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回主界面
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
