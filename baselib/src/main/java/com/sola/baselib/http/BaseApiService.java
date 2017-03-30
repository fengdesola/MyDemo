package com.sola.baselib.http;

import android.support.v4.util.ArrayMap;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Ｔａｍｉｃ on 2016-07-08.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public interface BaseApiService {

//    public static final String Base_URL = "http://ip.taobao.com/";
//    /**
//     *普通写法
//     */
//    @GET("service/getIpInfo.php")
//    Observable<BaseResponse<IpResult>> getData(@Query("ip") String ip);
//
//
//    @GET("{url}")
//    Observable<BaseResponse<Object>> executeGet(
//            @Path("url") String url,
//            @QueryMap Map<String, String> maps
//    );
//
//
//    @POST("{url}")
//    Observable<ResponseBody> executePost(
//            @Path("url") String url,
//            //  @Header("") String authorization,
//            @QueryMap Map<String, String> maps);
//
//    @Multipart
//    @POST("{url}")
//    Observable<ResponseBody> upLoadFile(
//            @Path("url") String url,
//            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);
//
//    @POST("{url}")
//    Call<ResponseBody> uploadFiles(
//            @Path("url") String url,
//            @Path("headers") Map<String, String> headers,
//            @Part("filename") String description,
//            @PartMap() Map<String, RequestBody> maps);
//
//    @Streaming
//    @GET
//    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    @FormUrlEncoded
    @POST
    Call<String> post(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @FieldMap Map<String, String> maps);
    
    @POST
    Call<String> post(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Body Object body);
    
    @GET
    Call<String> get(
            @Url String url,
            @HeaderMap Map<String, String> headers);
    @GET
    Call<String> get(
            @Url String url);
    
    @POST
    Call<String> post(
            @Url String url,
            @Body Object body);
    
    
    
    @POST
    Flowable<String> post2(
            @Url String url,
            @HeaderMap ArrayMap<String, String> headers,
            @Body Object body);
    
    @POST
    Flowable<String> post2(
            @Url String url,
            @Body Object body);
    
    
    @FormUrlEncoded
    @POST
    Call<String> post(
            @Url String url,
            @FieldMap Map<String, String> maps);


    @Multipart
    @POST
    Call<String> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> files,
                             @HeaderMap Map<String, String> headers);
    
    @Multipart
    @POST
    Call<String> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> files,
                             @HeaderMap Map<String, String> headers,
                             @QueryMap Map<String, String> params);
    @Multipart
    @POST
    Call<String> uploadFile(@Url String url, @Part MultipartBody.Part file,
                            @HeaderMap Map<String, String> headers);
    
    @Multipart
    @POST
    Call<String> uploadFile(@Url String url, @Part MultipartBody.Part file,
                            @HeaderMap Map<String, String> headers,
                            @QueryMap Map<String, String> params);


    @Multipart
    @POST
    Call<String> uploadHeader(@Url String url, @Part MultipartBody.Part part,
                              @Part MultipartBody.Part part1,
                              @Part MultipartBody.Part part2,
                              @Part MultipartBody.Part part3,
                              @HeaderMap Map<String, String> headers);
    

}
