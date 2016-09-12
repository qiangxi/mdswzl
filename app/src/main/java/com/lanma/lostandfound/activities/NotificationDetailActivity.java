package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationDetailActivity extends BaseActivity {

    @Bind(R.id.notificationDetailTitle)
    TextView mNotificationDetailTitle;
    @Bind(R.id.notificationDetailTime)
    TextView mNotificationDetailTime;
    @Bind(R.id.notificationDetailContent)
    TextView mNotificationDetailContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
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

    @OnClick(R.id.notificationDetailBack)
    public void onClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
