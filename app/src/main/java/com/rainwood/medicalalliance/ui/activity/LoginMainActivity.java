package com.rainwood.medicalalliance.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.pay.wechat.OnWeChatLoginListener;
import com.android.pay.wechat.WeChatConstants;
import com.android.pay.wechat.WeChatLogin;
import com.android.pay.wechat.WeChatUser;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/6 10:31
 * @Desc: 登录主界面 -- 选择登录方式或注册
 */
public final class LoginMainActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_main;
    }

    @ViewById(R.id.btn_wx_login)
    private Button wxLogin;
    @ViewById(R.id.btn_account_login)
    private Button accountLogin;
    @ViewById(R.id.tv_register)
    private TextView register;

    @Override
    protected void initView() {
        wxLogin.setOnClickListener(this);
        accountLogin.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wx_login:
                toast("微信登录");
                WeChatLogin.Builder builder = new WeChatLogin.Builder(this);
                builder.appId("xxx");
                builder.appSecret("xxx");
                builder.listener(new OnWeChatLoginListener() {
                    @Override
                    public void onWeChatLogin(int code, String msg, WeChatUser user) {
                        Log.d(TAG, "微信登录：code --" + code + " --- msg ---" + msg + " --- user ---" + user);
                        if (code == WeChatConstants.LOADING) {//登录中

                        }
                        if (code == WeChatConstants.SUCCEED) {//登录成功

                        }
                        if (code == WeChatConstants.CANCEL) {//用户取消登录

                        }
                        if (code == WeChatConstants.AUTH_DENIED) {//授权取消

                        }
                    }
                });
                builder.build();
                break;
            case R.id.btn_account_login:
                //toast("账号登录");
                openActivity(LoginAccountActivity.class);
                break;
            case R.id.tv_register:
                //toast("注册");
                openActivity(RegisterActivity.class);
                break;
        }
    }
}
