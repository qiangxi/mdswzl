package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/10 14:59.
 */
public interface SuggestionPresenter extends BasePresenter {
    /**
     * 发布成功
     */
    void SuggestSuccessful();

    /**
     * 发布失败
     */
    void SuggestFailure(String failureMessage);
}
