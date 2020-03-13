package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ContentCoversBean;
import com.rainwood.medicalalliance.ui.adapter.ContentAdapter;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/6 15:50
 * @Desc: 联盟动态
 */
public final class AllianceActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_alliance;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.et_search)
    private ClearEditText mSearchContent;
    @ViewById(R.id.tv_search)
    private TextView mSearch;
    @ViewById(R.id.drl_refresh)             // 刷新、加载
    private DaisyRefreshLayout mRefreshLayout;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView mContentList;

    private ContentCoversBean mCovers;          // 内容
    // mHandler
    private final int INITIAL_SIZE = 0xa1;
    private final int REFRESH_SIZE = 0xa2;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        if (Contants.CLICK_POSITION_SIZE == 0x1003) {    // 联盟动态
            mSearchContent.setHint("搜索你想看的动态");
        }
        if (Contants.CLICK_POSITION_SIZE == 0x1004) {     // 联盟活动
            mSearchContent.setHint("搜索你想看的活动");
        }
        mSearchContent.setFocusableInTouchMode(false);
        mSearchContent.setFocusable(false);
        mSearchContent.setOnClickListener(this);

        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mCovers = new ContentCoversBean();
            mCovers.setImgPath(null);
            mCovers.setTitle("专业医疗废物认准——融家科技");
            mCovers.setText("深化用户体验进行垃圾分类，传达环保理念，产品造型几度简化，正面突出突出...");
            mList.add(mCovers);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
            case R.id.et_search:
                openActivity(SearchViewActivity.class);
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
                    toast("更新了" + Math.abs(mList == null ? 0 : mList.size() - temp_count) + "条内容");
                    temp_count = mList == null ? 0 : mList.size();
                    ContentAdapter adapter = new ContentAdapter(AllianceActivity.this, mList);
                    mContentList.setAdapter(adapter);
                    adapter.setOnClickItem(position -> {
                        // toast("查看详情：" + position);
                        openActivity(ContentActivity.class);
                    });
                    // 可以加载了
                    mRefreshLayout.setEnableLoadMore(true);
                    mRefreshLayout.setEnableRefresh(true);
                    break;
                case REFRESH_SIZE:
                    // 不能刷新
                    mRefreshLayout.setEnableRefresh(false);
                    // 不能加载
                    mRefreshLayout.setEnableLoadMore(false);
                    Message contentMsg = new Message();
                    contentMsg.what = INITIAL_SIZE;
                    mHandler.sendMessage(contentMsg);
                    // 刷新
                    mRefreshLayout.setOnRefreshListener(() -> postDelayed(() -> {
                        mRefreshLayout.setRefreshing(false);
                        mList.add(mCovers);
                        Message msg1 = new Message();
                        msg1.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg1);
                    }, 1000 * 2));
                    // 加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        mRefreshLayout.setLoadMore(false);
                        mList.remove(mCovers);
                        Message msg1 = new Message();
                        msg1.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg1);
                    });
                    break;
            }
        }
    };
    // 记录contentList Size
    private static int temp_count = 0;

    private List<ContentCoversBean> mList;
}
