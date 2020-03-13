package com.rainwood.medicalalliance.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:30
 * @Desc: 基本资料
 */
public final class BaseInfoBean implements Serializable {

    private String title;       // title
    private String label;       // String 类型的内容
    private int type;               // adapter加载类型
    // 身份证照片、户口本照片
    private List<ImageBean> idCardList;         // 身份证
    private List<ImageBean> bookList;           // 户口本

    @Override
    public String toString() {
        return "BaseInfoBean{" +
                "title='" + title + '\'' +
                ", label='" + label + '\'' +
                ", type=" + type +
                ", idCardList=" + idCardList +
                ", bookList=" + bookList +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ImageBean> getIdCardList() {
        return idCardList;
    }

    public void setIdCardList(List<ImageBean> idCardList) {
        this.idCardList = idCardList;
    }

    public List<ImageBean> getBookList() {
        return bookList;
    }

    public void setBookList(List<ImageBean> bookList) {
        this.bookList = bookList;
    }
}
