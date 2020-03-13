package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/11 13:34
 * @Desc: 首页数据
 */
public final class HomePageBean implements Serializable {

    private String title;           // 标题
    private String num;             // 控制显示的数据的条数
    private List<DynamicBean> dongtai;         // 联盟动态
    private List<DynamicBean> huodong;         // 联盟活动

    @Override
    public String toString() {
        return "HomePageBean{" +
                "title='" + title + '\'' +
                ", num='" + num + '\'' +
                ", dongtai=" + dongtai +
                ", huodong=" + huodong +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<DynamicBean> getDongtai() {
        return dongtai;
    }

    public void setDongtai(List<DynamicBean> dongtai) {
        this.dongtai = dongtai;
    }

    public List<DynamicBean> getHuodong() {
        return huodong;
    }

    public void setHuodong(List<DynamicBean> huodong) {
        this.huodong = huodong;
    }
}
