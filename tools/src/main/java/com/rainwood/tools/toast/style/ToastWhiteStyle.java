package com.rainwood.tools.toast.style;

import android.content.Context;

/**
 * @anthor :  shearson
 * @time : 2019/11/29 14:51
 * @des: 默认白色样式实现
 */
public class ToastWhiteStyle extends ToastBlackStyle {

    public ToastWhiteStyle(Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor() {
        return 0XFFEAEAEA;
    }

    @Override
    public int getTextColor() {
        return 0XBB000000;
    }
}