package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.domain.DoctorBean;
import com.rainwood.medicalalliance.ui.adapter.DoctorListAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/9 9:42
 * @Desc: 医院详情
 */
public final class HospitalDetailActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_hopital_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_into_website)
    private TextView mIntoWebsite;
    @ViewById(R.id.tv_navigation)
    private TextView mNavigation;
    @ViewById(R.id.tv_desc)
    private TextView mDesc;
    @ViewById(R.id.tv_desc_line)
    private TextView mDescLine;
    @ViewById(R.id.tv_doctor)
    private TextView mDoctor;
    @ViewById(R.id.tv_doctor_line)
    private TextView mDoctorLine;
    @ViewById(R.id.wv_desc)
    private WebView mWebView;
    @ViewById(R.id.mlv_doctor_list)
    private MeasureListView mDoctorList;

    @ViewById(R.id.tv_name)
    private TextView mName;
    @ViewById(R.id.tv_tel)
    private TextView mTel;
    @ViewById(R.id.tv_opens_hours)
    private TextView mOpenHours;
    @ViewById(R.id.tv_address)
    private TextView mAddress;
    @ViewById(R.id.tv_distance)
    private TextView mDistance;

    private List<DoctorBean> mDoctorBeanList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int DOCTOR_LIST_SIZE = 0x102;
    private final int HOSPITAL_DESC_SIZE = 0x103;

    @Override
    protected void initView() {
        initEvents();

        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("医院详情");
        mIntoWebsite.setOnClickListener(this);
        mNavigation.setOnClickListener(this);
        mDesc.setOnClickListener(this);
        mDoctor.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 医生团队
        mDoctorBeanList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DoctorBean doctor = new DoctorBean();
            doctor.setImg(null);
            doctor.setName("李建中");
            doctor.setDepart("神经外科");
            doctor.setDesc("副主任、主任医师、教授、博士生导师，现任中华神经外科学会脊髓脊柱专家专业委员会委员，从事神经外科工作30余年，在神经肿瘤及先天性颅脑畸形等神经系统疾病诊治方面有较丰富的临床经验，擅长于岩斜坡肿瘤、桥小脑角肿瘤、脑干肿瘤等神经系统疾病的微创外科治疗。");
            mDoctorBeanList.add(doctor);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_into_website:
                toast("进入官网");
                break;
            case R.id.tv_navigation:
                toast("导航到医院");
                break;
            case R.id.tv_desc:
                // toast("医院描述");
                mDescLine.setVisibility(View.VISIBLE);
                mDoctorLine.setVisibility(View.INVISIBLE);
                mDoctorList.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                Message descMsg = new Message();
                descMsg.what = HOSPITAL_DESC_SIZE;
                mHandler.sendMessage(descMsg);
                break;
            case R.id.tv_doctor:
                // toast("医生团队");
                mDescLine.setVisibility(View.INVISIBLE);
                mDoctorLine.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mDoctorList.setVisibility(View.VISIBLE);
                Message docMsg = new Message();
                docMsg.what = DOCTOR_LIST_SIZE;
                mHandler.sendMessage(docMsg);
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    mName.setText("陆军军医大学第一附属医院—重庆西南医院");
                    mTel.setText("023-68888888");
                    mOpenHours.setText("营业时间 08:00-20:00");
                    mAddress.setText("重庆市沙坪坝区新桥正街83号");
                    mDistance.setText("距我12.4km");

                    // 默认显示医院介绍
                    Message descMsg = new Message();
                    descMsg.what = HOSPITAL_DESC_SIZE;
                    mHandler.sendMessage(descMsg);
                    break;
                case DOCTOR_LIST_SIZE:
                    DoctorListAdapter doctorAdapter = new DoctorListAdapter(getActivity(), mDoctorBeanList);
                    mDoctorList.setAdapter(doctorAdapter);
                    break;
                case HOSPITAL_DESC_SIZE:
                    // 医院描述
                    initWebView();
                    break;
            }
        }
    };


    /**
     * web解析
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);  //支持放大缩小
        settings.setBuiltInZoomControls(true); //显示缩放按钮
        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置不让其跳转浏览器
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        // 添加客户端支持
        mWebView.setWebChromeClient(new WebChromeClient());
        // mWebView.loadUrl(TEXTURL);
        //不加这个图片显示不出来
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setBlockNetworkImage(false);
        //允许cookie 不然有的网站无法登陆
        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        mWebView.loadDataWithBaseURL(null, htmlStr,
                "text/html", "utf-8", null);
    }


    /*
  模拟数据 https://www.baidu.com/img/bd_logo.png
   */
    private String htmlStr = "<html>\n" +
            "    <head>\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "        <style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <p style=\"text-align: left;\">" +
            "\n" + " 第三军医大学西南医院座落在两山环抱、江水回绕的山城重庆，1929年始建于南京，1941年迁至重庆，1951年定名为“西南医院”，现为第三军医大学第一附属医院和第一临床医学院。\n" +
            "医院共设临床、医技科、眼科、骨科、神经外科、病理科、信息科等科室。\n" +
            "                     先后获全国百姓放心百佳示范医院、全国优质服务百佳医院、全国医院文 化建设先进单位、全国精神文明建设先进单位、全军先进医院、全军为部队服务先进单位、总后先进师旅团级党委、重庆市民看病首选医院等殊荣。       </p>\n" + "</body>\n" +
            "</html>";
}
