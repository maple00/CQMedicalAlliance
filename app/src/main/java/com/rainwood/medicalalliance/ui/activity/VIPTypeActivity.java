package com.rainwood.medicalalliance.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: shearson
 * @Time: 2020/3/9 17:16
 * @Desc: 会员卡分类
 */
public final class VIPTypeActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_type_page;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.iv_personal_vip)
    private ImageView personalVipIv;
    @ViewById(R.id.tv_personal_vip)
    private TextView personalVipTv;
    @ViewById(R.id.iv_family_vip)
    private ImageView familyVipIv;
    @ViewById(R.id.tv_family)
    private TextView familyVipTv;

    @Override
    protected void initView() {
        initEvents();
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        personalVipIv.setOnClickListener(this);
        personalVipTv.setOnClickListener(this);
        familyVipIv.setOnClickListener(this);
        familyVipTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_personal_vip:
            case R.id.tv_personal_vip:
                /*toast("选择个人会员");
                Contants.CLICK_POSITION_SIZE = 0x1005;
                openActivity(VipInfoActivity.class);
                */
                // test
                openActivity(OpenVIPTypeActivity.class);
                break;
            case R.id.iv_family_vip:
            case R.id.tv_family:
                toast("选择家庭会员");
                Contants.CLICK_POSITION_SIZE = 0x1006;
                openActivity(VipInfoActivity.class);
                break;
        }
    }
}
