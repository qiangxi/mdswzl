package com.lanma.lostandfound.presenter;

import com.lanma.lostandfound.beans.LostFoundInfo;

import java.util.List;

/**
 * 作者 任强强 on 2016/9/2 15:23.
 */
public interface FoundInfoListPresenter extends BasePresenter {
    void onRefreshFoundInfoListSuccess(List<LostFoundInfo> list);

    void onRefreshFoundInfoListFailure(String failureMessage);

    void onLoadMoreFoundInfoListSuccess(List<LostFoundInfo> list);

    void onLoadMoreFoundInfoListFailure(String failureMessage);
}
