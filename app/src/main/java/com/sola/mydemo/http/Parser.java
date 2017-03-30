package com.sola.mydemo.http;

import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.sola.baselib.base.AppContext;
import com.sola.baselib.model.http.ResultModel;
import com.sola.baselib.model.http.Statue;
import com.sola.baselib.util.AppLogger;


/**
 * 解析
 * Created by Administrator on 2016/10/24.
 */
public class Parser {
    final static String CODE = "code";
    final static String DATA = "body";
    final static String MSG = "msg";
    final static String PRO = "properties";

    private static Parser mParser;

    public enum TYPE {
        LIST, OBJECT, BASE
    }

    private Parser() {

    }

    private static class SingletonHolder {
        private final static Parser INSTANCE = new Parser();
    }

    public static Parser getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public ResultModel parserData(String json, Class clazz, TYPE type) {
        if(json != null) {
            Logger.d(json);
            Logger.json(json);
        }else{
            AppLogger.e(json);
        }
        ResultModel mList = new ResultModel();
        if (null != json) {
            try {

                JSONObject ob = JSON.parseObject(json);
                ResultModel fList = null;
                int code = getCode(ob);
                fList = doFilter(code, ob);
                if (null != fList) {
                    return fList;
                }
                if (code == 200) {
                    mList.statue = Statue.SUCCESS;
//                    if (ob.containsKey(PRO)) {
//                        mList.pro = JSON.parseObject(ob.getString(PRO), PropertyVo.class);
//                    }
                    if (ob.containsKey(DATA)) {

                        switch (type) {
                            case LIST:
                                mList.data = JSON.parseArray(ob.getString(DATA), clazz);
                                break;
                            case OBJECT:
                                mList.data = JSON.parseObject(ob.getString(DATA), clazz);
                                break;
                            case BASE:
                                mList.data = ob.get(DATA);
                                break;
                        }

                        return mList;
                    } else {
                        mList.message = "数据为空";
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (ob.containsKey(MSG)) {
                        mList.message = ob.getString(MSG);
                    }

                    

                    return mList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
//                mList.data = data;
                return mList;
            }
        }
        return mList;
    }

    public ResultModel parserSpData(String json, Class clazz, String dataId, TYPE type) {
        if(json != null) {
            Logger.d(json);
            Logger.json(json);
        }else{
            AppLogger.e(json);
        }
        ResultModel mList = new ResultModel();
        if (null != json) {
            try {

                JSONObject ob = JSON.parseObject(json);

                mList.statue = Statue.SUCCESS;
                if (ob.containsKey(dataId)) {

                    switch (type) {
                        case LIST:
                            mList.data = JSON.parseArray(ob.getString(dataId), clazz);
                            break;
                        case OBJECT:
                            mList.data = JSON.parseObject(ob.getString(dataId), clazz);
                            break;
                        case BASE:
                            mList.data = ob.get(dataId);
                            break;
                    }

                    return mList;
                } else {
                    mList.message = "数据为空";
                    return mList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
//                mList.data = data;
                return mList;
            }
        }
        return mList;
    }


    private int getCode(JSONObject ob) {
        if (null != ob) {
            if (ob.containsKey(CODE))
                return ob.getIntValue(CODE);
            return -88;
        } else {
            return -88;
        }
    }

    private ResultModel doFilter(int code, JSONObject ob) {
        ResultModel mList = null;
        if (code == -6) {
            mList = new ResultModel();
            mList.statue = Statue.FORBID;
            if (ob.containsKey(MSG)) {
                mList.message = ob.getString(MSG);
            }
            return mList;
        }
//        if (code == -500) {
//            PushInfo info = new PushInfo();
//            info.description = "您的账号在其他设备上登陆,请重新登录!";
//            info.title = "提示";
//            Intent logoutIntent = new Intent(Constants.Logout_ACTION);
//            logoutIntent.putExtra("pushInfo", info);
//            AppContext.getContext().sendBroadcast(logoutIntent);
//
//            mList = new ResultModel();
//            mList.statue = Statue.DEVICEID_ERROR;
//            return mList;
//        }
//        if (code == -501) {
//            PushInfo info = new PushInfo();
//            info.description = "账号验证失败，请重新登录!";
//            info.title = "提示";
//            info.login = 1;
//            Intent logoutIntent = new Intent(Constants.Logout_ACTION);
//            logoutIntent.putExtra("pushInfo", info);
//            AppContext.getContext().sendBroadcast(logoutIntent);
//
//            mList = new ResultModel();
//            mList.statue = Statue.DEVICEID_ERROR;
//            return mList;
//        }
        return mList;
    }
}
