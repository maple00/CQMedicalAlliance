package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/11 13:30
 * @Desc: 联盟动态
 */
public final class DynamicBean implements Serializable {

    private String title;           // 标题
    private String photoSrc;            // 缩略图地址
    private String videoSrc;            // 视频地址
    private List<ArticleBean> article;      // content

    @Override
    public String toString() {
        return "DynamicBean{" +
                "title='" + title + '\'' +
                ", photoSrc='" + photoSrc + '\'' +
                ", videoSrc='" + videoSrc + '\'' +
                ", article=" + article +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public List<ArticleBean> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleBean> article) {
        this.article = article;
    }
}
