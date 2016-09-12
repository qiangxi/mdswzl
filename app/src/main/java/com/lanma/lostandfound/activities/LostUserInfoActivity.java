package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.utils.ImageViewTintUtil;
import com.lanma.lostandfound.view.RoundImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LostUserInfoActivity extends BaseActivity {
    @Bind(R.id.lostUserInfoHeaderImage)
    RoundImageView mLostUserInfoHeaderImage;//头像
    @Bind(R.id.lostUserInfoName)
    TextView mLostUserInfoName;//姓名
    @Bind(R.id.lostUserInfoSex)
    TextView mLostUserInfoSex;//性别
    @Bind(R.id.lostUserInfoAge)
    TextView mLostUserInfoAge;//年龄
    @Bind(R.id.lostUserInfoEmailAddress)
    TextView mLostUserInfoEmailAddress;//邮箱
    @Bind(R.id.lostUserInfoPhoneNumber)
    TextView mLostUserInfoPhoneNumber;//手机号
    @Bind(R.id.lostUserInfoYear)
    TextView mLostUserInfoYear;//入学年限
    @Bind(R.id.lostUserInfoCollege)
    TextView mLostUserInfoCollege;//学院
    @Bind(R.id.lostUserInfoSpecialities)
    TextView mLostUserInfoSpecialities;//专业
    @Bind(R.id.lostUserInfoApartNumber)
    TextView mLostUserInfoApartNumber;//公寓号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_user_info);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.lostUserInfoBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        StudentInfo studentInfo = (StudentInfo) intent.getSerializableExtra("studentInfo");
        setDataToView(studentInfo);
    }

    private void setDataToView(StudentInfo studentInfo) {
        if (null != studentInfo) {
            //设置头像
            Glide.with(this).load(studentInfo.getStudentHeadImage()).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                    into(mLostUserInfoHeaderImage);
            //姓名
            if (TextUtils.isEmpty(studentInfo.getStudentName())) {
                mLostUserInfoName.setVisibility(View.GONE);
            } else {
                mLostUserInfoName.setText(studentInfo.getStudentName());
            }
            //性别
            if ("女".equals(studentInfo.getStudentSex())) {
                mLostUserInfoSex.setText("女");
            } else {
                mLostUserInfoSex.setText("男");
            }
            //年龄
            if (TextUtils.isEmpty(studentInfo.getStudentAge())) {
                mLostUserInfoAge.setVisibility(View.GONE);

            } else {
                mLostUserInfoAge.setText(studentInfo.getStudentAge());
            }
            //邮箱
            if (TextUtils.isEmpty(studentInfo.getEmail())) {
                mLostUserInfoEmailAddress.setVisibility(View.GONE);
            } else {
                //隐藏邮箱：只显示@前面的首位和末位
                mLostUserInfoEmailAddress.setText(studentInfo.getEmail().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
            }
            //手机号
            if (TextUtils.isEmpty(studentInfo.getMobilePhoneNumber())) {
                mLostUserInfoPhoneNumber.setVisibility(View.GONE);
            } else {
                // 隐藏手机号码中间四位
                mLostUserInfoPhoneNumber.setText(studentInfo.getMobilePhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            //学院
            if (TextUtils.isEmpty(studentInfo.getStudentOfCollege())) {
                mLostUserInfoCollege.setVisibility(View.GONE);
            } else {
                mLostUserInfoCollege.setText(studentInfo.getStudentOfCollege());
            }
            //专业
            if (TextUtils.isEmpty(studentInfo.getStudentSpecialities())) {
                mLostUserInfoSpecialities.setVisibility(View.GONE);
            } else {
                mLostUserInfoSpecialities.setText(studentInfo.getStudentSpecialities());
            }
            //入学年份
            if (TextUtils.isEmpty(studentInfo.getStudentYear())) {
                mLostUserInfoYear.setVisibility(View.GONE);
            } else {
                mLostUserInfoYear.setText(studentInfo.getStudentYear() + "级");
            }
            //公寓号
            if (TextUtils.isEmpty(studentInfo.getStudentApartNumber())) {
                mLostUserInfoApartNumber.setVisibility(View.GONE);
            } else {
                mLostUserInfoApartNumber.setText(studentInfo.getStudentApartNumber());
            }
        } else {
            showSnackBar("获取信息失败");
        }
    }

    @OnClick(R.id.lostUserInfoBack)
    public void onClick() {
        finish();
    }
}
