package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/12 15:38
 * @Desc: 分享人信息
 */
public final class ShareManBean implements Serializable {

    private String type;
    private String adLoginName;
    private String companyName;

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
}
