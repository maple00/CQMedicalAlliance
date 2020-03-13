package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/10 17:52
 * @Desc: 广告bean
 */
public final class AdverteBean implements Serializable {

    private String id;          // 广告Id
    private String src;         // 广告地址
    private String url;

    @Override
    public String toString() {
        return "AdverteBean{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

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
