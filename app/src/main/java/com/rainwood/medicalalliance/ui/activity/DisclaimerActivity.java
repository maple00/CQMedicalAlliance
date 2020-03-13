package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
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
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/10 10:31
 * @Desc: 免责条款
 */
public final class DisclaimerActivity extends BaseActivity implements View.OnClickListener {
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

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("免责条款");

        initWebView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
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
            "<img src=\"https://www.baidu.com/img/bd_logo.png\"\n" +
            "             title=\"1541054060899024343.jpg\" alt=\"4.jpg\" />" +
            "<img src=\"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1383902695,1129447956&fm=26&gp=0.jpg\"\n" +
            "             title=\"1541054054758015328.jpg\" alt=\"1.jpg\" />" +
            "<img src=\"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2353449440,2528668120&fm=26&gp=0.jpg\"\n" +
            "             title=\"1541054057414099008.jpg\" alt=\"2.jpg\" />" +
            "<img src=\"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3446458559,1680525880&fm=26&gp=0.jpg\"\n" +
            "             title=\"1541054060899024343.jpg\" alt=\"3.jpg\" />" +
            "</p>\n" + "</body>\n" +
            "</html>";
}
