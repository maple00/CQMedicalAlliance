package com.rainwood.medicalalliance.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/11 13:31
 * @Desc: 内容
 */
public final class ArticleBean implements Serializable {

    private String type;
    private String word;            // 文章
    private String img;             // 图片地址
    private String list;            // 排序
    private String updateTime;      // 更新时间

    @Override
    public String toString() {
        return "ArticleBean{" +
                "type='" + type + '\'' +
                ", word='" + word + '\'' +
                ", img='" + img + '\'' +
                ", list='" + list + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
