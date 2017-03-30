package com.sola.baselib.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.sola.baselib.base.AppContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * RetrofitClient
 * Created by Tamic on 2016-06-15.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    private static String baseUrl = AppContext.getBaseUrl();
    private static Context mContext = AppContext.getContext();

    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                mContext, baseUrl);
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

   private RetrofitClient() {
   }

    private RetrofitClient(Context context, String url) {

        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .retryOnConnectionFailure(false)//不重连
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, DEFAULT_TIMEOUT, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为15s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        
        createBaseApi();

    }

//   /**
//     * ApiBaseUrl
//     *
//     * @param newApiBaseUrl
//     */
//    public static void changeApiBaseUrl(String newApiBaseUrl) {
//        baseUrl = newApiBaseUrl;
//        builder = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(baseUrl);
//    }
//
//    /**
//     *addcookieJar
//     */
//    public static void addCookie() {
//        okHttpClient.newBuilder().cookieJar(new NovateCookieManger(mContext)).build();
//        retrofit = builder.client(okHttpClient).build();
//    }
//
    /**
     * ApiBaseUrl
     *
     * @param newApiHeaders
     */
//    public static void changeApiHeader(Map<String, String> newApiHeaders) {
//
//        okHttpClient.newBuilder().addInterceptor(new BaseInterceptor(newApiHeaders)).build();
//        builder.client(httpClient.build()).build();
//    }

    /**
     * create BaseApi  default ApiManager
     * @return ApiManager
     */
    private RetrofitClient createBaseApi() {
        if(apiService == null)
            apiService = create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
    
    public Flowable<String> post2(String url, ArrayMap<String, String> headers, Object body){
        if(headers == null)
            return apiService.post2(url, body);
        return apiService.post2(url,headers, body);
    }
    public Call<String> post(String url, Map<String, String> headers, Object body){
        if(headers == null)
            return apiService.post(url, body);
        return apiService.post(url,headers, body);
    }
    public Call<String> get(String url, Map<String, String> headers){
        if(headers == null)
            return apiService.get(url);
        return apiService.get(url,headers);
    }
    
    public Call<String> postPicture(String url, String[] filePaths, String serviceFileName,
                                    Map<String, String> headers, Map<String, String> params){
        Map<String, RequestBody> bodyMap = new HashMap<>();
        if (null != filePaths && filePaths.length > 0) {
            for (int i = 0; i < filePaths.length; i++) {
                if (!TextUtils.isEmpty(filePaths[i])) {
                    File file = new File(filePaths[i]);
                    RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
                    bodyMap.put(serviceFileName + "\"; filename=\"" + file.getName()+"\"", photo);
                }
            }

            
        }
        if(params != null) {
            return apiService.uploadFiles(url, bodyMap, headers, params);
        }else{
            return apiService.uploadFiles(url, bodyMap, headers);
        }
    }
    public Call<String> postHeader(String url, String filePath, String serviceFileName,
                                   Map<String, String> headers, Map<String, String> params){
        
        
//        RequestBody requestBody = null;
        MultipartBody.Part part = null;
        MultipartBody.Part part1 = null;
        MultipartBody.Part part2 = null;
        MultipartBody.Part part3 = null;
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
            part = MultipartBody.Part.createFormData(serviceFileName, file.getName(), photo);
            part1 = MultipartBody.Part.createFormData("catalog", "avatar");
            part2 = MultipartBody.Part.createFormData("path", "avatar");
            part3 = MultipartBody.Part.createFormData("mode", "31");

        }

        return apiService.uploadHeader(url, part, part1, part2, part3, headers);
    }
    

}
