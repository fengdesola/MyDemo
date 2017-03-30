package com.sola.baselib.ui.base;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.sola.baselib.base.AppContext;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by Administrator on 2017/3/9.
 */
public class BaseFragment extends RxFragment {

    public void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
            Toast.makeText(AppContext.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
    public void showToast(@StringRes int resId) {
        Toast.makeText(AppContext.getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
