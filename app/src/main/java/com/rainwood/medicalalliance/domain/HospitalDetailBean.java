package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/16 13:26
 * @Desc: 医院详情
 */
public final class HospitalDetailBean implements Serializable {

    private String id;              // 医院id
    private String name;            // 医院名称
    private String linkTel;         // 医院联系电话
    private String website;         // 医院的网址
    private String openingTime;        // 医院营业时间
    private String videoSrc;            // 医院视频介绍播放地址
    private String address;         // 医院地址
    private List<ArticleBean> article;        // 医院内容介绍
    private List<HospitalDetailImgBean> lunBoImg;     // 轮播图

    @Override
    public String toString() {
        return "HospitalDetailBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", linkTel='" + linkTel + '\'' +
                ", website='" + website + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", videoSrc='" + videoSrc + '\'' +
                ", address='" + address + '\'' +
                ", article=" + article +
                ", lunBoImg=" + lunBoImg +
                '}';
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ArticleBean> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleBean> article) {
        this.article = article;
    }

    public List<HospitalDetailImgBean> getLunBoImg() {
        return lunBoImg;
    }

    public void setLunBoImg(List<HospitalDetailImgBean> lunBoImg) {
        this.lunBoImg = lunBoImg;
    }
}
