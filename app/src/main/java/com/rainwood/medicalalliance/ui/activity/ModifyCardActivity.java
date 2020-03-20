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
import com.rainwood.medicalalliance.utils.CountDownTimerUtils;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/19 9:47
 * @Desc: 修改会员卡密码
 */
public final class ModifyCardActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mCustomId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
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
    private PasswordEditText mPwd;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("修改会员卡密码");
        sendCode.setOnClickListener(this);
        confirm.setOnClickListener(this);

        // 获取客户id
        mCustomId = getIntent().getStringExtra("customId");
        // 验证手机号格式
        InputTextHelper.with(this)
                .addView(mobileNum)
                .setMain(sendCode)
                .setListener(helper -> mobileNum.getText().toString().length() == 11).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_code:         // getCaptcha
                if (TextUtils.isEmpty(mobileNum.getText())) {
                    toast("请输入手机号");
                    return;
                }
                mDialog = new DialogUtils(this, "发送中");
                mDialog.showDialog();
                RequestPost.getVerifyCode(mobileNum.getText().toString().trim(), this);
                CountDownTimerUtils.initCountDownTimer(60, sendCode, "可重新发送",
                        getResources().getColor(R.color.fontGray), getResources().getColor(R.color.green10));
                CountDownTimerUtils.countDownTimer.start();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(verifyCode.getText())) {
                    toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(mPwd.getText())) {
                    toast("请输入密码");
                    return;
                }
                mDialog = new DialogUtils(this, "修改中");
                mDialog.showDialog();
                RequestPost.modifyPwd(mCustomId, mPwd.getText().toString().trim(), verifyCode.getText().toString().trim(), this);
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
                if (result.url().contains("library/mData.php?type=getCaptcha")) {           // 发送手机验证码
                    toast("发送成功");
                }
                if (result.url().contains("library/mData.php?type=changePas")) {             // 修改密码
                    finish();
                }
            } else {
                toast(body.get("warn"));
            }
            mDialog.dismissDialog();
        }
    }
}
