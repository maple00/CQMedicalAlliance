package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/11 9:04
 * @Desc: 分享人信息
 */
public class SharePeople implements Serializable {

    private String type;
    private String adLoginName;
    private String companyName;
    private String linkName;

    @Override
    public String toString() {
        return "SharePeople{" +
                "type='" + type + '\'' +
                ", adLoginName='" + adLoginName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", linkName='" + linkName + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdLoginName() {
        return adLoginName;
    }

    public void setAdLoginName(String adLoginName) {
        this.adLoginName = adLoginName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
