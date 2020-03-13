package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/12 15:36
 * @Desc: 会员中心数据
 */
public final class VIPCenterBean implements Serializable {

    private String contactName;                 // 昵称
    private String headSrc;                     // 头像
    private ShareManBean sharePeople;           // 分享人
    private List<ArticleBean> article;                // vip会员特权

    public List<ArticleBean> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleBean> article) {
        this.article = article;
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

    public ShareManBean getSharePeople() {
        return sharePeople;
    }

    public void setSharePeople(ShareManBean sharePeople) {
        this.sharePeople = sharePeople;
    }

}
