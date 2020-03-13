package com.rainwood.tools.toast;

import android.widget.Toast;

/**
 * @Author: shearson
 * @time: 2019/11/29 14:08
 * @des: 土司默认拦截器
 */
public class ToastInterceptor implements IToastInterceptor {


    @Override
    public boolean intercept(Toast toast, CharSequence text) {
        // 如果是空对象或者空文本就进行拦截
        return text == null || "".equals(text.toString());
    }
}
