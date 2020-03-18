package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ArticleBean;
import com.rainwood.medicalalliance.domain.DoctorBean;
import com.rainwood.medicalalliance.domain.HospitalBean;
import com.rainwood.medicalalliance.domain.HospitalDetailBean;
import com.rainwood.medicalalliance.domain.HospitalDetailImgBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.DoctorListAdapter;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author: a797s
 * @Date: 2020/3/9 9:42
 * @Desc: 医院详情
 */
public final class HospitalDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener, AMapLocationListener, LocationSource {


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
    // 轮播图
    @ViewById(R.id.xvp_banner)
    private XBanner mXBanner;
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
    // 高德地图
    @ViewById(R.id.mv_map)
    private MapView mMapView;
    private AMap mMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng myLocation;

    private List<DoctorBean> mDoctorBeanList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int DOCTOR_LIST_SIZE = 0x102;
    private final int HOSPITAL_DESC_SIZE = 0x103;

    private HospitalBean mHospital;

    // 详情数据
    private HospitalDetailBean mData;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        initEvents();
        mHospital = (HospitalBean) this.getIntent().getSerializableExtra("hospital");
        if (mHospital != null) {
            // TODO： 请求医院详情
            RequestPost.getHospitalDetial(mHospital.getId(), this);
            // TODO: 请求医生列表
            RequestPost.getDoctorList(mHospital.getId(), this);
        } else {

        }
        mMapView.onCreate(getSavedInstanceState());
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
        mDialog = new DialogUtils(this, "加载中");
        mDialog.showDialog();
    }

    private void initMap(double latitude, double longitude) {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
        mMap.setLocationSource(this);               // 设置定位监听
        CameraPosition cameraPosition = new CameraPosition(new LatLng(latitude, longitude), 13, 0, 30);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
        drawMarkers(latitude, longitude);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_into_website:
                // toast("进入官网");
                String website = mData.getWebsite();
                String webRex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
                Pattern httpPattern = Pattern.compile(webRex);
                if (httpPattern.matcher(website).matches()) {         // 这是一个网址
                    Uri uri = Uri.parse(website);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    toast("这不是一个网址！！！ 请确认");
                    return;
                }
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
                    // 轮播图
                    if (mXBanner != null) {
                        mXBanner.removeAllViews();
                        try {
                            mXBanner.setData(mData.getLunBoImg(), null);                        // 为XBanner绑定数据
                        } catch (Exception e) {
                            toast("网卡了");
                            for (int i = 0; i < 5; i++) {
                                HospitalDetailImgBean shuffling = new HospitalDetailImgBean();
                                shuffling.setSrc(String.valueOf(R.drawable.icon_loading_fail));
                                mData.getLunBoImg().add(shuffling);
                            }
                        }
                        mXBanner.setmAdapter((banner, view, position) ->             // XBanner适配数据
                                Glide.with(Objects.requireNonNull(getActivity())).
                                        load(Contants.ROOT_URI + mData.getLunBoImg().get(position).getSrc()).into((ImageView) view));
                        mXBanner.setPageTransformer(Transformer.Rotate);               // 设置XBanner的页面切换特效
                        mXBanner.setPageChangeDuration(1000);                        // 设置XBanner页面切换的时间，即动画时长
                    }
                    // 数据
                    mName.setText(mData.getName());
                    mTel.setText(mData.getLinkTel());
                    mOpenHours.setText(mData.getOpeningTime());
                    mAddress.setText(mData.getAddress());
                    mDistance.setText("距我" + (mHospital.getDistance() == null ? 0 : mHospital.getDistance()));

                    // 默认显示医院介绍
                    Message descMsg = new Message();
                    descMsg.what = HOSPITAL_DESC_SIZE;
                    mHandler.sendMessage(descMsg);
                    break;
                case DOCTOR_LIST_SIZE:
                    Log.d(TAG, "医生列表：" + mDoctorBeanList);
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
        mWebView.loadDataWithBaseURL(null, startStr + contentStr + endStr,
                "text/html", "utf-8", null);
    }

    private StringBuffer contentStr = new StringBuffer();
    private String startStr = "<html>\n" +
            "    <head>\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "        <style>img{max-width: 100%; width:100%; margin:15px 0;height:auto;}*{margin:0px;}</style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "    <p style=\"text-align: left;\">";
    private String endStr = "</p>\n" + "</body>\n" + "</html>";

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                mDialog.dismissDialog();
                if (result.url().contains("library/mData.php?type=getHospitalById")) {                  // 请求医院数据
                    mData = JsonParser.parseJSONObject(HospitalDetailBean.class, body.get("data"));
                    Log.d(TAG, " --- " + mData.toString());
                    /**
                     * 定位出地址的位置
                     */
                    if (TextUtils.isEmpty(mData.getAddress() + mData.getName())) {
                        toast("请确认定位的地址哦~~");
                    } else {
                        getLatlon(mData.getAddress() + mData.getName());
                    }

                    // 文章内容
                    for (ArticleBean article : mData.getArticle()) {
                        if (article.getImg() != null && !"".equals(article.getImg())) {      // 如果文章为空，则必然是图片
                            contentStr.append("<img src=\"" + Contants.ROOT_URI + article.getImg() + "\" alt=\"" + article.getType() + "\"" + "/>\n");
                        } else {
                            contentStr.append(article.getWord() + "\r\n");
                        }
                    }

                    // 数据解析
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                if (result.url().contains("library/mData.php?type=getDoctorByHid")) {               // 请求医生列表
                    mDoctorBeanList = JsonParser.parseJSONArray(DoctorBean.class, body.get("data"));
                    Log.d(TAG, "医生列表：" + body.get("data"));
                }
            } else {
                toast(body.get("data"));
            }
        }
    }

    /**
     * 高德地图定位
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            if (mListener != null) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
            //获取当前经纬度坐标
            myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

           /* //定位成功回调信息，设置相关消息
            Log.d(TAG, "当前定位结果来源-----" + aMapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
            Log.d(TAG, "纬度 ----------------" + aMapLocation.getLatitude());//获取纬度
            Log.d(TAG, "经度-----------------" + aMapLocation.getLongitude());//获取经度
            Log.d(TAG, "精度信息-------------" + aMapLocation.getAccuracy());//获取精度信息
            Log.d(TAG, "地址-----------------" + aMapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            Log.d(TAG, "国家信息-------------" + aMapLocation.getCountry());//国家信息
            Log.d(TAG, "省信息---------------" + aMapLocation.getProvince());//省信息
            Log.d(TAG, "城市信息-------------" + aMapLocation.getCity());//城市信息
            Log.d(TAG, "城区信息-------------" + aMapLocation.getDistrict());//城区信息
            Log.d(TAG, "街道信息-------------" + aMapLocation.getStreet());//街道信息
            Log.d(TAG, "街道门牌号信息-------" + aMapLocation.getStreetNum());//街道门牌号信息
            Log.d(TAG, "城市编码-------------" + aMapLocation.getCityCode());//城市编码
            Log.d(TAG, "地区编码-------------" + aMapLocation.getAdCode());//地区编码
            Log.d(TAG, "当前定位点的信息-----" + aMapLocation.getAoiName());//获取当前定位点的AOI信息*/
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationOption.setOnceLocation(true);//只定位一次
            mLocationOption.setHttpTimeOut(2000);
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//开始定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    private void getLatlon(String cityName) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                            geocodeResult.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                        double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                        String adcode = geocodeAddress.getAdcode();//区域编码
                        // 显示定位
                        initMap(latitude, longititude);
                    } else {
                        Toast.makeText(HospitalDetailActivity.this, "地名出错", Toast.LENGTH_SHORT).show();
//                        ToastUtils.show(context,"地址名出错");
                    }
                }
            }
        });
        GeocodeQuery geocodeQuery = new GeocodeQuery(cityName.trim(), "29");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }

    //画定位标记图
    public void drawMarkers(double latitude, double longitude) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_address))
                .draggable(true);
        Marker marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    // 方法重写
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationOption = null;
        mLocationClient = null;
        mMap = null;
        mMapView.onDestroy();
    }

}
