package com.rainwood.medicalalliance.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseDialog;
import com.rainwood.medicalalliance.base.BaseFragment;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.HospitalBean;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.activity.HospitalDetailActivity;
import com.rainwood.medicalalliance.ui.activity.SearchViewActivity;
import com.rainwood.medicalalliance.ui.adapter.HospitalListAdapter;
import com.rainwood.medicalalliance.ui.dialog.AddressDialog;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.medicalalliance.utils.ListUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/6 17:36
 * @Desc: 医院介绍
 */
public final class HospitalDescFragment extends BaseFragment implements View.OnClickListener, AMapLocationListener, LocationSource, OnHttpListener {

    private MeasureListView mContentList;
    private TextView mLocation;
    private ImageView mArrow;

    // 高德地图
    private MapView mMapView;
    private AMap mMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng myLocation;
    // 默认值
    private String mProvince;       // 默认省
    private String mCity;           // 默认市
    private String mArea;           // 默认区
    private String mAddress;        // 默认地址
    private double mLat;            // 纬度
    private double mLng;            // 经度

    private DialogUtils mDialog;
    private List<HospitalBean> mList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private ClearEditText mSearch;

    @Override
    protected int initLayout() {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        return R.layout.fragment_hospital_desc;
    }

    @Override
    protected void initView(View view) {
        mContentList = view.findViewById(R.id.mlv_content_list);
        mLocation = view.findViewById(R.id.tv_location);
        mArrow = view.findViewById(R.id.iv_arrow);
        mSearch = view.findViewById(R.id.et_search);
        mMapView = view.findViewById(R.id.mv_map);
        initEvents();
    }

    private void initEvents() {
        mLocation.setOnClickListener(this);
        mArrow.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSearch.setFocusable(false);
        mSearch.setFocusableInTouchMode(false);
    }

    /**
     * 初始化权限
     */
    private void initPermissions() {
        XXPermissions.with(getActivity())
                .constantRequest()
                .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)       // 粗略定位、精确定位
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {    // 权限获取之后，获取定位信息
                            initMap();
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

    private void initMap() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
        mMap.setLocationSource(this);// 设置定位监听
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    protected void initData(Context mContext) {
        // 初始化定位权限
        mDialog = new DialogUtils(getContext(), "定位中");
        mDialog.showDialog();
        initPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
            case R.id.iv_arrow:
                // 地区选择对话框
                new AddressDialog.Builder(getActivity())
                        .setTitle(getString(R.string.address_title))
                        // 设置默认省份   -- 默认城市通过定位显示出来
                        .setProvince(mProvince)
                        // 设置默认城市(必须先设置默认省份)
                        .setCity(mCity)
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener(new AddressDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                dialog.dismiss();
                                mLocation.setText(province);
                                // TODO: 查询先择的省市区范围内的医院list
                                mDialog.showDialog();
                                // 请求列表数据
                                RequestPost.getHospitalList("", mProvince, mCity, mArea,
                                        String.valueOf(mLat), String.valueOf(mLng), HospitalDescFragment.this);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.et_search:                // 搜索医院
                Intent intent = new Intent(getContext(), SearchViewActivity.class);
                startActivityForResult(intent, Contants.HOSPITAL_RESULT_CODE);
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
                    HospitalListAdapter hospitalListAdapter = new HospitalListAdapter(getActivity(), mList);
                    mContentList.setAdapter(hospitalListAdapter);
                    hospitalListAdapter.setOnClickItem(position -> {
                        // TODO: 查看详情 -- 带id
                        Intent intent = new Intent(getActivity(), HospitalDetailActivity.class);
                        intent.putExtra("hospital", mList.get(position));
                        startActivity(intent);
                    });
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            if (mListener != null) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
            //获取当前经纬度坐标
            myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mProvince = aMapLocation.getProvince();
            mCity = aMapLocation.getCity();
            mArea = aMapLocation.getDistrict();
            mAddress = aMapLocation.getAddress();
            mLat = aMapLocation.getLatitude();
            mLng = aMapLocation.getLongitude();

            // 默认地址
            mLocation.setText(mProvince);
            // TODO: 查询省市区内的医院list
            // 请求列表数据
            // 如果有搜索条件，则是从搜索页面返回滴
            Log.d(TAG, "查询条件：" + Contants.Conditions);
            mDialog.dismissDialog();
            if (Contants.Conditions != null){
                mDialog = new DialogUtils(getContext(), "查询中");
                mDialog.showDialog();
                RequestPost.getHospitalList(Contants.Conditions, mProvince, mCity, mArea,
                        String.valueOf(mLat), String.valueOf(mLng), this);
                // 将查询条件使用完成后置空
                Contants.Conditions = null;
            }else {
                mDialog = new DialogUtils(getContext(), "加载中");
                mDialog.showDialog();
                RequestPost.getHospitalList("", mProvince, mCity, mArea,
                        String.valueOf(mLat), String.valueOf(mLng), this);
            }
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
            mLocationOption.setOnceLocation(true);          //只定位一次
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

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getHospitalList")) {          // 获取医院列表
                    mList = JsonParser.parseJSONArray(HospitalBean.class, body.get("data"));
                    if (ListUtils.getSize(mList) == 0){
                        toast("当前查询无效！！！");
                    }else {
                        Message msg = new Message();
                        msg.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg);
                    }
                    mDialog.dismissDialog();
                }
            } else {
                toast(body.get("warn"));
                postDelayed(() -> mDialog.dismissDialog(), 500);
            }
        }
    }
}
