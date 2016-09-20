package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.GridImageAdapter;
import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.dialog.DeleteInfoDialog;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.DeleteCurrentInfoPresenter;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLostAndFoundDetailActivity extends BaseActivity  {

    @Bind(R.id.myLostAndFoundDetailDesc)
    TextView mMyLostAndFoundDetailDesc;
    @Bind(R.id.myLostAndFoundDetailType)
    TextView mMyLostAndFoundDetailType;
    @Bind(R.id.myLostAndFoundDetailTime)
    TextView mMyLostAndFoundDetailTime;
    @Bind(R.id.myLostAndFoundDetailGridImages)
    GridView mMyLostAndFoundDetailGridImages;
    @Bind(R.id.myLostAndFoundDetailWhere)
    TextView mMyLostAndFoundDetailWhere;
    @Bind(R.id.myLostAndFoundDetailThanksWay)
    TextView mMyLostAndFoundDetailThanksWay;
    @Bind(R.id.myLostAndFoundDetailDescDetail)
    TextView mMyLostAndFoundDetailDescDetail;
    @Bind(R.id.myLostAndFoundDetailDelete)
    Button mMyLostAndFoundDetailDelete;

    private LoadingDialog mDialog;
    private LostFoundInfo info;
    private GridImageAdapter mAdapter;
    private ArrayList<String> mList = new ArrayList<>();
    private SpannableStringBuilder builder;
    private int position;
    private String infoType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_and_found_detail);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.myLostAndFoundDetailBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        position = getIntent().getIntExtra("position", -1);
        info = (LostFoundInfo) getIntent().getSerializableExtra("LostOrFoundInfo");
        infoType = info.getInfoType();
        //如果是失物类型
        if (AppConstants.LostInfoType.equals(infoType)) {
            mMyLostAndFoundDetailDelete.setText("已找到失物，删除该信息");
        }
        setDataToView(info);
    }

    /**
     * 给view填充数据
     */
    private void setDataToView(LostFoundInfo info) {
        //描述
        mMyLostAndFoundDetailDesc.setText(info.getThingDesc());
        //物品类型
        builder = new SpannableStringBuilder("物品类型: " + info.getThingType());
        builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMyLostAndFoundDetailType.setText(builder);
        //发布时间
        mMyLostAndFoundDetailTime.setText(info.getCreatedAt());
        //物品图片
        String thingImageUrl = info.getThingImageUrl();
        if (TextUtils.isEmpty(thingImageUrl)) {
            mMyLostAndFoundDetailGridImages.setVisibility(View.GONE);
        } else {
            String[] thingImageUrlArray = thingImageUrl.split(",");
            for (String imageUrl : thingImageUrlArray) {
                mList.add(imageUrl);
            }
            mAdapter = new GridImageAdapter(this, mList, AppConstants.NetWorkLoadType);
            mMyLostAndFoundDetailGridImages.setAdapter(mAdapter);
        }

        //在哪丢失
        if (TextUtils.isEmpty(info.getThingWhereFound())) {
            mMyLostAndFoundDetailWhere.setVisibility(View.GONE);
        } else {
            builder = new SpannableStringBuilder("大概在哪丢失:  " + info.getThingWhereFound());
            builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mMyLostAndFoundDetailWhere.setText(builder);
        }
        //感谢方式
        if (TextUtils.isEmpty(info.getStudentThanksWay())) {
            mMyLostAndFoundDetailThanksWay.setVisibility(View.GONE);
        } else {
            builder = new SpannableStringBuilder("酬谢方式:  " + info.getStudentThanksWay());
            builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mMyLostAndFoundDetailThanksWay.setText(builder);
        }
        //详细描述
        builder = new SpannableStringBuilder("详细描述:  " + info.getThingMark());
        builder.setSpan(new AbsoluteSizeSpan(16, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMyLostAndFoundDetailDescDetail.setText(builder);
    }


    @OnClick({R.id.myLostAndFoundDetailBack, R.id.myLostAndFoundDetailDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myLostAndFoundDetailBack:
                finish();
                break;
            case R.id.myLostAndFoundDetailDelete:
                DeleteInfoDialog dialog = new DeleteInfoDialog(this);
                if (AppConstants.LostInfoType.equals(infoType)) {
                    dialog.setDeleteMessage("您已经找到丢失的物品了吗？");
                }else{
                    dialog.setDeleteMessage("您已经找到该物品的失主了吗？");
                }
                dialog.setOnDeleteClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServerConnection.deleteCurrentInfo(info.getObjectId(), position, new deletePresenter());
                    }
                });
                dialog.show();
                break;
        }
    }

    private class deletePresenter implements DeleteCurrentInfoPresenter{
        public deletePresenter() {
        }

        @Override
        public void onDeleting() {
            mDialog.show();
        }

        @Override
        public void onDeleteSuccess(int position) {
            mDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("position",position);
            setResult(AppConstants.myLostAndFoundInfoDetailRequestCode, intent);
            finish();
        }

        @Override
        public void onDeleteFailure(String failureMessage) {
            mDialog.dismiss();
            showToast(failureMessage);
        }
    }
}
