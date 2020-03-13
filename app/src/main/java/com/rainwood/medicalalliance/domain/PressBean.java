package com.rainwood.medicalalliance.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/6 13:34
 * @Desc: 点击实体类 ---包括{图片 + 名字格式、}
 *
 */
public final class PressBean {

    private boolean clicked;        // 点击记录
    private String imgPath;         // 图片地址
    private String name;            // 名字
    private String desc;            // 描述


    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
