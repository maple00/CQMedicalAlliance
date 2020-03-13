package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/9 22:08
 * @Desc: java类作用描述
 */
public class BuyRecordBean implements Serializable {

    private String used;          // 累计消费
    private List<SubRecordBean> record;     // 记录列表

    @Override
    public String toString() {
        return "BuyRecordBean{" +
                "used='" + used + '\'' +
                ", record=" + record +
                '}';
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public List<SubRecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<SubRecordBean> record) {
        this.record = record;
    }
}
