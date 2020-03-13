package com.rainwood.medicalalliance.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 输入手机验证码
 */
public final class CodeVerifyActivity extends BaseActivity implements OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_code_verify;
    }

    // 返回
    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    // 验证码
    @ViewById(R.id.tv_verify_countdown)
    private TextView verifyCountDown;
    @ViewById(R.id.ed_code1)
    private EditText editText1;
    @ViewById(R.id.ed_code2)
    private EditText editText2;
    @ViewById(R.id.ed_code3)
    private EditText editText3;
    @ViewById(R.id.ed_code4)
    private EditText editText4;
    @ViewById(R.id.ed_code5)
    private EditText editText5;
    @ViewById(R.id.ed_code6)
    private EditText editText6;
    @ViewById(R.id.tv_tel_num)
    private TextView mTelNum;           // 发送到的手机号
    // 确定
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private String verifyCode;
    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mDialog = new DialogUtils(this, "加载中");
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        // 初始化监听EditText
        initListener();
        // 初始化定时器
        CountDownTimerUtils.initCountDownTimer(60, verifyCountDown, "重新发送验证码",
                getResources().getColor(R.color.fontGray), getResources().getColor(R.color.green10));
        CountDownTimerUtils.countDownTimer.start();
        verifyCountDown.addTextChangedListener(new TextWatcher() {          // 监听倒计时是否完毕
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable mText) {
                if ("重新发送验证码".contentEquals(mText)) {    // 计时器完毕
                    verifyCountDown.setOnClickListener(CodeVerifyActivity.this);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (verifyCode == null || "".equals(verifyCode)) {
                    toast("请输入验证码");
                    return;
                }
                Log.d(TAG, "-- code:" + verifyCode + " -- length: " + verifyCode.length());
                if (verifyCode.length() != 6) {
                    toast("请核对验证");
                    return;
                }
                mDialog.showDialog();

                RequestPost.RegisterVerifyCheck(verifyCode, this);
                break;
            default:
                break;
        }
    }

    private EditText[] mArray;

    /**
     * 输入的验证码的监听
     */
    private void initListener() {
        mArray = new EditText[]{editText1, editText2, editText3, editText4, editText5, editText6};
        for (int i = 0; i < mArray.length; i++) {
            final int j = i;
            mArray[j].addTextChangedListener(new TextWatcher() {
                private CharSequence temp;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (temp.length() == 1 && j < mArray.length - 1) {
                        mArray[j + 1].setFocusable(true);
                        mArray[j + 1].setFocusableInTouchMode(true);
                        mArray[j + 1].requestFocus();
                    }

                    if (temp.length() == 0) {
                        if (j >= 1) {
                            mArray[j - 1].setFocusable(true);
                            mArray[j - 1].setFocusableInTouchMode(true);
                            mArray[j - 1].requestFocus();
                        }
                    }
                    checkNumber();
                }
            });
        }
    }

    /**
     * 检查验证码
     */
    private void checkNumber() {
        if (!TextUtils.isEmpty(editText1.getText().toString().trim()) && !TextUtils.isEmpty(editText2.getText().toString().trim())
                && !TextUtils.isEmpty(editText3.getText().toString().trim()) && !TextUtils.isEmpty(editText4.getText().toString().trim())
                && !TextUtils.isEmpty(editText5.getText().toString().trim()) && !TextUtils.isEmpty(editText6.getText().toString().trim())) {


            // 获取输入的验证码
            verifyCode = editText1.getText().toString().trim() + editText2.getText().toString().trim() +
                    editText3.getText().toString().trim() + editText4.getText().toString().trim()
                    + editText5.getText().toString().trim() + editText6.getText().toString().trim();
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
                if (result.url().contains("library/mData.php?type=InProve")) {
                    toast(body.get("warn"));
                    switch (Contants.CLICK_POSITION_SIZE) {
                        case 0x1001:                    // 设置密码
                            postDelayed(() -> openActivity(SetPwdActivity.class), 900);
                            break;
                        case 0x1002:                    // 重置密码
                            postDelayed(() -> openActivity(ResetPwdActivity.class), 900);
                            break;
                    }
                }
            } else {
                toast(body.get("warn"));
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
