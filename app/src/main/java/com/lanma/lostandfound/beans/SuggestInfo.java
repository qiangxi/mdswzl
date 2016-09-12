package com.lanma.lostandfound.beans;

import cn.bmob.v3.BmobObject;

/**
 * 作者 任强强 on 2016/9/10 14:56.
 */
public class SuggestInfo extends BmobObject {
    private String suggestContent;//建议
    private StudentInfo mStudentInfo;//反馈者信息

    public SuggestInfo() {
    }

    public String getSuggestContent() {
        return suggestContent;
    }

    public void setSuggestContent(String suggestContent) {
        this.suggestContent = suggestContent;
    }

    public StudentInfo getStudentInfo() {
        return mStudentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        mStudentInfo = studentInfo;
    }
}
