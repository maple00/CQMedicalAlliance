package com.rainwood.medicalalliance.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseFragment;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.DynamicBean;
import com.rainwood.medicalalliance.domain.HomePageBean;
import com.rainwood.medicalalliance.domain.PressBean;
import com.rainwood.medicalalliance.domain.ShufflingBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.activity.AllianceActivity;
import com.rainwood.medicalalliance.ui.activity.ContentActivity;
import com.rainwood.medicalalliance.ui.activity.LoginMainActivity;
import com.rainwood.medicalalliance.ui.adapter.HomeContentAdapter;
import com.rainwood.medicalalliance.ui.adapter.TopTypeAdapter;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureListView;
import com.rainwood.tools.widget.XCollapsingToolbarLayout;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/3/6 13:27
 * @Desc: 首页
 */
public final class HomeFragment extends BaseFragment implements OnHttpListener {

    @Override
    protected int initLayout() {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        return R.layout.fragment_home;
    }

    private XBanner mBanner;
    private GridView mTopType;
    private MeasureListView mContentList;
    private XCollapsingToolbarLayout mCollapsingToolbarLayout;

    private DialogUtils mDialog;
    // mHandler
    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView(View view) {
        mBanner = view.findViewById(R.id.xvp_banner);
        mTopType = view.findViewById(R.id.gv_top_type);
        mContentList = view.findViewById(R.id.mlv_content_list);
        mCollapsingToolbarLayout = view.findViewById(R.id.ctl_collapsing_bar);
        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener((layout, shown) -> {
            if (shown) {         // 悬浮了 -- 设置状态栏字体为白色
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
            } else {
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
            }
        });

    }

    @Override
    protected void initData(Context mContext) {
        mDialog = new DialogUtils(getContext(), "加载中");
        // XBanner
        RequestPost.HomeShuffling(this);
        // top分类
        mTopItemList = new ArrayList<>();
        for (int i = 0; i < desc.length; i++) {
            PressBean item = new PressBean();
            item.setImgPath(String.valueOf(topImgs[i]));
            item.setName(desc[i]);
            mTopItemList.add(item);
        }
        // content
        mDialog.showDialog();
        RequestPost.HomeListData(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    // XBanner
                    if (mBanner != null) {
                        mBanner.removeAllViews();
                        try {
                            mBanner.setData(imgagesList, null);                        // 为XBanner绑定数据
                        } catch (Exception e) {
                            toast("网卡了");
                            imgagesList = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                ShufflingBean shuffling = new ShufflingBean();
                                shuffling.setSrc(String.valueOf(R.drawable.icon_loading_fail));
                                imgagesList.add(shuffling);
                            }
                        }
                        mBanner.setmAdapter((banner, view, position) ->             // XBanner适配数据
                                Glide.with(Objects.requireNonNull(getActivity())).
                                        load(Contants.ROOT_URI + imgagesList.get(position).getSrc()).into((ImageView) view));
                        mBanner.setPageTransformer(Transformer.Cube);               // 设置XBanner的页面切换特效
                        mBanner.setPageChangeDuration(1000);                        // 设置XBanner页面切换的时间，即动画时长
                    }

                    // topType
                    TopTypeAdapter topTypeAdapter = new TopTypeAdapter(getActivity(), mTopItemList);
                    mTopType.setNumColumns(3);
                    mTopType.setAdapter(topTypeAdapter);
                    topTypeAdapter.setOnClickItem(position -> {
                        switch (position) {
                            case 0:             // 联盟动态
                                Contants.CLICK_POSITION_SIZE = 0x1003;
                                startActivity(AllianceActivity.class);
                                break;
                            case 1:             // 联盟活动
                                Contants.CLICK_POSITION_SIZE = 0x1004;
                                startActivity(AllianceActivity.class);
                                break;
                            case 2:             // 激活会员
                                toast("激活会员");
                                break;
                        }
                    });
                    // contentList
                    HomeContentAdapter contentAdapter = new HomeContentAdapter(getActivity(), mList);
                    mContentList.setAdapter(contentAdapter);
                    // 查看更多
                    contentAdapter.setClickItem(position -> {
                        if (position == 0) {
                            Contants.CLICK_POSITION_SIZE = 0x1003;
                        } else {
                            Contants.CLICK_POSITION_SIZE = 0x1004;
                        }
                        startActivity(AllianceActivity.class);
                    });
                    // 查看详情
                    contentAdapter.setSubItem((parentPos, position) -> {
                        if (parentPos == 0) {
                            Contants.CLICK_POSITION_SIZE = 0x1003;
                        } else {
                            Contants.CLICK_POSITION_SIZE = 0x1004;
                        }
                        startActivity(ContentActivity.class);
                    });
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
                if (result.url().contains("library/mData.php?type=indexCounsel")) {          // 首页数据
                    HomePageBean homePage = JsonParser.parseJSONObject(HomePageBean.class, body.get("data"));
                    mList = new ArrayList<>();
                    for (int i = 0; i < titles.length; i++) {
                        HomePageBean page = new HomePageBean();
                        page.setTitle(titles[i]);
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
                        mList.add(page);
                    }
                    mDialog.dismissDialog();

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                } else if (result.url().contains("library/mData.php?type=getImgShou")) {              // 首页轮播图
                    imgagesList = JsonParser.parseJSONArray(ShufflingBean.class, body.get("data"));

                }
            } else {
                toast(body.get("warn"));
                if ("未登录".equals(body.get("warn"))){
                    startActivity(LoginMainActivity.class);
                }
            }
        }
    }

    private List<ShufflingBean> imgagesList;

    private void setXBanner() {
        // 请求轮播图的src
    }

    private List<PressBean> mTopItemList;
    private String[] desc = {"联盟动态", "联盟活动", "激活会员"};
    private int[] topImgs = {R.drawable.ic_trends, R.drawable.ic_activity, R.drawable.ic_vip,};
    private List<HomePageBean> mList;
    private String[] titles = {"联盟动态", "联盟活动"};

}
