package com.rainwood.medicalalliance.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/10 10:45
 * @Desc: 照片
 */
public class ImageBean {

    private String path;            // 图片地址
    private boolean hasAdd;      // 是否是添加图片标识
    private String src;         // 后台地址

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", hasAdd=" + hasAdd +
                ", src='" + src + '\'' +
                '}';
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isHasAdd() {
        return hasAdd;
    }

    public void setHasAdd(boolean hasAdd) {
        this.hasAdd = hasAdd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
