package com.rainwood.medicalalliance.request;

import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.okhttp.OkHttp;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.okhttp.RequestParams;

/**
 * @Author: a797s
 * @Date: 2020/3/10 17:01
 * @Desc: 后台数据请求
 */
public final class RequestPost {

    /**
     * 启动页广告栏获取
     */
    public static void Advert(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getImgIndex", params, listener);
    }

    /**
     * 账号密码登录
     */
    public static void AccountLogin(String account ,String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("loginTel", account);
        params.add("password", pwd);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=login", params, listener);
    }

    /**
     * 手机号注册 --- 登录注册发送手机验证码
     */
    public static void RegisterTel(String tel, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=inGetCaptcha",params, listener);
    }

    /**
     * 手机账号注册验证码验证
     */
    public static void RegisterVerifyCheck(String verify, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("prove", verify);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=InProve",params, listener);
    }

    /**
     * 输入注册密码
     */
    public static void RegisterPwdCheck(String password, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("password", password);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=inPassword",params, listener);
    }

    /**
    * 首页
     */
    public static void HomeListData(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=indexCounsel",params, listener);
    }

    /**
     * 首页轮播图 library/mData.php?type=getImgShou
     */
    public static void HomeShuffling(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getImgShou",params, listener);
    }

    /**
     * 个人中心数据请求
     */
    public static void VIPData(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=kehuMx",params, listener);
    }

    /**
     * 购买记录
     */
    public static void BuyRecord(String id, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("khid", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=buyRecord",params, listener);
    }

    /**
     * 购买会员的类型
     */
    public static void BuyCardType(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getVipCard",params, listener);
    }

    /**
     * 查询会员卡详情
     */
    public static void VIPCardDetail(String customId, String id,OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("khMxId", customId);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getVipCard",params, listener);
    }

    /**
     * 续费记录
     */
    public static void VIPRenewalRecord(String customId, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("khMxId", customId);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=renewRecord",params, listener);
    }
}
