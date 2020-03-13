package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:18
 * @Desc: 会员卡详情bean
 */
public final class VIPCardBean implements Serializable {

    private int type;                   // adapter 加载类型
    private String titles;              // topTitle
    private String label;               // 标签
    private List<UsuallyBean> infosList;      // 卡片信息
    private List<BaseInfoBean> mInfo;             // 基本资料

    @Override
    public String toString() {
        return "VIPCardBean{" +
                "type=" + type +
                ", titles='" + titles + '\'' +
                ", label='" + label + '\'' +
                ", infosList=" + infosList +
                ", mInfo=" + mInfo +
                '}';
    }

    public List<BaseInfoBean> getInfo() {
        return mInfo;
    }

    public void setInfo(List<BaseInfoBean> info) {
        mInfo = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<UsuallyBean> getInfosList() {
        return infosList;
    }

    public void setInfosList(List<UsuallyBean> infosList) {
        this.infosList = infosList;
    }

}
