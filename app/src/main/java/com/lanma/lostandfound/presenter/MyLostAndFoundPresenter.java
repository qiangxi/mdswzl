package com.lanma.lostandfound.presenter;

import com.lanma.lostandfound.beans.LostFoundInfo;

import java.util.List;

/**
 * 作者 任强强 on 2016/9/11 14:15.
 */
public interface MyLostAndFoundPresenter extends BasePresenter {
    void onRefreshMyLostAndFoundListSuccess(List<LostFoundInfo> list);

    void onRefreshMyLostAndFoundListFailure(String failureMessage);

    void onLoadMoreMyLostAndFoundListSuccess(List<LostFoundInfo> list);

    void onLoadMoreMyLostAndFoundListFailure(String failureMessage);
}
