package com.lanma.lostandfound.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lanma.lostandfound.R;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.net.ServerConnection;
import com.lanma.lostandfound.presenter.LoginPresenter;
import com.lanma.lostandfound.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 任强强 on 2016/9/1 19:53.
 */
public class LoginFragment extends BaseFragment implements LoginPresenter {
    @Bind(R.id.loginUserName)
    EditText mLoginUserName;//用户名
    @Bind(R.id.loginUserNameLayout)
    TextInputLayout mLoginUserNameLayout;//用户名布局
    @Bind(R.id.loginPassword)
    EditText mLoginPassword;//密码
    @Bind(R.id.loginPasswordLayout)
    TextInputLayout mLoginPasswordLayout;//密码布局

    private LoadingDialog mDialog;

    public static LoginFragment newInstance(int position) {
        LoginFragment fragment = new LoginFragment();
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
        if (null == mDialog) {
            mDialog = new LoadingDialog(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_layout, null, false);
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

    @OnClick({R.id.login, R.id.forgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String userName = mLoginUserName.getText().toString();
                String password = mLoginPassword.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    showToast("用户名不可为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不可为空");
                    return;
                }
                if (!StringUtils.isEmail(userName)) {
                    showToast("邮箱格式不正确");
                    return;
                }
                ServerConnection.LoginAction(userName, password, this);
                break;
            //忘记密码
            case R.id.forgetPassword:
                // TODO: 2016/9/29 发送邮件,用于修改密码或重置密码
                break;
        }
    }

    @Override
    public void LoginSuccess() {
        showToast("登陆成功");
        mDialog.dismiss();
        getActivity().finish();
    }

    @Override
    public void LoginFailure(String failureMessage) {
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
