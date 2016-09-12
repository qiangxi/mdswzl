package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/11 15:27.
 */
public interface DeleteCurrentInfoPresenter {
    void onDeleting();

    void onDeleteSuccess(int position);

    void onDeleteFailure(String failureMessage);
}
