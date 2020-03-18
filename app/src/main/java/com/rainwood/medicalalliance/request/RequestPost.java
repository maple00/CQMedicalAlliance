package com.rainwood.medicalalliance.request;

import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.okhttp.OkHttp;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.okhttp.RequestParams;

import java.io.File;
import java.util.List;

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
    public static void AccountLogin(String account, String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("loginTel", account);
        params.add("password", pwd);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=login", params, listener);
    }

    /**
     * 注册-输入手机号获取验证码（未注册的手机号）
     */
    public static void RegisterTel(String tel, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=inGetCaptcha", params, listener);
    }

    /**
     * 手机账号注册验证码验证
     */
    public static void RegisterVerifyCheck(String verify, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("prove", verify);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=InProve", params, listener);
    }

    /**
     * 输入注册密码
     */
    public static void RegisterPwdCheck(String password, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("password", password);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=inPassword", params, listener);
    }

    /**
     * 获取验证码
     */
    public static void getVerifyCode(String tel, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getCaptcha", params, listener);
    }

    /**
     * 首页
     */
    public static void HomeListData(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=indexCounsel", params, listener);
    }

    /**
     * 首页轮播图 library/mData.php?type=getImgShou
     */
    public static void HomeShuffling(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getImgShou", params, listener);
    }

    /**
     * 个人中心数据请求
     */
    public static void VIPData(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=kehuMx", params, listener);
    }

    /**
     * 购买记录
     */
    public static void BuyRecord(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("khid", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=buyRecord", params, listener);
    }

    /**
     * 购买会员的类型
     */
    public static void BuyCardType(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getVipCard", params, listener);
    }

    /**
     * 查询会员卡详情
     */
    public static void VIPCardDetail(String customId, String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("khMxId", customId);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=cardMX", params, listener);
    }

    /**
     * 续费记录
     */
    public static void VIPRenewalRecord(String customId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("khMxId", customId);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=renewRecord", params, listener);
    }

    /**
     * 退出登录
     */
    public static void logout(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=logOut", params, listener);
    }

    /**
     * 修改登录密码
     */
    public static void modifyLoginPwd(String verify, String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("prove", verify);
        params.add("pas", pwd);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=logOut", params, listener);
    }

    /**
     * 获取免责条款
     */
    public static void getDisclaimer(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getExceptions", params, listener);
    }

    /**
     * 请求医院列表
     */
    public static void getHospitalList(String searchName, String province, String city, String area, String lat, String lng, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("name", searchName);             // 模糊搜索名称
        params.add("province", province);           // 省
        params.add("city", city);                   // 市
        params.add("area", area);                   // 区县
        params.add("lat", lat);                     // 纬度
        params.add("lng", lng);                     // 经度
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getHospitalList", params, listener);
    }

    /**
     * 医院详情
     */
    public static void getHospitalDetial(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getHospitalById", params, listener);
    }

    /**
     * 请求医生列表
     */
    public static void getDoctorList(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getDoctorByHid", params, listener);
    }

    /**
     * 动态搜索 getDongtaiByName
     */
    public static void getDynamicList(String name, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("name", name);
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getDongtaiByName", params, listener);
    }

    /**
     * 新增会员信息 --- 家庭
     */
    public static void addVIPInfos(String type, String name, String sex, String idCard, String idCardFront,
                                   String idCardBack, String pwd, String isRead, String tel,
                                   String residence, List<String> subResideceLists, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("type", type);                       // 购买的VIP的类型
        params.add("name", name);                       // 姓名
        params.add("sex", sex);                         // 性别
        params.add("idCard", idCard);                    // 身份证号
        params.add("idCardFrontSrc", idCardFront);      // 身份证正面
        params.add("idCardBackSrc", idCardBack);        // 身份证背面
        params.add("cardPassword", pwd);                // 会员卡密码
        params.add("read", isRead);                     // 是否阅读了免责条款
        params.add("tel", tel);                         // 电话号码
        params.add("bookletInFront", residence);        // 户口本主页
        for (String resideceList : subResideceLists) {
            params.add("bookletZi[]", resideceList);           // 户口本子页
        }
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=homeMessage", params, listener);
    }

    /**
     * 新增会员信息 --- 个人
     */
    public static void getPersonalVIP(String type, String name, String sex, String idCard, String idCardFront, String idCardBack,
                                      String pwd, String isRead, String tel, OnHttpListener listener ){
        RequestParams params = new RequestParams();
        params.add("type", type);                       // 购买的VIP的类型
        params.add("name", name);                       // 姓名
        params.add("sex", sex);                         // 性别
        params.add("idCard", idCard);                    // 身份证号
        params.add("idCardFrontSrc", idCardFront);      // 身份证正面
        params.add("idCardBackSrc", idCardBack);        // 身份证背面
        params.add("cardPassword", pwd);                // 会员卡密码
        params.add("read", isRead);                     // 是否阅读了免责条款
        params.add("tel", tel);                         // 电话号码
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=personalMessage", params, listener);
    }

    /**
     * 购买会员 -- 阿里云人脸识别
     */

    /**
     * 活体人脸请求verifyToken
     */
    public static void getVerifyToken(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "library/mData.php?type=getDongtaiByName", params, listener);
    }
}
