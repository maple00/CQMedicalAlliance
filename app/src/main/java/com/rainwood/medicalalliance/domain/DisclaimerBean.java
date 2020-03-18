package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/16 10:22
 * @Desc: 免责条款
 */
public final class DisclaimerBean implements Serializable {

    private String id;
    private String word;
    private String img;

    @Override
    public String toString() {
        return "DisclaimerBean{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
