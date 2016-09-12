package com.lanma.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.adapter.GridImageAdapter;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.constants.AppConstants;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.ReleaseInfoPresenter;
import com.lanma.lostandfound.utils.ImageViewTintUtil;
import com.lanma.lostandfound.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobUser;

public class AddFoundInfoActivity extends BaseActivity implements ReleaseInfoPresenter {

    @Bind(R.id.foundInfoDesc)
    EditText mFoundInfoDesc;//描述
    @Bind(R.id.foundInfoType)
    TextView mFoundInfoType;//类型
    @Bind(R.id.foundInfoGridImages)
    GridView mFoundInfoGridImages;//图片
    @Bind(R.id.foundInfoWhere)
    EditText mFoundInfoWhere;//在哪发现
    @Bind(R.id.foundInfoThanksWay)
    EditText mFoundInfoThanksWay;//感谢方式
    @Bind(R.id.foundInfoPhoneNumber)
    EditText mFoundInfoPhoneNumber;//手机号
    @Bind(R.id.foundInfoDescDetail)
    EditText mFoundInfoDescDetail;//详细描述


    private GridImageAdapter mAdapter;
    private ArrayList<String> mList = new ArrayList<>();//用来装载图片路径的
    private ArrayList<String> followList = new ArrayList<>();//主要用来在初始化GridView时填充item的
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_found_info);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.foundInfoBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        for (int i = 0; i < 3; i++) {
            followList.add(null);
        }
        mAdapter = new GridImageAdapter(this, followList, AppConstants.LocalLoadType);
        mFoundInfoGridImages.setAdapter(mAdapter);
    }

    @OnClick({R.id.foundInfoBack, R.id.foundInfoType, R.id.releaseFoundInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回上一层
            case R.id.foundInfoBack:
                finish();
                break;
            //类型
            case R.id.foundInfoType:
                Intent intent = new Intent(this, ThingTypeActivity.class);
                startActivityForResult(intent, AppConstants.ThingTypeRequestCode);
                break;
            //发布招领信息
            case R.id.releaseFoundInfo:
                checkDataAndReleaseInfo();
                break;
        }
    }

    /**
     * 检查所填信息的合法性并发布信息
     */
    private void checkDataAndReleaseInfo() {
        //物品描述
        String lostInfoDesc = mFoundInfoDesc.getText().toString().trim();
        //物品类型
        String lostThingType = mFoundInfoType.getText().toString().trim();
        //在哪丢失
        String lostInfoWhere = mFoundInfoWhere.getText().toString().trim();
        //感谢方式
        String lostInfoThanksWay = mFoundInfoThanksWay.getText().toString().trim();
        //联系手机号
        String lostInfoPhoneNumber = mFoundInfoPhoneNumber.getText().toString().trim();
        //详细描述
        String lostInfoDescDetail = mFoundInfoDescDetail.getText().toString().trim();
        if (TextUtils.isEmpty(lostInfoDesc)) {
            showSnackBar("物品描述不可为空");
            return;
        }
        if (TextUtils.isEmpty(lostThingType)) {
            showSnackBar("物品类型不可为空");
            return;
        }
        if (null == mList || mList.size() == 0) {
            showSnackBar("请至少选择一张图片");
            return;
        }
        if (TextUtils.isEmpty(lostInfoWhere)) {
            showSnackBar("在哪捡到不可为空");
            return;
        }
        if (TextUtils.isEmpty(lostInfoPhoneNumber)) {
            showSnackBar("手机号码不可为空");
            return;
        }
        if (!StringUtils.isMobileNo(lostInfoPhoneNumber)) {
            showSnackBar("手机号码不合法");
            return;
        }
        if (TextUtils.isEmpty(lostInfoDescDetail) || lostInfoDescDetail.length() < 15) {
            showSnackBar("详细描述不可少于15个字");
            return;
        }
        MobclickAgent.onEvent(this,"releaseFoundInfo");
        ServerConnection.ReleaseLostInfo(this, AppConstants.FoundInfoType, lostInfoDesc, lostThingType, mList, lostInfoWhere, lostInfoThanksWay,
                lostInfoPhoneNumber, lostInfoDescDetail, BmobUser.getCurrentUser(StudentInfo.class), this);
    }

    @OnItemClick(R.id.foundInfoGridImages)
    public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, SelectPictureActivity.class);
        intent.putStringArrayListExtra("ImageList", checkListData());
        startActivityForResult(intent, AppConstants.ThingImageRequestCode);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (AppConstants.ThingImageRequestCode == requestCode) {
                mList.clear();
                mList = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                mAdapter = new GridImageAdapter(this, mList, AppConstants.LocalLoadType);
                mFoundInfoGridImages.setAdapter(mAdapter);
            }
            if (AppConstants.ThingTypeRequestCode == requestCode) {
                String thingType = data.getStringExtra("thingType");
                mFoundInfoType.setText(thingType);
            }
        }
    }

    @Override
    public void releaseSuccessful() {
        mDialog.dismiss();
        //添加一个空的intent的目的是为了防止每次从这个界面回到主界面时都要刷新数据
        //要达到的效果是只有用户发布信息成功之后回到主界面才会刷新,其他情况一概不刷新,节省性能
        Intent intent = new Intent();
        setResult(AppConstants.foundInfoRequestCode, intent);
        finish();
    }

    @Override
    public void releaseFailure(String failureMessage) {
        mDialog.dismiss();
        showSnackBar(failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }
}
