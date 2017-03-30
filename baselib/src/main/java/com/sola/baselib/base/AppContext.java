package com.sola.baselib.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/8.
 */
public class AppContext extends Application {
    private static Context sContext;
    private static Resources sResource;//直接获取Resources 放在application，可以减少调用getResources的次数
    private static String baseUrl;
    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
    }
    public static void initialize(Context context){
        sContext=context;
        sResource=sContext.getResources();
    }
    public static void initialize(Context context, String url){
        initialize(context);
        baseUrl = url;
    }


    public static Context getContext(){
        return sContext;
    }
    public static String getBaseUrl(){
        return baseUrl;
    }

    public static Resources resources(){
        return sResource;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
