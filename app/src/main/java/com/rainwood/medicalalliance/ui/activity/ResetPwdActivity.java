package com.rainwood.medicalalliance.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/6 11:47
 * @Desc: 重置密码
 */
public final class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.cet_password)
    private ClearEditText mPassword;
    @ViewById(R.id.cet_password_again)
    private ClearEditText mPasswordAgain;
    @ViewById(R.id.btn_confirm)
    private Button mConfirm;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                // toast("确定了");
                openActivity(HomeActivity.class);
                break;
        }
    }
}
