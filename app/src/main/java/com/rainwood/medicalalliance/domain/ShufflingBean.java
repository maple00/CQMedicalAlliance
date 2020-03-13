package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/11 15:16
 * @Desc: 轮播图
 */
public final class ShufflingBean implements Serializable {

    private String id;      // id
    private String src;     // src
    private String url;         // 点击跳转的链接

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
