package com.lanma.lostandfound.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.RegisterPresenter;
import com.lanma.lostandfound.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 任强强 on 2016/9/1 19:53.
 */
public class RegisterFragment extends BaseFragment implements RegisterPresenter {
    @Bind(R.id.registerUserName)
    EditText mRegisterUserName;
    @Bind(R.id.registerPassword)
    EditText mRegisterPassword;
    @Bind(R.id.registerPasswordSecond)
    EditText mRegisterPasswordSecond;

    private LoadingDialog mDialog;
    public static RegisterFragment newInstance(int position) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null==mDialog){
            mDialog = new LoadingDialog(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.register)
    public void onClick() {
        String userName = mRegisterUserName.getText().toString();
        String password = mRegisterPassword.getText().toString();
        String passwordSecond = mRegisterPasswordSecond.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            showToast("用户名不可为空");
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordSecond)) {
            showToast("密码不可为空");
            return;
        }
        if (!StringUtils.isEmail(userName)) {
            showToast("邮箱格式不正确");
            return;
        }
        if (!TextUtils.equals(password, passwordSecond)) {
            showToast("两次输入的密码不一致");
            return;
        }
        ServerConnection.RegisterAction(userName, password, this);
    }

    @Override
    public void registerSuccess() {
        showToast("注册成功,快去登陆吧");
        mDialog.dismiss();
    }

    @Override
    public void registerFailure(String failureMessage) {
        mDialog.dismiss();
        showToast(failureMessage);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
    }
}
