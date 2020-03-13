package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.base.BaseDialog;
import com.rainwood.medicalalliance.domain.PayBean;
import com.rainwood.medicalalliance.domain.VIPPriceBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.OpenVIPAdapter;
import com.rainwood.medicalalliance.ui.dialog.MessageDialog;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/10 11:42
 * @Desc: 开通的会员类型
 */
public final class OpenVIPTypeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private PopupWindow mPopwindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_vip_type;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.btn_buy_now)
    private Button buyNow;
    @ViewById(R.id.gv_open_type)
    private GridView openType;

    private List<VIPPriceBean> mList;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("会员类型");
        buyNow.setOnClickListener(this);

        String customId = getIntent().getStringExtra("customId");
        if (customId != null){              // 从续费界面过来的
            Log.d(TAG, " 续费客户id： " + customId);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //mList = new ArrayList<>();
        RequestPost.BuyCardType(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_buy_now:              // popWindow 布局
                int count = -1;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isSelector()) {
                        count = i;
                    }
                }
                if (count < 0) {
                    toast("请选择会员类型");
                    return;
                } else {
                    toast("您选择了：" + mList.get(count).getGrade() + "类型会员");
                }
                //toast("立即购买");
                PayBean payMethod = new PayBean();
                View rootView = LayoutInflater.from(this).inflate(R.layout.activity_open_vip_type, null);
                View view = View.inflate(this, R.layout.pop_pay_method, null);
                mPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mPopwindow.setOutsideTouchable(false);
                mPopwindow.setClippingEnabled(false);
                mPopwindow.setAnimationStyle(R.style.BottomAnimStyle);
                mPopwindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                mPopwindow.setOutsideTouchable(false);
                popOutShadow(mPopwindow);
                ImageView close = view.findViewById(R.id.iv_close);
                close.setOnClickListener(v1 -> new MessageDialog.Builder(OpenVIPTypeActivity.this)
                        .setTitle(null)
                        .setMessage("是否放弃本次支付？")
                        .setConfirm(getString(R.string.dialog_go_on))
                        .setCancel(getString(R.string.dialog_give_up))
                        .setAutoDismiss(false)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("请继续支付");
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                                mPopwindow.dismiss();
                            }
                        }).show());
                LinearLayout wetPay = view.findViewById(R.id.ll_wet_pay);
                ImageView wet = view.findViewById(R.id.iv_wet_checked);
                ImageView ali = view.findViewById(R.id.iv_ali_checked);
                LinearLayout aliPay = view.findViewById(R.id.ll_ali_pay);
                // 支付方式
                aliPay.setOnClickListener(v13 -> {          // 支付宝支付
                    payMethod.setMethod("1");
                    wet.setImageResource(R.drawable.shape_uncheck_shape);
                    ali.setImageResource(R.drawable.shape_checked_shape);
                });
                wetPay.setOnClickListener(v12 -> {          // 微信支付
                    payMethod.setMethod("0");
                    wet.setImageResource(R.drawable.shape_checked_shape);
                    ali.setImageResource(R.drawable.shape_uncheck_shape);
                });
                // 保险条款
                ImageView agreed = view.findViewById(R.id.iv_agreed);
                agreed.setOnClickListener(v14 -> {
                    payMethod.setAgreed(!payMethod.isAgreed());
                    if (payMethod.isAgreed()) {
                        agreed.setImageResource(R.drawable.ic_checked);
                    } else {
                        agreed.setImageResource(R.drawable.shape_uncheck_shape);
                    }
                });

                // 确认支付
                Button confirmPay = view.findViewById(R.id.btn_confirm_pay);
                confirmPay.setOnClickListener(v15 -> {
                    Log.d(TAG, payMethod.toString());
                    if (payMethod.getMethod() == null) {
                        toast("请选择支付方式");
                        return;
                    }
                    if (!payMethod.isAgreed()) {
                        toast("未同意条款不能支付");
                        return;
                    }
                    // TODO: 去支付
                    openActivity(SuccessActivity.class);
                });

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    OpenVIPAdapter vipAdapter = new OpenVIPAdapter(OpenVIPTypeActivity.this, mList);
                    openType.setNumColumns(3);
                    openType.setAdapter(vipAdapter);
                    vipAdapter.setOnClickItem(position -> {
                        for (VIPPriceBean priceBean : mList) {
                            priceBean.setSelector(false);
                        }
                        mList.get(position).setSelector(true);
                    });
                    break;
            }
        }
    };

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getVipCard")) {            // 购买会员卡类型
                    mList = JsonParser.parseJSONArray(VIPPriceBean.class, body.get("data"));
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
        }
    }
}
