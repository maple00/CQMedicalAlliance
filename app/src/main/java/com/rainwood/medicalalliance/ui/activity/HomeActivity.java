package com.rainwood.medicalalliance.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.helper.BottomNavigationViewHelper;
import com.rainwood.medicalalliance.ui.fragment.HomeFragment;
import com.rainwood.medicalalliance.ui.fragment.HospitalDescFragment;
import com.rainwood.medicalalliance.ui.fragment.VIPCenterFragment;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/6 10:10
 * @Desc: 首页
 */
public final class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @ViewById(R.id.bottomNavigationView)
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        // 设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置导航栏监听器
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        //设置默认选择的导航栏子项tab_one即首页
        mBottomNavigationView.setSelectedItemId(R.id.tab_one);
        // 设置底部的字体颜色
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.navigation_menu_item_color);
        mBottomNavigationView.setItemTextColor(csl);
        //取消导航栏子项图片的颜色覆盖
        mBottomNavigationView.setItemIconTintList(null);
        // 取消底部导航栏的动画效果, 如果sdk版本小于28则调用方法，大于28则设置属性
        // msg：调用设置没有效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 设置属性
            mBottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            mBottomNavigationView.setItemHorizontalTranslationEnabled(false);
        } else {
            BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        }
    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     * 处理导航栏子项的点击事件
     *
     * @param menuItem 导航栏子项
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // 获取点击位置以及对应的id
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.tab_one:
                replaceFragment(new HomeFragment());   //id 为tab_one 则第一项被点击，用Fragment替换空的Fragment
                menuItem.setChecked(true);
                break;
            case R.id.tab_two:
                replaceFragment(new HospitalDescFragment());
                menuItem.setChecked(true);
                break;
            case R.id.tab_three:
                replaceFragment(new VIPCenterFragment());
                menuItem.setChecked(true);
                break;
        }

        return false;
    }

    // 替换Fragment 的方法
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_empty, fragment);
        transaction.commit();
    }

    private static int rebackFlag = -1;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if (rebackFlag < 0) {
                toast("再按一次退出到桌面");
                rebackFlag++;
                return false;
            } else {
                rebackFlag = -1;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 当BottonNavigationBar 的items大于三个时
     * 取消这个底部导航栏的动画效果
     */


}

