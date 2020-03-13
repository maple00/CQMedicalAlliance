package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.BuyRecordBean;
import com.rainwood.medicalalliance.domain.SubRecordBean;
import com.rainwood.medicalalliance.domain.UsuallyBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.BuyRecordAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/3/9 21:56
 * @Desc: 购买记录
 */
public final class BuyRecordActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_record;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_total_buy)
    private TextView mTotalBuy;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView contentList;

    private List<SubRecordBean> mList;
    // mHandler
    private final int INITIAL_SIZE = 0x101;

    private String[] titles = {"类型", "累计消费", "续费时间", "到期时间"};

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("购买记录");
    }

    @Override
    protected void initData() {
        super.initData();
        RequestPost.BuyRecord(Contants.mLoginner.getKhid(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    BuyRecordAdapter recordAdapter = new BuyRecordAdapter(BuyRecordActivity.this, mList);
                    contentList.setAdapter(recordAdapter);
                    recordAdapter.setOnClickButton(new BuyRecordAdapter.OnClickButton() {
                        @Override
                        public void onClickDetail(int position) {
                            // toast("查看详情：" + position);
                            //openActivity(VipCardDetailActivity.class);
                            Intent intent = new Intent(BuyRecordActivity.this, VipCardDetailActivity.class);
                            intent.putExtra("record", mList.get(position));
                            startActivity(intent);
                        }

                        @Override
                        public void onClickRenewal(int position) {
                            //toast("续费：" + position);
                            Intent intent = new Intent(BuyRecordActivity.this, OpenVIPTypeActivity.class);
                            // 传递该条记录的客户id
                            intent.putExtra("customId", mList.get(position).getKhMxId());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=buyRecord")) {          // 购买记录
                    BuyRecordBean buyRecord = JsonParser.parseJSONObject(BuyRecordBean.class, body.get("data"));
                    Log.d(TAG, " --- buy " + buyRecord.toString());
                    // 赋值
                    for (SubRecordBean recordBean : buyRecord.getRecord()) {
                        for (int i = 0; i < titles.length; i++) {
                            UsuallyBean usually = new UsuallyBean();
                            usually.setTitle(titles[i]);
                            switch (i) {
                                case 0:             // 类型
                                    usually.setContent(recordBean.getType());
                                    break;
                                case 1:             // 累计消费
                                    usually.setContent(recordBean.getPrice());
                                    break;
                                case 2:             // 续费时间
                                    usually.setContent(recordBean.getPayTime());
                                    break;
                                case 3:             // 到期时间
                                    usually.setContent(recordBean.getOverTime());
                                    break;
                            }
                            recordBean.getList().add(usually);
                        }
                    }
                    mTotalBuy.setText("您已累计购买" + buyRecord.getUsed() + "元");
                    // 购买记录
                    mList = buyRecord.getRecord();
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
