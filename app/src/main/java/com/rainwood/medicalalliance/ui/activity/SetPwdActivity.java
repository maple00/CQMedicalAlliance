package com.rainwood.medicalalliance.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.json.JsonParser;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/6 12:12
 * @Desc: 注册时设置密码
 */
public final class SetPwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pwd;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.pet_password)
    private PasswordEditText mPassword;
    @ViewById(R.id.btn_get_code)
    private Button mGetCode;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mDialog = new DialogUtils(this, "加载中，请稍后");
        mPageBack.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(mPassword.getText())) {
                    toast("请输入密码");
                    break;
                }
                mDialog.showDialog();
                RequestPost.RegisterPwdCheck(mPassword.getText().toString().trim(), this);
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
                if (result.url().contains("library/mData.php?type=inPassword")) {
                    Log.d(TAG, " --- " + body.get("data"));
                    openActivity(HomeActivity.class);
                }
            }else {
                toast(body.get("warn"));
            }
            mDialog.dismissDialog();
        }
    }
}
