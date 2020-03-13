package com.rainwood.medicalalliance.ui.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.domain.RenewalBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.RenewalAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/13 16:52
 * @Desc: 续费记录
 */
public final class RenewalRecordActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_renewal_record;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView mContentList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;

    // list
    private List<RenewalBean> mList;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("续费记录");
    }

    @Override
    protected void initData() {
        super.initData();
        String customId = getIntent().getStringExtra("customId");
        if (customId != null){
            RequestPost.VIPRenewalRecord(customId, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case INITIAL_SIZE:
                    RenewalAdapter renewalAdapter = new RenewalAdapter(RenewalRecordActivity.this, mList);
                    mContentList.setAdapter(renewalAdapter);
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=renewRecord")) {               // 续费记录
                    mList = JsonParser.parseJSONArray(RenewalBean.class,  body.get("data"));
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }
}
