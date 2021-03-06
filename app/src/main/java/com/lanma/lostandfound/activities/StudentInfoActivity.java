package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.GetStudentInfoPresenter;
import com.lanma.lostandfound.presenter.UpdateStudentInfoPresenter;
import com.lanma.lostandfound.presenter.UploadHeadImagePresenter;
import com.lanma.lostandfound.utils.FileUtils;
import com.lanma.lostandfound.utils.StringUtils;
import com.lanma.lostandfound.view.RoundImageView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class StudentInfoActivity extends BaseActivity implements GetStudentInfoPresenter, UpdateStudentInfoPresenter, UploadHeadImagePresenter {

    @Bind(R.id.studentHeaderImage)
    RoundImageView mStudentHeaderImage;//学生头像
    @Bind(R.id.studentName)
    EditText mStudentName;//学生姓名
    @Bind(R.id.studentSexMan)
    TextView mStudentSexMan;//学生性别(男) )
    @Bind(R.id.studentSexWoman)
    TextView mStudentSexWoman;//学生性别(女)
    @Bind(R.id.studentEmailAddress)
    EditText mStudentEmailAddress;//邮箱地址
    @Bind(R.id.studentPhoneNumber)
    EditText mStudentPhoneNumber;//手机号
    @Bind(R.id.studentYear)
    EditText mStudentYear;//入学年份
    @Bind(R.id.studentSpecialities)
    EditText mStudentSpecialities;//所学专业
    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    private static final String SAVE_DIRECTORY = "/mdswzl"; // 头像存储路径
    private static final String SAVE_PIC_NAME = "header.jpeg";// 头像存储名称
    private Uri photoUri; // 头像完整存储路径的uri
    public static final int FLAG_PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int FLAG_PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int FLAG_PHOTO_REQUEST_CUT = 3;// 返回裁剪处理后的图片
    private String studentSex = "男";// 用户性别
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setStatusBarColor(Color.parseColor("#47549E"));
        initToolBar();
        initData();
        initPhotoUri();
    }

    private void initToolBar() {
        mToolBar.setBackgroundColor(Color.parseColor("#47549E"));
        mToolBar.setTitle("个人信息");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.inflateMenu(R.menu.menu_student_info);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                StudentInfo.logOut();
                Intent intent = new Intent(StudentInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        ServerConnection.getStudentInfo(StudentInfo.getCurrentUser(StudentInfo.class).getUsername(), this);
    }

    /**
     * 初始化PhotoUri
     */
    private void initPhotoUri() {
        try {
            photoUri = FileUtils.getUriByFileDirAndFileName(SAVE_DIRECTORY, SAVE_PIC_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.saveUserInfo, R.id.studentHeaderImage,
            R.id.studentSexMan, R.id.studentSexWoman})
    public void onClick(View view) {
        switch (view.getId()) {
            //更新学生信息
            case R.id.saveUserInfo:
                updateStudentInfo();
                break;
            //修改头像
            case R.id.studentHeaderImage:
                chooseUserHeaderImage();
                break;
            //学生性别(男)
            case R.id.studentSexMan:
                studentSex = "男";
                setManSex();
                break;
            //学生性别(女)
            case R.id.studentSexWoman:
                studentSex = "女";
                setWomanSex();
                break;
        }
    }

    private void updateStudentInfo() {
        String studentName = mStudentName.getText().toString();
        String studentEmailAddress = mStudentEmailAddress.getText().toString();
        String studentPhoneNumber = mStudentPhoneNumber.getText().toString();
        String studentYear = mStudentYear.getText().toString();
        String studentSpecialities = mStudentSpecialities.getText().toString();
        if (!TextUtils.isEmpty(studentEmailAddress) && !StringUtils.isEmail(studentEmailAddress)) {
            showToast("邮箱格式不正确");
            return;
        }
        if (!TextUtils.isEmpty(studentPhoneNumber)) {
            if (!StringUtils.isMobileNo(studentPhoneNumber) || studentPhoneNumber.length() < 11) {
                showToast("手机号格式不正确");
                return;
            }
        }
        ServerConnection.UpdateStudentInfo(BmobUser.getCurrentUser(StudentInfo.class).getObjectId(), studentName,
                "", studentSex, studentEmailAddress, studentPhoneNumber + "", studentYear, "",
                studentSpecialities, "", this);
    }

    /**
     * 设置为女性性别
     */
    private void setWomanSex() {
        mStudentSexWoman.setBackground(getResources().getDrawable(R.drawable.button_normal_blue_background));
        mStudentSexWoman.setTextColor(Color.WHITE);
        mStudentSexMan.setBackground(getResources().getDrawable(R.drawable.button_normal_white_background));
        mStudentSexMan.setTextColor(Color.BLACK);
    }

    /**
     * 设置为男性性别
     */
    private void setManSex() {
        mStudentSexMan.setBackground(getResources().getDrawable(R.drawable.button_normal_blue_background));
        mStudentSexMan.setTextColor(Color.WHITE);
        mStudentSexWoman.setBackground(getResources().getDrawable(R.drawable.button_normal_white_background));
        mStudentSexWoman.setTextColor(Color.BLACK);
    }

    /**
     * 选择用户头像
     */
    private void chooseUserHeaderImage() {
        final String[] Items = {"拍照", "从相册选择"};
        final ActionSheetDialog dialog = new ActionSheetDialog(StudentInfoActivity.this, Items, null);
        dialog.isTitleShow(false)//
                .lvBgColor(Color.WHITE)// dialog背景颜色
                .itemHeight(53)// item高度
                .itemTextColor(getResources().getColor(R.color.actionBarColor))// itme文字颜色
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                // 打开 "拍照"
                if (position == 0) {
                    openCameraActivity();
                }
                // 打开 "相册"
                else {
                    openAlbumActivity();
                }
            }
        });
    }

    /**
     * 打开 "拍照"
     */
    private void openCameraActivity() {
        if (!FileUtils.hasSdcard()) {
            showToast("没有找到SD卡，请检查SD卡是否接触良好");
            return;
        }
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", true);// 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, FLAG_PHOTO_REQUEST_TAKEPHOTO);
    }

    /**
     * 打开 "相册"
     */
    private void openAlbumActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, FLAG_PHOTO_REQUEST_GALLERY);
    }

    /**
     * 进行截图
     *
     * @param uri  Uri
     * @param size 大小
     */
    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, FLAG_PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FLAG_PHOTO_REQUEST_TAKEPHOTO: // 拍照
                File file = FileUtils.getFileByUri(this, photoUri);
                if (null != file) {
                    //设置头像
                    Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).
                            centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                            into(mStudentHeaderImage);
                    //上传头像
                    ServerConnection.uploadHeadImage(file, this);
                } else {
                    showToast("获取图片异常");
                }
                break;
            case FLAG_PHOTO_REQUEST_GALLERY:// 相册获取
                if (null != data) {
                    File file1 = FileUtils.getFileByUri(this, data.getData());
                    if (null != file1) {
                        //设置头像
                        Glide.with(this).load(file1).diskCacheStrategy(DiskCacheStrategy.ALL).
                                centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                                into(mStudentHeaderImage);
                        //上传头像
                        ServerConnection.uploadHeadImage(file1, this);
                    } else {
                        showToast("获取图片异常");
                    }
                } else {
                    showToast("获取图片异常");
                }
                break;
            case FLAG_PHOTO_REQUEST_CUT: // 接收处理返回的图片结果
                break;
        }

    }

    @Override
    public void getStudentInfoSuccess(StudentInfo studentInfo) {
        mDialog.dismiss();
        if (null != studentInfo) {
            //设置头像
            Glide.with(this).load(studentInfo.getStudentHeadImage()).diskCacheStrategy(DiskCacheStrategy.ALL).
                    centerCrop().placeholder(R.drawable.icon_passenger_man).error(R.drawable.icon_passenger_man).
                    into(mStudentHeaderImage);
            //姓名
            if (TextUtils.isEmpty(studentInfo.getStudentName())) {
                mStudentName.setText("");
                mStudentName.setHint("您的姓名...");
            } else {
                mStudentName.setText(studentInfo.getStudentName());
            }
            //性别
            if ("女".equals(studentInfo.getStudentSex())) {
                setWomanSex();
            } else {
                setManSex();
            }
            //邮箱
            if (TextUtils.isEmpty(studentInfo.getEmail())) {
                mStudentEmailAddress.setText("");
                mStudentEmailAddress.setHint("邮箱...");
            } else {
                mStudentEmailAddress.setText(studentInfo.getEmail());
            }
            //手机号
            if (TextUtils.isEmpty(studentInfo.getMobilePhoneNumber())) {
                mStudentPhoneNumber.setText("");
                mStudentPhoneNumber.setHint("手机号...");
            } else {
                mStudentPhoneNumber.setText(studentInfo.getMobilePhoneNumber());
            }
            //专业
            if (TextUtils.isEmpty(studentInfo.getStudentSpecialities())) {
                mStudentSpecialities.setText("");
                mStudentSpecialities.setHint("专业...");
            } else {
                mStudentSpecialities.setText(studentInfo.getStudentSpecialities());
            }
            //入学年份
            if (TextUtils.isEmpty(studentInfo.getStudentYear())) {
                mStudentYear.setText("");
                mStudentYear.setHint("入学年份...");
            } else {
                mStudentYear.setText(studentInfo.getStudentYear());
            }
        }
    }

    @Override
    public void getStudentInfoFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
        Logger.e("getStudentInfoFailure" + failureMessage);
    }

    @Override
    public void updateStudentInfoSuccess() {
        mDialog.dismiss();
        showToast("信息更新成功");
    }

    @Override
    public void updateStudentInfoFailure(int errorCode, String failureMessage) {
        mDialog.dismiss();
        if (errorCode == 206) {
            showToast("登陆信息已过期,不能修改个人信息,请重新登陆重试");
        } else {
            showToast(failureMessage);
        }
    }

    @Override
    public void uploadHeadImageSuccess() {
        mDialog.dismiss();
        showToast("头像更新成功");
    }

    @Override
    public void uploadHeadImageFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
        Logger.e("uploadHeadImageFailure" + failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
