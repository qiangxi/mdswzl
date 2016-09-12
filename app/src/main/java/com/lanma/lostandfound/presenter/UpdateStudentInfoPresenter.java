package com.lanma.lostandfound.presenter;

/**
 * 作者 任强强 on 2016/9/2 10:20.
 */
public interface UpdateStudentInfoPresenter extends BasePresenter {
    void updateStudentInfoSuccess();

    void updateStudentInfoFailure(int errorCode, String failureMessage);
}
