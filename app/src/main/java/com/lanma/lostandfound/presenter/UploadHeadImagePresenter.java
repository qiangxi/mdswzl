package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/2 10:20.
 */
public interface UploadHeadImagePresenter extends BasePresenter {
    void uploadHeadImageSuccess();

    void uploadHeadImageFailure(String failureMessage);
}
