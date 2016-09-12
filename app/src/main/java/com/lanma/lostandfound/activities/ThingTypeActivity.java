package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.ThingTypeAdapter;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ThingTypeActivity extends BaseActivity {

    @Bind(R.id.thingType)
    GridView mThingType;

    private String[] thingTypeArray;
    private ThingTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_type);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.thingTypeBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
    }

    private void initData() {
        thingTypeArray = getResources().getStringArray(R.array.thing_type_array);
        mAdapter = new ThingTypeAdapter(this, thingTypeArray);
        mThingType.setAdapter(mAdapter);
    }

    @OnClick(R.id.thingTypeBack)
    public void onClick() {
        finish();
    }

    @OnItemClick(R.id.thingType)
    public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("thingType", thingTypeArray[position]);
        setResult(AppConstants.ThingTypeRequestCode, intent);
        finish();
    }
}
