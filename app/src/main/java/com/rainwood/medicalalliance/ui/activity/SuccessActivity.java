package com.rainwood.medicalalliance.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/10 15:38
 * @Desc: 成功页面
 */
public final class SuccessActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_success;
    }

    @ViewById(R.id.tv_pay_amount)       // 支付金额
    private TextView amount;
    @ViewById(R.id.btn_return)              // 返回到会员中心
    private Button returnVip;

    @Override
    protected void initView() {
        amount.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.green10) + " size='"
                + FontDisplayUtil.dip2px(this, 24f) + "'>￥ </font>"
                + "<font color=" + getResources().getColor(R.color.green10) + " size='"
                + FontDisplayUtil.dip2px(this, 30f) + "'><b>"
                + "100" + "</b></font>"));

        // 返回到会员中心
        returnVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HomeActivity.class);
            }
        });
    }
}
