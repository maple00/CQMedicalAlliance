package com.rainwood.medicalalliance.utils;

/**
 * @Author: a797s
 * @Date: 2020/3/3 17:58
 * @Desc: 测试Utils
 */
public final class TestUtils {

    /**
     * 生成范围性随机数
     */
    public static int getRandonNum(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }



}
