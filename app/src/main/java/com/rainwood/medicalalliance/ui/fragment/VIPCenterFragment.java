package com.rainwood.medicalalliance.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseDialog;
import com.rainwood.medicalalliance.base.BaseFragment;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ArticleBean;
import com.rainwood.medicalalliance.domain.VIPCenterBean;
import com.rainwood.medicalalliance.io.IOUtils;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.activity.BuyRecordActivity;
import com.rainwood.medicalalliance.ui.activity.ModifyActivity;
import com.rainwood.medicalalliance.ui.activity.VIPTypeActivity;
import com.rainwood.medicalalliance.ui.dialog.MenuDialog;
import com.rainwood.medicalalliance.utils.CameraUtil;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.scantools.android.QRCodeCaptureActivity;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.rainwood.medicalalliance.utils.CameraUtil.PHOTO_REQUEST_CAREMA;
import static com.rainwood.medicalalliance.utils.CameraUtil.RESULT_CAMERA_IMAGE;
import static com.rainwood.medicalalliance.utils.CameraUtil.uri_;

/**
 * @Author: a797s
 * @Date: 2020/3/6 17:38
 * @Desc: 会员中心
 * 1、是会员的情况：
 * 2、不是会员的情况：
 */
public final class VIPCenterFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    // 不是会员的情况
    private ImageView mHeadImg;
    private TextView mNickName;
    private LinearLayout mNickClick;
    private TextView mTips;
    private TextView mReference;            // 有推荐人的时候显示推荐人
    private TextView mScanReference;        // 没有推荐人的时候显示扫码添加推荐人
    private Button mBuyVIP;

    // 是会员的情况
    private TextView mName;
    private LinearLayout mReferees;
    private TextView mReferee;
    private WebView mWebView;
    private VIPCenterBean mVipData;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int SCAN_SIZE = 0x102;            // 扫一扫

    private ImageView mImg;
    private TextView mScanAdd;

    private DialogUtils mDialog;

    @Override
    protected int initLayout() {
        if (Contants.hasMembers) {            // 是会员的情况
            return R.layout.fragment_is_vip_center;
        } else {
            return R.layout.fragment_not_vip_center;
        }
    }

    @Override
    protected void initView(View view) {
        if (Contants.hasMembers) {       // 是会员的UI
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
            mImg = view.findViewById(R.id.iv_img);
            mImg.setOnClickListener(this);
            mName = view.findViewById(R.id.tv_name);
            mName.setOnClickListener(this);
            // 扫码添加推荐人
            mScanAdd = view.findViewById(R.id.tv_scan_add);
            mScanAdd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            // 推荐人
            mReferees = view.findViewById(R.id.ll_referees);
            mReferee = view.findViewById(R.id.tv_referees);
            // 会员特权
            mWebView = view.findViewById(R.id.wv_content);
            mScanAdd.setOnClickListener(this);
            LinearLayout buyRecord = view.findViewById(R.id.ll_buy_record);
            buyRecord.setOnClickListener(this);
            Button buyVip = view.findViewById(R.id.btn_buy_vip);
            buyVip.setOnClickListener(this);

        } else {                        // 不是会员的UI
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
            mHeadImg = view.findViewById(R.id.iv_head);
            mHeadImg.setOnClickListener(this);
            mNickName = view.findViewById(R.id.tv_name);
            mNickClick = view.findViewById(R.id.ll_nick);
            mWebView = view.findViewById(R.id.wv_content);
            mTips = view.findViewById(R.id.tv_tips);
            mReference = view.findViewById(R.id.tv_reference);
            mScanReference = view.findViewById(R.id.tv_scan_reference);
            mNickClick.setOnClickListener(this);
            mScanReference.setOnClickListener(this);
            mBuyVIP = view.findViewById(R.id.btn_buy_vip);
            mBuyVIP.setOnClickListener(this);
        }
    }

    @Override
    protected void initData(Context mContext) {
        // 初始化请求接口
        mDialog = new DialogUtils(getContext(), "加载中");
        mDialog.showDialog();
        RequestPost.VIPData(this);
    }

    @Override
    public void onClick(View v) {
        if (Contants.hasMembers) {           // -- 是会员的情况
            switch (v.getId()) {
                case R.id.iv_img:
                    // toast("头像上传");
                    imageSelector();
                    break;
                case R.id.tv_scan_add:
                    // toast("扫码添加推荐人");
                    Message msg = new Message();
                    msg.what = SCAN_SIZE;
                    mHandler.sendMessage(msg);
                    break;
                case R.id.btn_buy_vip:
                    // toast("立即购买VIP");
                    startActivity(VIPTypeActivity.class);
                    break;
                case R.id.ll_buy_record:
                    // toast("购买记录");
                    startActivity(BuyRecordActivity.class);
                    break;
                case R.id.tv_name:
                    // toast("修改资料");
                    startActivity(ModifyActivity.class);
                    break;
            }
        } else {                    // --- 不是会员的情况
            switch (v.getId()) {
                case R.id.ll_nick:
                    //toast("用户信息");
                    startActivity(ModifyActivity.class);
                    break;
                case R.id.tv_scan_reference:
                    //toast("扫码添加推荐人");
                    Message msg = new Message();
                    msg.what = SCAN_SIZE;
                    mHandler.sendMessage(msg);
                    break;
                case R.id.btn_buy_vip:
                    //toast("购买VIP");
                    startActivity(VIPTypeActivity.class);
                    break;
                case R.id.iv_head:
                    // toast("头像上传");
                    imageSelector();
                    break;
            }
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
                    if (Contants.mLoginner != null) {
                        if (!Contants.hasMembers) {         // 不是会员的时候
                            Log.d(TAG, "头像：" + Contants.ROOT_URI + mVipData.getHeadSrc());
                            Glide.with(getContext()).load(Contants.ROOT_URI + mVipData.getHeadSrc())
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                                    .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                                    .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                                    .into(mHeadImg);
                            initWebView();
                            // 昵称
                            if (TextUtils.isEmpty(mVipData.getContactName())) {
                                mNickName.setText("暂时没有设置昵称");
                            } else {
                                mNickName.setText(mVipData.getContactName());
                            }
                            if ("0".equals(Contants.mLoginner.getIfKehu())) {        // 不是会员
                                mTips.setText("你目前还不是VIP会员");
                            }
                        } else {                             // 是会员的情况
                            // 头像
                            Glide.with(Objects.requireNonNull(getActivity()))
                                    .load(Contants.ROOT_URI + mVipData.getHeadSrc())
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                                    .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                                    .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(mImg);
                            // vip特权解析
                            initWebView();
                            // 昵称
                            if (TextUtils.isEmpty(mVipData.getContactName())) {
                                mName.setText("暂时没有设置昵称");
                            } else {
                                mName.setText(mVipData.getContactName());
                            }
                            // 推荐人
                            if (mVipData.getSharePeople() == null) {
                                mScanAdd.setVisibility(View.VISIBLE);
                                mReferees.setVisibility(View.GONE);
                            } else {
                                mScanAdd.setVisibility(View.GONE);
                                mReferees.setVisibility(View.VISIBLE);
                                mReferee.setText("推荐人：" + mVipData.getSharePeople().getCompanyName());
                            }
                        }
                    } else {
                        Log.i(TAG, "loginner is empty!!!");
                    }
                    break;
                case SCAN_SIZE:
                    // 先获取相机权限
                    XXPermissions.with(getActivity())
                            // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                            .constantRequest()
                            // 不指定权限则自定获取订单中的危险权限
                            .permission(Permission.CAMERA)
                            .request(new OnPermission() {
                                @Override
                                public void hasPermission(List<String> granted, boolean isAll) {
                                    if (isAll) {
                                        // 去扫码
                                        Intent intent = new Intent(getContext(), QRCodeCaptureActivity.class);
                                        // 设置标题栏的颜色
                                        intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                                        startActivityForResult(intent, Contants.SCANCHECKCODE);
                                    } else {
                                        toast("获取权限成功，部分权限未正常授予");
                                    }
                                }

                                @Override
                                public void noPermission(List<String> denied, boolean quick) {
                                    if (quick) {
                                        toast("被永久拒绝授权，请手动授予权限");
                                        //如果是被永久拒绝就跳转到应用权限系统设置页面
                                        XXPermissions.gotoPermissionSettings(getActivity());
                                    } else {
                                        toast("获取权限失败");
                                    }
                                }
                            });
                    break;
            }
        }
    };

    /**
     * 图片选择器
     */
    private String[] selectors = {"相机", "相册"};

    private void imageSelector() {
        List<String> data = new ArrayList<>(Arrays.asList(selectors));
        // 先权限检查
        XXPermissions.with(getActivity())
                .constantRequest()
                .permission(Permission.Group.STORAGE)       // 读写权限
                .permission(Permission.CAMERA)              // 相机权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            new MenuDialog.Builder(getActivity())
                                    // 设置null 表示不显示取消按钮
                                    .setCancel(R.string.common_cancel)
                                    // 设置点击按钮后不关闭弹窗
                                    .setAutoDismiss(false)
                                    // 显示的数据
                                    .setList(data)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MenuDialog.OnListener<String>() {
                                        @Override
                                        public void onSelected(BaseDialog dialog, int position, String text) {
                                            dialog.dismiss();
                                            switch (position) {
                                                case 0:         // 拍照
                                                    //toast("相机");
                                                    CameraUtil.openCamera(VIPCenterFragment.this);
                                                    break;
                                                case 1:         // 相册
                                                    // toast("相册");
                                                    gallery();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, RESULT_CAMERA_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAMERA_IMAGE:                                       // 从相册返回的数据
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        // String path = CameraUtil.getPath(getContext(), uri);
                        // uploadImg(path);
                        File file = IOUtils.decodeUri(getActivity(), uri);
                        File file1 = CameraUtil.getFileFromUri(uri, getContext());
                        RequestPost.UploadFile(file1, this);
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:                     //照的相片  //图片解析成Bitmap对象 -- uri转换成Bitmap对象的时候会出现图片旋转的问题
                    Log.d(TAG, "11111");
                    if (data != null) {          // 相机可能尚未指定intent.puExtra(MediaStore.EXTRA_OUTPUT, uri);
                        if (data.hasExtra("data")) {     // 返回有缩略图
                            // 得到bitmap后处理、如压缩...
                            Bitmap bitmap = data.getParcelableExtra("data");
                            Glide.with(getContext()).load(bitmap)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(mImg);
                        } else {     // 如果返回的不是缩略图，则直接获取地址
                            Bitmap bitmap = IOUtils.decodeUri(getContext(), uri_);
                            Glide.with(getContext())
                                    .load(bitmap)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(mImg);
                            File file = IOUtils.decodeUri(getActivity(), uri_);
                            RequestPost.UploadFile(file, this);
                        }
                    }
//                    uploadImg(file);
                    break;
            }
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, "result ---" + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=kehuMx")) {          // 个人中心数据
                    mVipData = JsonParser.parseJSONObject(VIPCenterBean.class, body.get("data"));
                    mDialog.dismissDialog();
                    // VIP特权解析
                    if (mVipData != null) {
                        for (ArticleBean article : mVipData.getArticle()) {
                            if (article.getImg() != null && !"".equals(article.getImg())) {      // 如果文章为空，则必然是图片
                                content.append("<img src=\"" + Contants.ROOT_URI + article.getImg() + "\" alt=\"" + article.getType() + "\"" + "/>\n");
                            } else {
                                content.append(article.getWord() + "\r\n");
                            }
                        }
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
//                    Log.d(TAG, " --- data -- " + body.get("data"));
                }

                if (result.url().contains("library/mData.php?type=uplopImg")) {          // 图片上传
                    //
                    toast("修改成功");
                    String path = JsonParser.parseJSONObject(body.get("data")).get("src");
                    Glide.with(getContext()).load(path)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                            .into(mImg);
                }
            } else {
                toast(body.get("warn"));
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
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);  //支持放大缩小
        settings.setBuiltInZoomControls(false); //显示缩放按钮
        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
        // Log.d(TAG, " --- " + startStr + content + endStr);
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
