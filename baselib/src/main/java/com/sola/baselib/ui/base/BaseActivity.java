package com.sola.baselib.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.sola.baselib.widget.dialog.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by Administrator on 2017/3/9.
 */
public class BaseActivity extends RxAppCompatActivity {

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog();
    }

    public void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
    public void showToast(@StringRes int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }
    
    public void showLoadingDialog(){
        showLoadingDialog(null);
    }
    public void showLoadingDialog(String msg){
        loadingDialog.build(this).show(msg);
    }
    public void dismissLoadingDialog(){
        loadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        super.onDestroy();
    }
}
