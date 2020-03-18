package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.DisclaimerBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.medicalalliance.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/10 10:31
 * @Desc: 免责条款
 */
public final class DisclaimerActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_disclsimer;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.wv_disclaimer)
    private WebView mWebView;

    private List<DisclaimerBean> mList;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("免责条款");

    }

    @Override
    protected void initData() {
        super.initData();
        mDialog = new DialogUtils(this, "加载中");
        RequestPost.getDisclaimer(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getExceptions")) {            // 获取免责条款
                    mList = JsonParser.parseJSONArray(DisclaimerBean.class, body.get("data"));
                    Log.d(TAG, "免责条款" + body.get("data"));
                    for (DisclaimerBean disclaimerBean : mList) {
                        if (!TextUtils.isEmpty(disclaimerBean.getImg())) {    // 如果图片为空
                            content.append("<img src = \"" + Contants.ROOT_URI + disclaimerBean.getImg() + "\">\n");
                        } else {
                            content.append(disclaimerBean.getWord());
                        }
                    }
                    // 显示免责条款
                    initWebView();
                    mDialog.dismissDialog();
                }
            }
        }
    }

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
        settings.setLoadWithOverviewMode(false);
        settings.setSupportZoom(true);  //支持放大缩小
        settings.setBuiltInZoomControls(false); //显示缩放按钮
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
        mWebView.loadDataWithBaseURL(null, startStr + content + endStr,
                "text/html", "utf-8", null);
    }

    private StringBuffer content = new StringBuffer();
    private String startStr = "<html>\n" +
            "    <head>\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "        <style>img{max-width: 100%; width:100%; margin:15px 0;height:auto;}*{margin:0px;}</style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "    <p style=\"text-align: left;\">";
    private String endStr = "</p>\n" + "</body>\n" + "</html>";
}
