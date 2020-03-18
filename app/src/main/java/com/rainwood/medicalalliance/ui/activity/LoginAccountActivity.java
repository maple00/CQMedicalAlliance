package com.rainwood.medicalalliance.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pay.wechat.OnWeChatLoginListener;
import com.android.pay.wechat.WeChatConstants;
import com.android.pay.wechat.WeChatLogin;
import com.android.pay.wechat.WeChatUser;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.LoginnerBean;
import com.rainwood.medicalalliance.json.JsonParser;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/6 10:56
 * @Desc: 账号登录
 */
public final class LoginAccountActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_login;
    }

    @ViewById(R.id.et_login_account)
    private ClearEditText mAccount;
    @ViewById(R.id.et_login_password)
    private PasswordEditText mPassword;
    @ViewById(R.id.tv_forget_pwd)
    private TextView mForget;
    @ViewById(R.id.btn_login_commit)
    private Button mLogin;
    @ViewById(R.id.tv_register)
    private TextView mRegister;
    @ViewById(R.id.iv_wx_login)
    private ImageView mWXLogin;
    @ViewById(R.id.iv_qq_login)
    private ImageView mQQLogin;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mDialog = new DialogUtils(this, "登录中");
        mForget.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mWXLogin.setOnClickListener(this);
        mQQLogin.setOnClickListener(this);

        mAccount.setText("15999999999");
        mPassword.setText("0000000");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                toast("忘记密码");
                openActivity(ReserveActivity.class);
                break;
            case R.id.btn_login_commit:
                if (TextUtils.isEmpty(mAccount.getText())) {
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(mPassword.getText())) {
                    toast("请输入密码");
                    return;
                }
                mDialog.showDialog();
                RequestPost.AccountLogin(mAccount.getText().toString().trim(), mPassword.getText().toString().trim(), this);
                break;
            case R.id.tv_register:
                toast("立即注册");
                openActivity(RegisterActivity.class);
                break;
            case R.id.iv_wx_login:
                toast("微信登录");
                //openActivity(HomeActivity.class);
                WeChatLogin.Builder builder = new WeChatLogin.Builder(this);
                builder.appId("xxx");
                builder.appSecret("xxx");
                builder.listener(new OnWeChatLoginListener() {
                    @Override
                    public void onWeChatLogin(int code, String msg, WeChatUser user) {
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
            case R.id.iv_qq_login:
                toast("QQ登录");
                openActivity(HomeActivity.class);
                break;

        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=login")) {
                    Contants.mLoginner = JsonParser.parseJSONObject(LoginnerBean.class, body.get("data"));
                    Log.d(TAG, " -- 登录信息： " + Contants.mLoginner.toString());
                    if (Contants.mLoginner != null) {
                        // 是不是会员标识
                        Contants.hasMembers = !"0".equals(Contants.mLoginner.getIfKehu());
                    }
                    mDialog.dismissDialog();
                    toast("登录成功");
                    postDelayed(() -> openActivity(HomeActivity.class), 1000);
                }
            } else {
                toast(body.get("warn"));
                postDelayed(() -> mDialog.dismissDialog(), 500);
            }
        }
    }
}
