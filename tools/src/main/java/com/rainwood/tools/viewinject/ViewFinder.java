package com.rainwood.tools.viewinject;

import android.app.Activity;
import android.view.View;

/**
 * @Author: shearson
 * @time: 2019/11/27 15:25
 * @des: View 的findViewById 的辅助类
 */
public class ViewFinder {

    /**
     * 所保存的activity 实例
     */
    private Activity mActivit;

    /**
     * class 实例
     */
    private Class<?> mClass;

    public ViewFinder(Activity activity) {
        this.mActivit = activity;
        this.mClass = activity.getClass();
    }

    /**
     * 根据id 找到对应的view
     */
    public View finderViewById(int id){
        return mActivit != null ? mActivit.findViewById(id) : null;
    }

    /**
     * 返回对应的对象
     */
    public Object finderObject(){
        if (mActivit != null){
            return mActivit;
        }
        return null;
    }

    /**
     * 返回对应的Class 实例
     */
    public Class<?> finderClass(){
        return mClass;
    }
}
