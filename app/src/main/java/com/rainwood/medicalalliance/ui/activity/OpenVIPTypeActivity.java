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

import com.android.pay.alipay.AliPay;
import com.android.pay.alipay.OnAliPayListener;
import com.android.pay.wechat.OnWeChatPayListener;
import com.android.pay.wechat.WeChatConstants;
import com.android.pay.wechat.WeChatPay;
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
        if (customId != null) {              // 从续费界面过来的
            Log.d(TAG, " 续费客户id： " + customId);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //mList = new ArrayList<>();
        RequestPost.BuyCardType(this);
    }

    @SuppressLint("SetTextI18n")
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
                        break;
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
                // 卡信息
                TextView purpose = view.findViewById(R.id.tv_purpose);
                purpose.setText(mList.get(count).getGrade() + "，原价￥" + mList.get(count).getOldPrice());
                TextView payMoney = view.findViewById(R.id.tv_pay_money);
                payMoney.setText("￥" + mList.get(count).getPrice());
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
                    // TODO: 去支付-- 支付方式
                    // openActivity(SuccessActivity.class);
                    if ("0".equals(payMethod.getMethod())) {             // 微信支付
                        wxPay();
                    }
                    if ("1".equals(payMethod.getMethod())) {             // 支付宝支付
                        aliPay();
                    }
                });

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        AliPay.Builder builder = new AliPay.Builder(this);
        // app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
        builder.orderInfo("2021001144648331");
        builder.listener(new OnAliPayListener() {
            /**
             * 参数解释
             * @param status 是结果码(类型为字符串)。
             *       9000	订单支付成功
             *       8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
             *       4000	订单支付失败
             *       5000	重复请求
             *       6001	用户中途取消
             *       6002	网络连接出错
             *       6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
             *       其它	其它支付错误
             * @param json        是处理结果(类型为json结构字符串)
             *       out_trade_no	String	是	64	商户网站唯一订单号	70501111111S001111119
             *       trade_no	String	是	64	该交易在支付宝系统中的交易流水号。最长64位。	2014112400001000340011111118
             *       app_id	String	是	32	支付宝分配给开发者的应用Id。	2014072300007148
             *       total_amount	Price	是	9	该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01,100000000.00]，精确到小数点后两位。	9.00
             *       seller_id	String	是	16	收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字	2088111111116894
             *       msg	String	是	16	处理结果的描述，信息来自于code返回结果的描述	success
             *       charset	String	是	16	编码格式	utf-8
             *       timestamp	String	是	32	时间	2016-10-11 17:43:36
             *       code	String	是	16	结果码	具体见
             * @param description description是描述信息(类型为字符串)
             */
            @Override
            public void onAliPay(String status, String json, String description) {
                Log.d(TAG, " -- status: " + status + " -- json:" + json + " --- description:" + description);
                if (status.equals("9000")) {//成功

                } else if (status.equals("6001")) {//用户取消

                } else {//支付失败

                }
            }
        });
        builder.loading(true);
        builder.build();
    }

    /**
     * 微信支付
     */
    private void wxPay() {
        WeChatPay.Builder builder = new WeChatPay.Builder(this);
        builder.appId("xxxx");
        builder.partnerId("xxx");
        builder.prepayId("xxx");
        builder.nonceStr("xxxx");
        builder.timeStamp("xxxx");
        builder.packageValue("Sign=WXPay");
        builder.sign("xxxx");
        builder.listener(new OnWeChatPayListener() {
            @Override
            public void onWeChatPay(int code, String msg) {
                Log.d(TAG, " --- code -- " + code + " ---- msg ----" + msg);
                if (code == WeChatConstants.SUCCEED) {//支付成功

                }
                if (code == WeChatConstants.CANCEL) {//用户取消

                }
                if (code == WeChatConstants.FAILED) {//支付失败

                }
            }
        });
        builder.extData("用于购买XXXX");//支付提示文字
        builder.build();
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
