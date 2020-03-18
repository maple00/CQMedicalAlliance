package com.rainwood.medicalalliance.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/16 9:27
 * @Desc: 修改登录密码
 */
public final class ModifyLoginPwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_login_pwd;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.cet_mobile_num)
    private ClearEditText mobileNum;
    @ViewById(R.id.tv_send_code)
    private TextView sendCode;
    @ViewById(R.id.cet_verify_code)
    private ClearEditText verifyCode;
    @ViewById(R.id.pet_password)
    private PasswordEditText mPassword;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("修改登录密码");
        sendCode.setOnClickListener(this);
        confirm.setOnClickListener(this);

        mDialog = new DialogUtils(this, "加载中");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_code:
                // toast("发送验证码");
                if (TextUtils.isEmpty(mobileNum.getText())) {
                    toast("请输入手机号");
                    return;
                }
                mDialog.showDialog();
                RequestPost.getVerifyCode(mobileNum.getText().toString().trim(), this);
                break;
            case R.id.btn_confirm:
                // toast("确定了");
                if (TextUtils.isEmpty(verifyCode.getText())) {
                    toast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mPassword.getText())) {
                    toast("请输入密码");
                    return;
                }
                mDialog.showDialog();
                RequestPost.modifyLoginPwd(verifyCode.getText().toString().trim(), mPassword.getText().toString().trim(), this);
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
                if (result.url().contains("library/mData.php?type=changeLoginPas")) {           // 修改登录密码
                    mDialog.dismissDialog();
                    toast("修改成功");
                    openActivity(ModifyActivity.class);
                }
                if (result.url().contains("library/mData.php?type=inPassword")) {        // 获取验证码
                    toast("验证码发送成功");
                }
            } else {
                toast(body.get("warn"));
            }
        }
    }
}
