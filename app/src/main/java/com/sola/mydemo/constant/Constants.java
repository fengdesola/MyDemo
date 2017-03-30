package com.sola.mydemo.constant;

import com.sola.mydemo.BuildConfig;

/**
 * Created by Administrator on 2017/3/9.
 */
public class Constants {

    public static final String HttpUrl = BuildConfig.HttpUrl +"/";
    public static final String HttpUrl2 = BuildConfig.HttpUrl2 +"/";
    public static final String HttpDownloadUrl = BuildConfig.HttpDownloadUrl + "/";

    //web地址
    public static final String HttpApiUrl2 = HttpUrl2 + "coms-web/";
    // api地址
    public static final String HttpApiUrl = HttpUrl + "bs-coms/";
    public static final String HttpDonwloadApiUrl = HttpDownloadUrl + "api/";

    //图片地址
    public static final String HttpImgUrl = HttpApiUrl+"upload/";


    public static final String App_Id = "coms.xiangtan";

    public static final String Role_Id = "patient";
    public static final String Json_Request = "*.jsonRequest";
    public static final String Head_Id = "X-Service-Id";
    public static final String Head_Token = "X-Access-Token";
    public static final String Content_Type = "Content-Type";
    public static final String Application_Json = "application/json";
}
