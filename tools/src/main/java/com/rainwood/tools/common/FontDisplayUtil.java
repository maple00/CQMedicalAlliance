package com.rainwood.tools.common;

import android.content.Context;

/**
 * @Author: a797s
 * @Date: 2019/12/13 11:01
 * @Desc: dp，sp，px 之间的相互转换工具类
 */
public class FontDisplayUtil {

    /**
     * 将 PX 转换成dip或dp值，保证尺寸不变
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 将 dip或者dp 值转换成px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换成sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp 值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5f);
    }


}
