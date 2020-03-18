package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/9 10:37
 * @Desc: 医生团队
 */
public class DoctorBean implements Serializable {

    private String id;            // 医生id
    private String doctorName;      // 医生姓名
    private String sectionName;     // 科室名称
    private String headSrc;         // 头像
    private String text;            // 介绍

    @Override
    public String toString() {
        return "DoctorBean{" +
                "id='" + id + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", headSrc='" + headSrc + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getHeadSrc() {
        return headSrc;
    }

    public void setHeadSrc(String headSrc) {
        this.headSrc = headSrc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
