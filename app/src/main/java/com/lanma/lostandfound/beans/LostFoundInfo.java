package com.lanma.lostandfound.beans;

import cn.bmob.v3.BmobObject;

/**
 * 作者 任强强 on 2016/9/2 08:59.
 */
public class LostFoundInfo extends BmobObject {
    private String infoType;//失物类型/招领类型
    private String thingDesc;//物品描述
    private String thingImageUrl;//物品图片地址
    private String thingType;//物品类型(eg:身份证/书本/学生证等)
    private String thingWhereFound;//在哪里捡到的(发布招领信息时用到)
    private String thingMark;//物品备注信息
    private String studentPhoneNumber;//发布此信息的学生手机号
    private String studentThanksWay;//酬谢方式
    private StudentInfo studentInfo; //学生信息

    public LostFoundInfo() {
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getThingDesc() {
        return thingDesc;
    }

    public void setThingDesc(String thingDesc) {
        this.thingDesc = thingDesc;
    }

    public String getThingImageUrl() {
        return thingImageUrl;
    }

    public void setThingImageUrl(String thingImageUrl) {
        this.thingImageUrl = thingImageUrl;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public String getThingWhereFound() {
        return thingWhereFound;
    }

    public void setThingWhereFound(String thingWhereFound) {
        this.thingWhereFound = thingWhereFound;
    }

    public String getThingMark() {
        return thingMark;
    }

    public void setThingMark(String thingMark) {
        this.thingMark = thingMark;
    }

    public String getStudentPhoneNumber() {
        return studentPhoneNumber;
    }

    public void setStudentPhoneNumber(String studentPhoneNumber) {
        this.studentPhoneNumber = studentPhoneNumber;
    }

    public String getStudentThanksWay() {
        return studentThanksWay;
    }

    public void setStudentThanksWay(String studentThanksWay) {
        this.studentThanksWay = studentThanksWay;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

}
