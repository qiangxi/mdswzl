package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.ThingTypeAdapter;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.utils.YouMiAdUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ThingTypeActivity extends BaseActivity {

    @Bind(R.id.thingType)
    GridView mThingType;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    private String[] thingTypeArray;
    private ThingTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_type);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        YouMiAdUtils.showSuspendBannerAdInBottom(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("物品类型");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        thingTypeArray = getResources().getStringArray(R.array.thing_type_array);
        mAdapter = new ThingTypeAdapter(this, thingTypeArray);
        mThingType.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.thingType)
    public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("thingType", thingTypeArray[position]);
        setResult(AppConstants.ThingTypeRequestCode, intent);
        finish();
    }
}
