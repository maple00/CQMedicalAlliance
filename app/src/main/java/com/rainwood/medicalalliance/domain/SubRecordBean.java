package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/12 17:14
 * @Desc: 消费记录
 */
public final class SubRecordBean implements Serializable {

    private String id;              // 记录id
    private String khMxId;          // 客户id
    private String grade;           // Vip卡等级
    private String cardNum;         // vip卡号
    private String type;            // 卡类型
    private String price;           // 累计消费
    private String payTime;         // 续费时间
    private String overTime;        // 到期时间
    private List<UsuallyBean> mList = new ArrayList<>();

    @Override
    public String toString() {
        return "SubRecordBean{" +
                "id='" + id + '\'' +
                ", khMxId='" + khMxId + '\'' +
                ", grade='" + grade + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", payTime='" + payTime + '\'' +
                ", overTime='" + overTime + '\'' +
                ", mList=" + mList +
                '}';
    }

    public String getKhMxId() {
        return khMxId;
    }

    public void setKhMxId(String khMxId) {
        this.khMxId = khMxId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UsuallyBean> getList() {
        return mList;
    }

    public void setList(List<UsuallyBean> list) {
        mList = list;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
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
