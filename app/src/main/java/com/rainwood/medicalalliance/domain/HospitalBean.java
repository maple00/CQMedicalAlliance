package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/9 8:55
 * @Desc: 医院
 */
public class HospitalBean implements Serializable {

    private String id;
    private String name;            // 医院名称
    private String logoSrc;         // 医院logo
    private String address;         // 医院地址
    private String distance;        // 距离

    @Override
    public String toString() {
        return "HospitalBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logoSrc='" + logoSrc + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoSrc() {
        return logoSrc;
    }

    public void setLogoSrc(String logoSrc) {
        this.logoSrc = logoSrc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
