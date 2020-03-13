package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.domain.BaseInfoBean;
import com.rainwood.medicalalliance.domain.ImageBean;
import com.rainwood.medicalalliance.domain.SubRecordBean;
import com.rainwood.medicalalliance.domain.UsuallyBean;
import com.rainwood.medicalalliance.domain.VIPCardBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.CardDetailAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/12 17:59
 * @Desc: 会员卡详情
 */
public final class VipCardDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_card_detail;
    }


    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView mPageRightTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView mContentList;

    // VIP记录
    private SubRecordBean mRecord;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    // initial
    private List<VIPCardBean> mList;
    private String[] titles = {"卡片信息", "基本资料", "安全设置"};
    private String[] labels = {"修改", "修改会员卡密码"};
    // 卡片信息
    private String[] infosTitles = {"会员等级", "卡号", "卡片类型", "销售价", "原价", "累计消费", "续费时间", "到期时间"};
    // 基本资料
    private String[] baseTitels = {"会员卡分类", "姓名", "性别", "手机号", "身份证号", "身份证照片", "户口本照片"};

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("会员卡详情");
        mPageRightTitle.setText("续费记录");
        mPageRightTitle.setTextColor(getResources().getColor(R.color.green10));
        mPageRightTitle.setOnClickListener(this);

        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        mRecord = (SubRecordBean) getIntent().getSerializableExtra("record");
        if (mRecord != null) {           // 通过会员卡id和客户id查询会员卡详情
            // Log.d(TAG, " -- record:" + record.toString());
            RequestPost.VIPCardDetail(mRecord.getKhMxId(), mRecord.getId(), this);
        }

        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            VIPCardBean card = new VIPCardBean();
            card.setTitles(titles[i]);
            card.setType(i);
            if (i > 0) {     // 修改、修改会员卡密码
                card.setLabel(labels[i - 1]);
            }
            // 卡片信息
            List<UsuallyBean> infoList = new ArrayList<>();
            for (int j = 0; j < infosTitles.length; j++) {
                UsuallyBean usually = new UsuallyBean();
                usually.setTitle(infosTitles[j]);
                usually.setContent("哈哈");
                infoList.add(usually);
            }
            card.setInfosList(infoList);
            // 基本资料
            List<BaseInfoBean> baseList = new ArrayList<>();
            for (int j = 0; j < baseTitels.length; j++) {
                BaseInfoBean baseInfo = new BaseInfoBean();
                baseInfo.setTitle(baseTitels[j]);
                baseInfo.setType(0);        // 设置加载的adapter类型
                if (j > 4) {
                    if (j == 5) {            // 身份证照片
                        baseInfo.setType(1);
                        List<ImageBean> imageList = new ArrayList<>();
                        for (int k = 0; k < 2; k++) {
                            ImageBean image = new ImageBean();
                            image.setPath(String.valueOf(R.drawable.icon_loading_fail));
                            imageList.add(image);
                        }
                        baseInfo.setIdCardList(imageList);
                    }
                    if (j == 6) {             // 户口本照片
                        baseInfo.setType(2);
                        List<ImageBean> imageList = new ArrayList<>();
                        for (int k = 0; k < 4; k++) {
                            ImageBean image = new ImageBean();
                            image.setPath(String.valueOf(R.drawable.icon_loading_fail));
                            imageList.add(image);
                        }
                        baseInfo.setBookList(imageList);
                    }

                } else {
                    baseInfo.setLabel("哈哈哈");
                }
                baseList.add(baseInfo);
            }
            card.setInfo(baseList);
            mList.add(card);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:                // 续费记录
                Intent intent = new Intent(this, RenewalRecordActivity.class);
                intent.putExtra("customId", mRecord.getKhMxId());
                startActivity(intent);
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
                    CardDetailAdapter detailAdapter = new CardDetailAdapter(VipCardDetailActivity.this, mList);
                    mContentList.setAdapter(detailAdapter);
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
                if (result.url().contains("library/mData.php?type=getVipCard")) {               // 会员卡详情

                }
            }
        }
    }

}
