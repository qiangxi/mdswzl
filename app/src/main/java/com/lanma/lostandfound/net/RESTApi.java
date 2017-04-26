package com.lanma.lostandfound.net;

import android.content.Context;

import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.presenter.DeleteCurrentInfoPresenter;
import com.lanma.lostandfound.presenter.GetStudentInfoPresenter;
import com.lanma.lostandfound.presenter.LoginPresenter;
import com.lanma.lostandfound.presenter.LostInfoListPresenter;
import com.lanma.lostandfound.presenter.MessageInfoPresenter;
import com.lanma.lostandfound.presenter.MyLostAndFoundPresenter;
import com.lanma.lostandfound.presenter.RegisterPresenter;
import com.lanma.lostandfound.presenter.ReleaseInfoPresenter;
import com.lanma.lostandfound.presenter.SuggestionPresenter;
import com.lanma.lostandfound.presenter.UpdateStudentInfoPresenter;
import com.lanma.lostandfound.presenter.UploadHeadImagePresenter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者 任强强 on 2016/10/21 09:49.
 * RESTApi方式请求
 */

public class RESTApi {
    /**
     * 请求地址
     */
    public static final String baseUrl = "https://api.bmob.cn";
    /**
     * 登陆,GET
     */
    public static final String login = "/1/login";
    /**
     * 注册 POST
     */
    public static final String register = "/1/users";
    /**
     * 查询用户信息 GET
     */
    public static final String getStudentInfo = "/1/users/objectId";
    /**
     * 更新用户信息 PUT
     */
    public static final String UpdateStudentInfo = "/1/users/objectId";
    /**
     * 上传头像 POST
     */
    public static final String uploadHeadImage = "/2/files/fileName";
    /**
     * 设置头像地址到StudentInfo PUT
     */
    public static final String setHeadImageUrlToStudentInfo = "/1/users/objectId";
    /**
     * 获取"失物"信息列表 GET
     */
    public static final String getLostFoundInfoList = "/1/classes/TableName/objectId";
    /**
     * 发布"失物"或"招领"信息 	POST
     */
    public static final String ReleaseLostInfo = "/1/classes/TableName";
    /**
     * 上传物品图片(1张-3张) 	POST
     */
    public static final String uploadThingImage = "/2/files/fileName";
    /**
     * 发布"失物"或"招领"信息  PUT
     */
    public static final String releaseLostOrFoundInfo = "/1/classes/TableName/objectId";
    /**
     * 提交反馈建议 POST
     */
    public static final String submitSuggestion = "/1/classes/TableName";
    /**
     * 获取消息列表 GET
     */
    public static final String getMessageInfo = "/1/classes/TableName/objectId";
    /**
     * 获取“我”发布的失物招领信息列表 GET
     */
    public static final String getMyLostAndFoundInfo = "/1/classes/TableName/objectId";
    /**
     * 删除一条“失物”或“招领”信息 DELETE
     */
    public static final String deleteCurrentInfo = "/1/classes/TableName/objectId";

    private RESTApi() {
    }

    private static Retrofit mRetrofit;
    private static RequestInterface sRequestInterface;

    private static Retrofit getRetrofit() {
        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return mRetrofit;
    }

    private static RequestInterface getApiService() {
        if (null == sRequestInterface) {
            sRequestInterface = getRetrofit().create(RequestInterface.class);
        }
        return sRequestInterface;
    }

    private static String getAppId() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("local.properties"));
            return properties.getProperty("appId");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getApiKey() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("local.properties"));
            return properties.getProperty("apiKey");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 登陆
     */
    public static void LoginAction(String userName, String password, final LoginPresenter presenter) {
        Observable<StudentInfo> loginObservable = getApiService().login(getAppId(), getApiKey(), userName, password);
        loginObservable.subscribe(new Observer<StudentInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                Logger.e("onSubscribe");
            }

            @Override
            public void onNext(StudentInfo info) {
                Logger.e("info:" + info.getCreatedAt());
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("Throwable:" + e.toString());
            }

            @Override
            public void onComplete() {
                Logger.e("onComplete");
            }
        });
    }

    /**
     * 注册
     */
    public static void RegisterAction(String userName, String password, final RegisterPresenter presenter) {

    }

    /**
     * 查询用户信息
     *
     * @param username 用户注册时用的用户名,即邮箱
     */
    public static void getStudentInfo(String username, final GetStudentInfoPresenter presenter) {

    }

    /**
     * 更新用户信息
     */
    public static void UpdateStudentInfo(String objectId, String studentName, String studentAge, String studentSex,
                                         String studentEmailAddress, String studentPhoneNumber, String studentYear,
                                         String studentCollege, String studentSpecialities, String studentApartmentNumber,
                                         final UpdateStudentInfoPresenter presenter) {

    }

    /**
     * 上传头像
     */
    public static void uploadHeadImage(File file, final UploadHeadImagePresenter presenter) {

    }

    /**
     * 设置头像地址到StudentInfo
     */
    private static void setHeadImageUrlToStudentInfo(String imageUrl, final UploadHeadImagePresenter presenter) {

    }


    /**
     * 获取"失物"信息列表
     */
    public static void getLostFoundInfoList(String lostInfoType, final int CurrentListSize, final LostInfoListPresenter presenter) {

    }

    /**
     * 发布"失物"或"招领"信息
     */
    public static void ReleaseLostInfo(Context context, String lostInfoType, String lostInfoDesc, String lostThingType, final ArrayList<String> list,
                                       String lostInfoWhere, String lostInfoThanksWay, String lostInfoPhoneNumber,
                                       String lostInfoDescDetail, StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {

    }

    /**
     * 上传物品图片(1张-3张)
     */
    private static void uploadThingImage(Context context, final String lostInfoType, final String lostInfoDesc, final String lostThingType, final ArrayList<String> mList,
                                         final String lostInfoWhere, final String lostInfoThanksWay, final String lostInfoPhoneNumber,
                                         final String lostInfoDescDetail, final StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {

    }

    /**
     * 发布"失物"或"招领"信息
     */
    private static void releaseLostInfo(String lostInfoType, String lostInfoDesc, String lostThingType, final String thingImagesUrl,
                                        String lostInfoWhere, String lostInfoThanksWay, String lostInfoPhoneNumber,
                                        String lostInfoDescDetail, StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {

    }


    /**
     * 提交反馈建议
     */
    public static void submitSuggestion(String suggest, StudentInfo currentUser, final SuggestionPresenter presenter) {

    }

    /**
     * 获取消息列表
     */
    public static void getMessageInfo(final MessageInfoPresenter presenter) {

    }

    /**
     * 获取“我”发布的失物招领信息列表
     */
    public static void getMyLostAndFoundInfo(StudentInfo currentUser, final int currentListSize, String infoType, final MyLostAndFoundPresenter presenter) {

    }

    /**
     * 删除一条“失物”或“招领”信息
     */
    public static void deleteCurrentInfo(String objectId, final int position, final DeleteCurrentInfoPresenter presenter) {

    }
}
