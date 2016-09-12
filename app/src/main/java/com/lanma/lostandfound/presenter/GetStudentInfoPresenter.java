package com.lanma.lostandfound.presenter;

import com.lanma.lostandfound.beans.StudentInfo;

/**
 * 作者 任强强 on 2016/9/2 10:20.
 */
public interface GetStudentInfoPresenter extends BasePresenter {
    void getStudentInfoSuccess(StudentInfo studentInfo);
    void getStudentInfoFailure(String failureMessage);
}
