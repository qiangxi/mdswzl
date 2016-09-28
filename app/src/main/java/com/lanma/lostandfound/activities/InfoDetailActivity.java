package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.GridImageAdapter;
import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.constants.AppConstants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 失物招领信息详情页
 */
public class InfoDetailActivity extends BaseActivity {

    @Bind(R.id.InfoDetailDesc)
    TextView mInfoDetailDesc;//物品描述
    @Bind(R.id.InfoDetailType)
    TextView mInfoDetailType;//物品类型
    @Bind(R.id.InfoDetailTime)
    TextView mInfoDetailTime;//信息发布时间
    @Bind(R.id.InfoDetailGridImages)
    GridView mInfoDetailGridImages;//物品图片
    @Bind(R.id.InfoDetailWhere)
    TextView mInfoDetailWhere;//在哪丢失
    @Bind(R.id.InfoDetailThanksWay)
    TextView mInfoDetailThanksWay;//感谢方式
    @Bind(R.id.InfoDetailDescDetail)
    TextView mInfoDetailDescDetail;//详细描述
    @Bind(R.id.InfoDetailLookUserInfo)
    Button mInfoDetailLookUserInfo;
    @Bind(R.id.InfoDetailCallPhone)
    Button mInfoDetailCallPhone;
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    private LostFoundInfo info;
    private GridImageAdapter mAdapter;
    private ArrayList<String> mList = new ArrayList<>();
    private SpannableStringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ButterKnife.bind(this);
        initToolBar();
        initData();
    }

    private void initToolBar() {
        mToolBar.setTitle("信息详情");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        info = (LostFoundInfo) intent.getSerializableExtra("LostOrFoundInfo");
        //如果是招领类型
        if (AppConstants.FoundInfoType.equals(info.getInfoType())) {
            mInfoDetailLookUserInfo.setText("查看'雷锋'信息");
            mInfoDetailCallPhone.setText("联系'雷锋'");
        }
        setDataToView(info);
    }

    /**
     * 给view填充数据
     */
    private void setDataToView(LostFoundInfo info) {
        //描述
        mInfoDetailDesc.setText(info.getThingDesc());
        //物品类型
        builder = new SpannableStringBuilder("物品类型: " + info.getThingType());
        builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mInfoDetailType.setText(builder);
        //发布时间
        mInfoDetailTime.setText(info.getCreatedAt());
        //物品图片
        String thingImageUrl = info.getThingImageUrl();
        if (TextUtils.isEmpty(thingImageUrl)) {
            mInfoDetailGridImages.setVisibility(View.GONE);
        } else {
            String[] thingImageUrlArray = thingImageUrl.split(",");
            for (String imageUrl : thingImageUrlArray) {
                mList.add(imageUrl);
            }
            mAdapter = new GridImageAdapter(this, mList, AppConstants.NetWorkLoadType);
            mInfoDetailGridImages.setAdapter(mAdapter);
        }

        //在哪丢失
        if (TextUtils.isEmpty(info.getThingWhereFound())) {
            mInfoDetailWhere.setVisibility(View.GONE);
        } else {
            builder = new SpannableStringBuilder("大概在哪丢失:  " + info.getThingWhereFound());
            builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mInfoDetailWhere.setText(builder);
        }
        //感谢方式
        if (TextUtils.isEmpty(info.getStudentThanksWay())) {
            mInfoDetailThanksWay.setVisibility(View.GONE);
        } else {
            builder = new SpannableStringBuilder("酬谢方式:  " + info.getStudentThanksWay());
            builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mInfoDetailThanksWay.setText(builder);
        }
        //详细描述
        builder = new SpannableStringBuilder("详细描述:  " + info.getThingMark());
        builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mInfoDetailDescDetail.setText(builder);
    }

    @OnClick({R.id.InfoDetailLookUserInfo, R.id.InfoDetailCallPhone})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //查看失主信息
            case R.id.InfoDetailLookUserInfo:
                intent = new Intent(this, LostUserInfoActivity.class);
                intent.putExtra("studentInfo", info.getStudentInfo());
                startActivity(intent);
                break;
            // 联系失主
            case R.id.InfoDetailCallPhone:
                try {
                    intent = new Intent();
                    // 系统默认的action，用来打开默认的电话界面
                    intent.setAction(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + info.getStudentPhoneNumber());
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                    showToast("获取电话异常");
                    e.printStackTrace();
                }
                break;
        }
    }

    @OnItemClick(R.id.InfoDetailGridImages)
    public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, LookImageActivity.class);
        intent.putStringArrayListExtra("ImageList", checkListData());
        startActivity(intent);
    }

    private ArrayList<String> checkListData() {
        //删除掉数据为null的位置,保留有数据的位置
        for (int i = 0; i < mList.size(); i++) {
            if (TextUtils.isEmpty(mList.get(i))) {
                mList.remove(i);
            }
        }
        //把检查之后的mList重新由0-N排序并返回
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < mList.size(); i++) {
            list.add(mList.get(i));
        }
        return list;
    }
}
