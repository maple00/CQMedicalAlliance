package com.rainwood.medicalalliance.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/10 13:59
 * @Desc: 支付实体类
 */
public class PayBean {

    private String amount;          // 支付金额
    private String content;         // 支付的内容
    private String method;          // 支付方式
    private boolean agreed;         // 同意条款是否勾选

    @Override
    public String toString() {
        return "PayBean{" +
                "amount='" + amount + '\'' +
                ", content='" + content + '\'' +
                ", method='" + method + '\'' +
                ", agreed=" + agreed +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }
}
