package com.lanma.lostandfound.beans;

import cn.bmob.v3.BmobUser;

/**
 * 作者 任强强 on 2016/9/1 21:29.
 */
public class StudentInfo extends BmobUser {
    private String studentName;//学生姓名
    private String studentSex;//学生性别
    private String studentAge;//学生年龄
    private String studentOfCollege;//学生所在学院
    private String studentYear;//学生入学年份
    private String studentSpecialities;//学生所学专业
    private String studentApartNumber;//学生所在公寓(eg:5号楼)
    private String studentHeadImage;//学生头像地址

    public StudentInfo() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public String getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    public String getStudentOfCollege() {
        return studentOfCollege;
    }

    public void setStudentOfCollege(String studentOfCollege) {
        this.studentOfCollege = studentOfCollege;
    }

    public String getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(String studentYear) {
        this.studentYear = studentYear;
    }

    public String getStudentSpecialities() {
        return studentSpecialities;
    }

    public void setStudentSpecialities(String studentSpecialities) {
        this.studentSpecialities = studentSpecialities;
    }

    public String getStudentApartNumber() {
        return studentApartNumber;
    }

    public void setStudentApartNumber(String studentApartNumber) {
        this.studentApartNumber = studentApartNumber;
    }

    public String getStudentHeadImage() {
        return studentHeadImage;
    }

    public void setStudentHeadImage(String studentHeadImage) {
        this.studentHeadImage = studentHeadImage;
    }
}
