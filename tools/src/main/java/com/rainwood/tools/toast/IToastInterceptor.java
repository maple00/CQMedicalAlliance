package com.rainwood.tools.toast;

import android.widget.Toast;

/**
 * @Author: shearson
 * @time: 2019/11/29 13:59
 * @des: 土司拦截器接口
 */
public interface IToastInterceptor {

    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    boolean intercept(Toast toast, CharSequence text);
}
