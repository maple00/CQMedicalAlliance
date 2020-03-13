package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/10 18:04
 * @Desc: 用户登录
 */
public final class LoginnerBean implements Serializable {

    private String khid;                // 登录者id
    private String contactName;         // 昵称
    private String headSrc;             // 头像
    private String ifKehu;              // 是否是会员
    private String huiyuan;             // 会员的状态
    private SharePeople sharePeople;         // 分享人

    @Override
    public String toString() {
        return "LoginnerBean{" +
                "khid='" + khid + '\'' +
                ", contactName='" + contactName + '\'' +
                ", headSrc='" + headSrc + '\'' +
                ", ifKehu='" + ifKehu + '\'' +
                ", huiyuan='" + huiyuan + '\'' +
                ", sharePeople=" + sharePeople +
                '}';
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getIfKehu() {
        return ifKehu;
    }

    public void setIfKehu(String ifKehu) {
        this.ifKehu = ifKehu;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getHeadSrc() {
        return headSrc;
    }

    public void setHeadSrc(String headSrc) {
        this.headSrc = headSrc;
    }

    public String getHuiyuan() {
        return huiyuan;
    }

    public void setHuiyuan(String huiyuan) {
        this.huiyuan = huiyuan;
    }

    public SharePeople getSharePeople() {
        return sharePeople;
    }

    public void setSharePeople(SharePeople sharePeople) {
        this.sharePeople = sharePeople;
    }
}
