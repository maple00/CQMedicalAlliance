package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ImageBean;
import com.rainwood.medicalalliance.ui.adapter.UploadImgAdapter;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/9 17:28
 * @Desc: 会员基本信息页面
 */
public final class VipInfoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_infos;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_checked_type)
    private TextView choiceType;
    @ViewById(R.id.cet_name)
    private ClearEditText name;         // 输入姓名
    @ViewById(R.id.iv_man)
    private ImageView manIv;
    @ViewById(R.id.tv_man)
    private TextView manTv;
    @ViewById(R.id.iv_woman)
    private ImageView womanIv;
    @ViewById(R.id.tv_woman)
    private TextView womanTv;
    @ViewById(R.id.cet_card_num)
    private ClearEditText cardNum;
    @ViewById(R.id.fl_card_front)       // 身份证正面
    private FrameLayout cardFront;
    @ViewById(R.id.fl_card_reverse)
    private FrameLayout cardReverse;
    @ViewById(R.id.cet_mobile_num)
    private ClearEditText mobileNum;
    @ViewById(R.id.tv_send_code)
    private TextView sendCode;
    @ViewById(R.id.cet_verify_code)
    private ClearEditText verifyCode;
    @ViewById(R.id.pet_card_pwd)
    private PasswordEditText cardPwd;
    @ViewById(R.id.iv_checked)
    private ImageView service;
    @ViewById(R.id.tv_terms)            // 阅读条款
    private TextView terms;
    @ViewById(R.id.btn_next_step)
    private Button nextStep;        // 下一步

    // 家庭户口本
    @ViewById(R.id.ll_family)
    private LinearLayout familyLl;
    @ViewById(R.id.mgv_family_vip)
    private MeasureGridView familyImg;

    private List<ImageBean> mImageList;

    // mHandler
    private final int FAMILY_SIZE = 0x0110;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("会员基本信息");
        cardFront.setOnClickListener(this);
        cardReverse.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        service.setOnClickListener(this);
        terms.setOnClickListener(this);
        manIv.setOnClickListener(this);
        manTv.setOnClickListener(this);
        womanIv.setOnClickListener(this);
        womanTv.setOnClickListener(this);
        nextStep.setOnClickListener(this);

        // 个人会员
        if (Contants.CLICK_POSITION_SIZE == 0x1005) {
            familyLl.setVisibility(View.GONE);
            choiceType.setText("已选个人会员");
        }
        // 家庭会员
        if (Contants.CLICK_POSITION_SIZE == 0x1006) {
            familyLl.setVisibility(View.VISIBLE);
            choiceType.setText("已选家庭会员");
            Message msg = new Message();
            msg.what = FAMILY_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_man:
            case R.id.tv_man:
                // toast("先生");
                womanIv.setImageResource(R.drawable.shape_uncheck_shape);
                manIv.setImageResource(R.drawable.shape_checked_shape);
                break;
            case R.id.iv_woman:
            case R.id.tv_woman:
                // toast("女士");
                womanIv.setImageResource(R.drawable.shape_checked_shape);
                manIv.setImageResource(R.drawable.shape_uncheck_shape);
                break;
            case R.id.fl_card_front:
                toast("上传身份证正面");
                break;
            case R.id.fl_card_reverse:
                toast("身份证背面");
                break;
            case R.id.tv_send_code:
                toast("发送验证码");
                break;
            case R.id.iv_checked:
                toast("阅读服务条款了");
                break;
            case R.id.tv_terms:
                toast("免责条款");
                openActivity(DisclaimerActivity.class);
                break;
            case R.id.btn_next_step:
                toast("下一步");
                openActivity(OpenVIPTypeActivity.class);
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FAMILY_SIZE:
                    mImageList = new ArrayList<>();
                    ImageBean image = new ImageBean();
                    image.setHasAdd(true);
                    image.setPath("");
                    mImageList.add(image);
                    UploadImgAdapter imgAdapter = new UploadImgAdapter(VipInfoActivity.this, mImageList);
                    familyImg.setAdapter(imgAdapter);
                    familyImg.setNumColumns(3);
                    imgAdapter.setOnClickImage(new UploadImgAdapter.OnClickImage() {
                        @Override
                        public void onClickUpload() {
                            toast("添加图片");
                        }
                    });
                    break;
            }
        }
    };
}
