package com.lanma.lostandfound.net;

import com.lanma.lostandfound.beans.StudentInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


/**
 * 作者 任强强 on 2016/10/21 09:53.
 */

public interface RequestInterface {

    @GET(RESTApi.login)
    Observable<StudentInfo> login(@Header("X-Bmob-Application-Id") String appId, @Header("X-Bmob-REST-API-Key") String apiKey, @Field(value = "1",encoded = true) String s,@Query("username") String username, @Query("password") String password);
}
