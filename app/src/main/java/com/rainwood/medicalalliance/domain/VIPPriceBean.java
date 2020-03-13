package com.rainwood.medicalalliance.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/10 11:51
 * @Desc: 会员价格类型
 */
public final class VIPPriceBean  {

    private boolean selector;
    private String id;              // 卡的id
    private String grade;           // 卡的等级
    private String price;         // 折扣价
    private String oldPrice;           // 原价

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }
}
