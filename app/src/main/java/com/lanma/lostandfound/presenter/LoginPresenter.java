package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/1 20:47.
 */
public interface LoginPresenter extends BasePresenter {
    void LoginSuccess();

    void LoginFailure(String failureMessage);
}
