package com.rainwood.medicalalliance.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.common.ActivityStackManager;
import com.rainwood.medicalalliance.common.StatusManager;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.viewinject.ViewBind;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:23
 * @des: Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    //获取TAG的fragment名称
//    protected final String TAG = this.getClass().getSimpleName();
    protected final String TAG = "sxs";

    // 需要重写OnCreate的Activity调用
    private Bundle savedInstanceState;

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            //设置坚屏 一定要放到try catch里面，否则会崩溃
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   //禁止横屏
        } catch (Exception e) {
        }
        super.onCreate(savedInstanceState);
        // 初始化布局
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        this.savedInstanceState = savedInstanceState;
        // 初始化数据
        init();
    }

    protected void init() {
        // View 绑定
        ViewBind.inject(this);
        // 沉浸式状态栏
        setStatusBar();
        // 初始视图
        initView();
        // 初始化数据
        initData();
        // 将activity 入栈
        ActivityStackManager.getInstance().addActivity(this);
    }

    /**
     * 状态栏适配
     * 魅族，小米适配需要单独设置
     * setFUI
     * setMiUI
     */
    private void setStatusBar() {
        //  当fistsSystemWindows 设置为true时，会在屏幕的最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        // 一般的手机的状态栏文字和图标都是白色的，如果应用是纯白的，则会导致状态栏文字看不清
        // 如果是纯白的状态栏背景，则设置状态栏使用深灰色图标风格，否则可以选择性注释掉if中的内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        /*
         * 设置状态栏透明，黑色字体
         */
//        StatusBarUtil.setTranslucentStatus(this);
//        StatusBarUtil.setStatusBarDarkTheme(this, true);

           /*
           白色字体
            */
//           StatusBarUtil.setStatusBarDarkTheme(this, false);

        /*
        设置白色字体，其他背景
         */
//        StatusBarUtil.setStatusBarDarkTheme(this, false);
//        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#58C087"));

    }

    /**
     * 布局ID
     *
     * @return 布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 获取当前 Activity 对象
     */
    public BaseActivity getActivity() {
        return this;
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * 延迟执行
     */
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public final Object mHandlerToken = hashCode();

    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime(r, mHandlerToken, uptimeMillis);
    }

    @Override
    protected void onDestroy() {
        ActivityStackManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }


    private final StatusManager mStatusManager = new StatusManager();

    /**
     * 显示加载中
     */
    public void showLoading() {
        mStatusManager.showLoading(this);
    }

    public void showLoading(@StringRes int id) {
        mStatusManager.showLoading(this, getString(id));
    }

    public void showLoading(CharSequence text) {
        mStatusManager.showLoading(this, text);
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        mStatusManager.showComplete();
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        mStatusManager.showEmpty(getContentView());
    }

    /**
     * 显示错误提示
     */
    public void showError() {
        mStatusManager.showError(getContentView());
    }

    /**
     * 显示自定义提示
     */
    public void showLayout(@DrawableRes int drawableId, @StringRes int stringId) {
        mStatusManager.showLayout(getContentView(), drawableId, stringId);
    }

    public void showLayout(Drawable drawable, CharSequence hint) {
        mStatusManager.showLayout(getContentView(), drawable, hint);
    }

    private static int rebackFlag = -1;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       /* if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if (rebackFlag < 0){
                toast("再按一次退出到桌面");
                rebackFlag++;
                return false;
            }else {
                rebackFlag = -1;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }
        }*/
        return super.onKeyDown(keyCode, event);
    }

}