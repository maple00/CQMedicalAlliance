package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:19
 * @Desc: 卡片信息
 */
public final class CardInfosBean implements Serializable {

    private String grade;               // 等级
    private String num;                 // 卡号
    private String type;                // 卡片类型
    private String sellPrice;               // 销售价
    private String oriPrice;                // 原价
    private String totalCoust;          // 累计消费
    private String renewalTime;         // 续费时间
    private String endTime;             // 到期时间

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getTotalCoust() {
        return totalCoust;
    }

    public void setTotalCoust(String totalCoust) {
        this.totalCoust = totalCoust;
    }

    public String getRenewalTime() {
        return renewalTime;
    }

    public void setRenewalTime(String renewalTime) {
        this.renewalTime = renewalTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
