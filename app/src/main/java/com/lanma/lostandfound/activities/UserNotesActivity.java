package com.lanma.lostandfound.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户须知
 */
public class UserNotesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.userNoteBack));
        getSwipeBackLayout().setEnableGesture(true);
    }

    @OnClick(R.id.userNoteBack)
    public void onClick() {
        finish();
    }
}
