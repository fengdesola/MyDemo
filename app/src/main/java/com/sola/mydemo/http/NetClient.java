package com.sola.mydemo.http;


import android.support.v4.util.ArrayMap;

import com.sola.baselib.http.RetrofitClient;
import com.sola.baselib.model.http.ResultModel;
import com.sola.baselib.util.NetworkUtil;
import com.sola.mydemo.ui.base.BaseActivity;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/1.
 */
public class NetClient {
    
    public static void post(BaseActivity activity, String url, ArrayMap<String, String> header, Object body,
                            final Class clazz,
                            final Listener listener){

        if(!NetworkUtil.isNetworkAvailable()){
            activity.showToast("网络未打开");
            return;
        }
        
        RetrofitClient.getInstance().post2(url, header, body)
                .map(new Function<String, ResultModel>() {
                    @Override
                    public ResultModel apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        return Parser.getInstance().parserData(s, clazz);
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {//subscribe()调用后而且在事件发送前执行(默认情况下subcribe发生的线程决定，可以通过最近的跟在后面的subscribeOn指定)
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Subscription subscription) throws Exception {
                        if(listener != null)
                            listener.onPrepare();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//指定doOnSubscribe的线程
                .compose(RxLifecycle.bindUntilEvent(activity.lifecycle(), ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        ResultModel result = (ResultModel) o;
                        if(listener != null)
                            listener.onSuccess(result);

                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        if(listener != null) 
                            listener.onFaile(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    
    public interface Listener<T>{
        void onPrepare();
        void onSuccess(T result);
        void onFaile(Throwable t);
    }
}
