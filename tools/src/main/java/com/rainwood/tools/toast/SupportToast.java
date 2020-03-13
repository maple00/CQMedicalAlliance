package com.rainwood.tools.toast;

import android.app.Application;

/**
 * @Author: shearson
 * @time: 2019/11/29 14:04
 * @des: 土司无通知栏权限兼容
 */
public class SupportToast extends BaseToast {

    /** 吐司弹窗显示辅助类 */
    private final ToastHelper mToastHelper;

    SupportToast(Application application) {
        super(application);
        mToastHelper = new ToastHelper(this, application);
    }

    @Override
    public void show() {
        // 显示吐司
        mToastHelper.show();
    }

    @Override
    public void cancel() {
        // 取消显示
        mToastHelper.cancel();
    }
}
