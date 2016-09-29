package com.lanma.lostandfound.beans;

/**
 * Created by qiang_xi on 2016/9/29 22:03 23:24.
 */

public class DrawerLayoutBean {
    private int resId;
    private String desc;
    private Class<?> clazz;
    private String intentAction;
    public DrawerLayoutBean() {
    }

    public DrawerLayoutBean(int resId, String desc) {
        this.resId = resId;
        this.desc = desc;
    }

    public DrawerLayoutBean(int resId, String desc, Class<?> clazz, String intentAction) {
        this.resId = resId;
        this.desc = desc;
        this.clazz = clazz;
        this.intentAction = intentAction;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getIntentAction() {
        return intentAction;
    }

    public void setIntentAction(String intentAction) {
        this.intentAction = intentAction;
    }
}
