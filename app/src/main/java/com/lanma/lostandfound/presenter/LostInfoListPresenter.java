package com.lanma.lostandfound.presenter;

import com.lanma.lostandfound.beans.LostFoundInfo;

import java.util.List;

/**
 * 作者 任强强 on 2016/9/2 15:23.
 */
public interface LostInfoListPresenter extends BasePresenter {
    void onRefreshLostInfoListSuccess(List<LostFoundInfo> list);

    void onRefreshLostInfoListFailure(String failureMessage);

    void onLoadMoreLostInfoListSuccess(List<LostFoundInfo> list);

    void onLoadMoreLostInfoListFailure(String failureMessage);
}
