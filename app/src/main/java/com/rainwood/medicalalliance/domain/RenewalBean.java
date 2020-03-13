package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/13 17:06
 * @Desc: 续费
 */
public final class RenewalBean implements Serializable {

    private String type;            // 类型
    private String price;           // 续费金额
    private String payTime;         // 续费时间
    private String overTime;        // 到期时间

    @Override
    public String toString() {
        return "RenewalBean{" +
                "type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", payTime='" + payTime + '\'' +
                ", overTime='" + overTime + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }
}
