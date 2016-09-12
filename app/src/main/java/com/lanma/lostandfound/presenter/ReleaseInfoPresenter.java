package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/5 11:34.
 */
public interface ReleaseInfoPresenter extends BasePresenter {
    /**
     * 发布成功
     */
    void releaseSuccessful();

    /**
     * 发布失败
     */
    void releaseFailure(String failureMessage);
}
