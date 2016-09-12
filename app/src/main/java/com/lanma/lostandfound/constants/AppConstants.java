package com.lanma.lostandfound.constants;

import android.os.Environment;

public class AppConstants {

    /********************** requestCode常量，从0x000-0x099,共100个常量*********************/
    /**
     * 从添加"失物"/"招领"信息界面进入选择丢失物品类型界面
     */
    public static final int ThingTypeRequestCode = 0x000;
    /**
     * 从"失物"列表界面进入添加"失物信息"界面
     */
    public static final int LostInfoRequestCode = 0x001;

    /**
     * 从添加"失物"/"招领"信息界面进入选择丢失物品图片界面
     */
    public static final int ThingImageRequestCode = 0x002;
    /**
     * 从"招领"信息列表界面进入添加"招领信息"界面
     */
    public static int foundInfoRequestCode = 0x003;
    /**
     * 从"我的"失物招领信息列表界面进入添加详情界面
     */
    public static int myLostAndFoundInfoDetailRequestCode = 0x004;
    /////////////////////////////////// action常量////////////////////////////////
    /**
     * 我发布的失物信息
     */
    public static String MyLostInfoAction = "MyLostInfoAction";
    /**
     * 我发布的招领信息
     */
    public static String MyFoundInfoAction = "MyFoundInfoAction";

    /************************ Type *******************************/
    /**
     * 失物类型
     */
    public static String LostInfoType = "IT00001";
    /**
     * 招领类型
     */
    public static String FoundInfoType = "IT00002";
    /**
     * 网络类型的加载图片
     */
    public static final String NetWorkLoadType = "NetWorkLoadType";
    /**
     * 本地类型的加载图片
     */
    public static final String LocalLoadType = "LocalLoadType";
    /*****************************
     * 文件存储路径
     ******************************/
    public static String appDownloadPath = Environment.getExternalStorageDirectory().getPath() + "/LMZC_User.apk";


}
