package com.rainwood.medicalalliance.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.security.rp.RPSDK;
import com.rainwood.medicalalliance.common.ActivityStackManager;
import com.rainwood.medicalalliance.ui.activity.CrashActivity;
import com.rainwood.medicalalliance.ui.activity.SplashActivity;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Application 基类
 */
public class BaseApplication extends Application {

    /**
     * BaseApplication对象
     */
    public static BaseApplication app;

    private Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appContext = getInstance().getApplicationContext();
        // initActivity 初始化Activity 栈管理
        initActivity();
        // 初始化三方的框架
        initSDK();

        // 初始化工具类
        initTools();
    }

    /**
     * 初始化活动
     */
    private void initActivity() {
        ActivityStackManager.getInstance().register(this);
    }

    /**
     * 创建Application 对象
     */
    public static BaseApplication getInstance() {
        return Instance.INSTANCE;
    }

    private static class Instance {
        static BaseApplication INSTANCE = new BaseApplication();
    }


    /**
     * 初始化一些三方框架
     */
    private void initSDK() {
        // 初始化实人认证SDK(阿里云)
        RPSDK.initialize(appContext);

    }

    /**
     * 初始化工具类
     */
    private void initTools() {
        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);

        // 本地异常
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(SplashActivity.class)
                // 错误的 Activity
                .errorActivity(CrashActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();
    }

    /**
     * 判断网络环境
     */
    public boolean isDetermineNetwork() {
        return true;
    }

    /**
     * debug模式
     */
    public boolean isDebug(){
        return true;
    }
}
