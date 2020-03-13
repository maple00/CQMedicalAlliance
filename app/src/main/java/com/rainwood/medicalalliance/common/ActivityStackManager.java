package com.rainwood.medicalalliance.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:36
 * @des: Activity 管理类
 */
public final class ActivityStackManager implements Application.ActivityLifecycleCallbacks {

    private Stack<Activity> stack;

    private ActivityStackManager() {
        stack = new Stack<>();
    }

    public static ActivityStackManager getInstance() {
        //
        return Instance.INSTANCE;
    }

    private static class Instance {
        static ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    // App 绑定入栈
    public void register(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    // App 栈解绑
    public void unRegister(Application app) {
        app.unregisterActivityLifecycleCallbacks(this);
    }

    /**
     * @param activity 需要添加进栈管理的activity
     */
    public void addActivity(Activity activity) {
        stack.add(activity);
    }

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return
     */
    public boolean removeActivity(Activity activity) {
        return stack.remove(activity);
    }

    /**
     * @param activity 查询指定activity在栈中的位置，从栈顶开始
     * @return
     */
    public int searchActivity(Activity activity) {
        return stack.search(activity);
    }

    /**
     * @param activity 将指定的activity从栈中删除然后finish()掉
     */
    public void finishActivity(Activity activity) {
        stack.pop().finish();
    }

    /**
     * @param activity 将指定类名的activity从栈中删除并finish()掉
     */
    public void finishActivityClass(Class<Activity> activity) {
        if (activity != null) {
            Iterator<Activity> iterator = stack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (next.getClass().equals(activity)) {
                    iterator.remove();
                    finishActivity(next);
                }
            }
        }
    }

    /**
     * 销毁所有的activity
     */
    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(@Nullable Activity activity) {

    }

    @Override
    public void onActivityResumed(@Nullable Activity activity) {

    }

    @Override
    public void onActivityPaused(@Nullable Activity activity) {

    }

    @Override
    public void onActivityStopped(@Nullable Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@Nullable Activity activity, @Nullable Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@Nullable Activity activity) {
        removeActivity(activity);
    }
}
