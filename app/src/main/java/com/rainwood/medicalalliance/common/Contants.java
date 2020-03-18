package com.rainwood.medicalalliance.common;

import com.rainwood.medicalalliance.domain.LoginnerBean;

/**
 * @Author: a797s
 * @Date: 2020/3/6 12:59
 * @Desc: 常量
 */
public final class Contants {

    /**
     * 记录点击不同的module
     * 0x1001: 注册获取验证码
     * 0x1002: 找回密码获取验证码
     * 0x1003: 联盟动态列表
     * 0x1004：联盟活动列表
     * 0x1005: 点击的是个人会员
     * 0x1006: 点击的是家庭会员
     * 0x1007: 点击的身份证正面
     * 0x1008: 点击的身份证背面
     */
    public static int CLICK_POSITION_SIZE = -1;

    /**
     * 请求失败
     */
    public static final String HTTP_MSG_RESPONSE_FAILED = "The request data failed and the response code is not 200,code = ";

    /**
     * 请求根域名
     */
    public static final String ROOT_URI = "https://www.yumukeji.cn/project/bshd/";

    /**
     * 记录最近输入的一次手机号
     */
    public static String LAST_TEL = "";

    /**
     * 标记是否是会员
     */
    public static boolean hasMembers;

    /**
     * 登录者信息
     */
    public static LoginnerBean mLoginner;

    /**
     * 扫一扫
     */
    public static final int SCANCHECKCODE = 0x0001;

    /**
     * 医院搜索页面返回请求码、结果码
     */
    public static String Conditions;
    public static final int HOSPITAL_RESULT_CODE = 0x002;
    public static final int DYNAMIC_RESULT_CODE = 0x003;
    public static final int ACTIVITY_RESULT_CODE = 0x004;
}
