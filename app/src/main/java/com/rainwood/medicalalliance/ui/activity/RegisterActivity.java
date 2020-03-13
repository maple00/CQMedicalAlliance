package com.rainwood.medicalalliance.ui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.json.JsonParser;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.utils.CountDownTimerUtils;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/6 11:57
 * @Desc: 注册
 */
public final class RegisterActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.cet_mobile_num)
    private ClearEditText mTelNum;
    @ViewById(R.id.btn_get_code)
    private Button mGetCode;
    @ViewById(R.id.tv_login)
    private TextView mLogin;        // 立即登录
    @ViewById(R.id.iv_wx_login)
    private ImageView mWXLogin;
    @ViewById(R.id.iv_qq_login)
    private ImageView mQQLogin;


    // Dialog
    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mWXLogin.setOnClickListener(this);
        mQQLogin.setOnClickListener(this);

        // 手机号监听 -- 手机号格式不正确则置灰
        InputTextHelper.with(this)
                .addView(mTelNum)
                .setMain(mGetCode)
                .setListener(helper -> mTelNum.getText().toString().length() == 11).build();
    }

    @Override
    protected void initData() {
        super.initData();
        mDialog = new DialogUtils(this, "加载中");
        // 记录最近输入的一次手机号
        if (!"".equals(Contants.LAST_TEL)) {
            mTelNum.setText(Contants.LAST_TEL);
        }

        // 计时器发送 --- 不可点击
        if (CountDownTimerUtils.CountDownTimerSize > 0) {
            CountDownTimerUtils.initCountDownTimer(CountDownTimerUtils.CountDownTimerSize / 1000,
                    mGetCode, "可重新发送", getResources().getColor(R.color.fontGray), getResources().getColor(R.color.white));
            CountDownTimerUtils.countDownTimer.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code:             // 获取验证码
                Contants.CLICK_POSITION_SIZE = 0x1001;
                // 发送验证码
                if (TextUtils.isEmpty(mTelNum.getText())) {
                    toast("请输入手机号");
                    return;
                }
                Contants.LAST_TEL = mTelNum.getText().toString().trim();
                mDialog.showDialog();

                RequestPost.RegisterTel(mTelNum.getText().toString().trim(), this);
                break;
            case R.id.tv_login:                 // 到登录界面登录
                openActivity(LoginAccountActivity.class);
                break;
            case R.id.iv_wx_login:              // 微信授权登录
                toast("微信登录");
                break;
            case R.id.iv_qq_login:              // QQ授权登录
                toast("QQ登录");
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
                if (result.url().contains("library/mData.php?type=inGetCaptcha")) {   // 获取手机验证码
                    toast(body.get("warn"));
                    openActivity(CodeVerifyActivity.class);
                    // postDelayed(() -> openActivity(CodeVerifyActivity.class), 1000);
                }
            } else {
                toast(body.get("warn"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog.dismissDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
