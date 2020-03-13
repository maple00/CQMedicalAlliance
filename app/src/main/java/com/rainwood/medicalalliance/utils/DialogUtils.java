package com.rainwood.medicalalliance.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.common.Contants;

import java.util.Objects;

/**
 * @Author: sxs797
 * @Date: 2020/1/2 17:36
 * @Desc: Dialog 工具类
 */
public final class DialogUtils {

    private static Dialog dialog;

    public DialogUtils(Context context, String tips) {
        dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvMsg = dialog.findViewById(R.id.tv_wait_message);
        tvMsg.setText(tips);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }


    // 写一个加载监听回调，显示网络不好的情况
}
