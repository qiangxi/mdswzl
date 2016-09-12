package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/1 21:11.
 */
public interface RegisterPresenter extends BasePresenter {
    void registerSuccess();

    void registerFailure(String failureMessage);
}
