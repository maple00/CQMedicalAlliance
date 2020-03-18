package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ArticleBean;
import com.rainwood.medicalalliance.domain.DynamicBean;
import com.rainwood.medicalalliance.domain.HomePageBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.ContentAdapter;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.medicalalliance.utils.ListUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.rainwood.medicalalliance.common.Contants.ACTIVITY_RESULT_CODE;
import static com.rainwood.medicalalliance.common.Contants.DYNAMIC_RESULT_CODE;

/**
 * @Author: a797s
 * @Date: 2020/3/6 15:50
 * @Desc: 联盟动态
 */
public final class AllianceActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {


    private List<HomePageBean> mArrayList;

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

    // mHandler
    private final int INITIAL_SIZE = 0xa1;
    private final int REFRESH_SIZE = 0xa2;

    private DialogUtils mDialog;

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
    }

    @Override
    protected void initData() {
        super.initData();
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
                    if (ListUtils.getSize(mList) == 0){
                        toast("当前查询内容暂无记录");
                        return;
                    }
                    toast("更新了" + Math.abs(mList == null ? 0 : mList.size() - temp_count) + "条内容");
                    temp_count = mList == null ? 0 : mList.size();
                    ContentAdapter adapter = new ContentAdapter(AllianceActivity.this, mList);
                    mContentList.setAdapter(adapter);
                    adapter.setOnClickItem(position -> {
                        // 动态详情或者活动详情 --- 拼接之后传递
                        Intent intent = new Intent(AllianceActivity.this, ContentActivity.class);
                        StringBuffer content = new StringBuffer();
                        if (!TextUtils.isEmpty(mList.get(position).getPhotoSrc())) {      // 如果有图片
                            content.append("<img src=\"" + Contants.ROOT_URI + mList.get(position).getPhotoSrc() + "\" alt=\"" + mList.get(position).getTitle() + "\"" + "/>\n");
                        }
                        if (!TextUtils.isEmpty(mList.get(position).getVideoSrc())) {          // 如果有视频
                            //content.append()
                        }
                        if (mList.get(position).getArticle() != null && mList.get(position).getArticle().size() != 0) {      // 如果有文章
                            for (ArticleBean articleBean : mList.get(position).getArticle()) {
                                if (TextUtils.isEmpty(articleBean.getImg())) {
                                    content.append(articleBean.getWord());
                                } else {
                                    content.append("<img src=\"" + Contants.ROOT_URI + articleBean.getImg() + "\" alt=\"" + articleBean.getType() + "\"" + "/>\n");
                                }
                            }
                        }
                        intent.putExtra("content", (Serializable) content);
                        startActivity(intent);
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
                        // TODO: 请求刷新

                        Message msg1 = new Message();
                        msg1.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg1);
                    }, 1000 * 2));
                    // 加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        mRefreshLayout.setLoadMore(false);
                        // TODO: 请求加载

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

    private List<DynamicBean> mList;

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=indexCounsel")) {          // 首页数据
                    Log.d(TAG, " data:: " + body.get("data"));
                    HomePageBean homePage = JsonParser.parseJSONObject(HomePageBean.class, body.get("data"));
                    mArrayList = new ArrayList<>();
                    for (int i = 0; i < titles.length; i++) {
                        HomePageBean page = new HomePageBean();
                        page.setNum(Objects.requireNonNull(homePage).getNum());
                        if (i == 0) {        // 联盟动态
                            List<DynamicBean> dynamicList = new ArrayList<>();
                            for (int j = 0; j < homePage.getDongtai().size() && j < Integer.parseInt(page.getNum()); j++) {
                                dynamicList.add(homePage.getDongtai().get(j));
                            }
                            page.setDongtai(dynamicList);
                        } else {             // 联盟活动
                            List<DynamicBean> dynamicList = new ArrayList<>();
                            for (int j = 0; j < homePage.getHuodong().size() && j < Integer.parseInt(page.getNum()); j++) {
                                dynamicList.add(homePage.getHuodong().get(j));
                            }
                            page.setHuodong(dynamicList);
                        }
                        mArrayList.add(page);
                    }
                    if (Contants.CLICK_POSITION_SIZE == 0x1003) {    // 联盟动态
                        mList = mArrayList.get(0).getDongtai();
                    }
                    if (Contants.CLICK_POSITION_SIZE == 0x1004) {     // 联盟活动
                        mList = mArrayList.get(1).getHuodong();
                    }

                    Message msg = new Message();
                    msg.what = REFRESH_SIZE;
                    mHandler.sendMessage(msg);
                }

                if (result.url().contains("library/mData.php?type=getDongtaiByName")){
                    mList = JsonParser.parseJSONArray(DynamicBean.class, body.get("data"));
                    Message msg = new Message();
                    msg.what = REFRESH_SIZE;
                    mHandler.sendMessage(msg);
                }
                mDialog.dismissDialog();
            } else {
                toast(body.get("warn"));
            }
        }
    }

    private String[] titles = {"联盟动态", "联盟活动"};

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(TAG, "查询条件：" + Contants.Conditions);
        if (Contants.Conditions != null){           // 有查询条件
            // 如果是带参数传递的
            mDialog = new DialogUtils(this, "查询中");
            mDialog.showDialog();
            RequestPost.getDynamicList(Contants.Conditions, this);
            Contants.Conditions = null;
        }else {             // 没有查询条件
            mDialog = new DialogUtils(this, "加载中");
            mDialog.showDialog();
            // 请求数据
            RequestPost.HomeListData(this);
        }
    }
}
