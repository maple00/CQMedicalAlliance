package com.rainwood.tools.toast;

import android.widget.Toast;

/**
 * @Author: shearson
 * @time: 2019/11/29 14:01
 * @des: 土司处理策略
 */
public interface IToastStrategy {

    /**
     * 短吐司显示的时长
     */
    int SHORT_DURATION_TIMEOUT = 2000;
    /**
     * 长吐司显示的时长
     */
    int LONG_DURATION_TIMEOUT = 3500;

    /**
     * 绑定 Toast 对象
     */
    void bind(Toast toast);

    /**
     * 显示 Toast
     */
    void show(CharSequence text);

    /**
     * 取消 Toast
     */
    void cancel();
}
