package com.rainwood.medicalalliance.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/6 11:26
 * @Desc: 找回密码
 */
public final class ReserveActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.btn_get_code)
    private Button mGetCode;
    @ViewById(R.id.cet_tel_num)
    private ClearEditText mTelNum;

    @Override
    protected void initView() {
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
                toast("获取验证码");
                Contants.CLICK_POSITION_SIZE = 0x1002;
                openActivity(CodeVerifyActivity.class);
                break;
        }
    }
}
