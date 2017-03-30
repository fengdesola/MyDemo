package com.sola.mydemo;

import android.app.Application;

import com.sola.baselib.base.AppContext;
import com.sola.mydemo.constant.Constants;

/**
 * Created by Administrator on 2017/3/9.
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.initialize(getApplicationContext(), Constants.HttpApiUrl);
    }
}
