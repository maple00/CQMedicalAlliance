package com.rainwood.medicalalliance.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * @Author: a797s
 * @Date: 2019/12/21 14:14
 * @Desc: 发送验证码定时器
 */
public final class CountDownTimerUtils {

    /**
     * 全局定时器变量
     */
    public static long CountDownTimerSize = -1;
    /**
     * 定时器
     */
    public static CountDownTimer countDownTimer;

    /**
     * TextView 倒计时
     * @param totalTime
     * @param mText
     * @param tips
     * @param unClickColor
     * @param afterColor
     */
    @SuppressLint("SetTextI18n")
    public static void initCountDownTimer(long totalTime, final TextView mText, final String tips, int unClickColor, int afterColor) {
        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 记录倒计时时间
                CountDownTimerSize = millisUntilFinished;
                mText.setText(millisUntilFinished / 1000 + "s后" + tips);
                mText.setEnabled(false);
                mText.setTextColor(unClickColor);
            }

            @Override
            public void onFinish() {
                mText.setEnabled(true);
                mText.setText(tips);
                mText.setTextColor(afterColor);
            }
        };
    }

    /**
     * Button 倒计时
     * @param totalTime
     * @param mText
     * @param tips
     * @param unClickColor
     * @param afterColor
     */
    public static void initCountDownTimer(long totalTime, final Button mText, final String tips, int unClickColor, int afterColor) {
        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                // 记录倒计时时间
                CountDownTimerSize = millisUntilFinished;
                mText.setText(millisUntilFinished / 1000 + "s后" + tips);
                mText.setEnabled(false);
                mText.setTextColor(unClickColor);
            }

            @Override
            public void onFinish() {
                mText.setEnabled(true);
                mText.setText(tips);
                mText.setTextColor(afterColor);
            }
        };
    }
}
