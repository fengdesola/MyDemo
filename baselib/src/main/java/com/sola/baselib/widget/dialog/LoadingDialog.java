package com.sola.baselib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sola.baselib.R;
import com.sola.baselib.base.AppContext;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Administrator on 2017/3/28.
 */
public class LoadingDialog {
    private Dialog dialog;
    private TextView tvMessage;
    private AVLoadingIndicatorView avi;
    
    public LoadingDialog(){
        
    }
    
    public LoadingDialog build(Context context){
        if (dialog == null) {
            dialog = new Dialog(context, R.style.alertDialogTheme);

            dialog.setContentView(R.layout.dialog_loading);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            tvMessage = (TextView) dialog.findViewById(R.id.tv);
            avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
        }
        return this;
    }
    

    public void show() {
        show(null);
    }
    public void showL() {
        show(AppContext.getContext().getResources().getString(R.string.loading));
    }

    public void show(String message) {
        if(dialog != null && !dialog.isShowing()) {
            if (TextUtils.isEmpty(message)) {
                tvMessage.setVisibility(View.GONE);
            } else {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(message);
            }
            avi.smoothToShow();
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            avi.smoothToHide();
        }
    }
}
