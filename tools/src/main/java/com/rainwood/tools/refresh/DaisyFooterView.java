package com.rainwood.tools.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.tools.R;

/**
 * Description: <DaisyHeaderView>
 * Author: mxdl
 * Date: 2019/2/25
 * Version: V1.0.0
 * Update:
 */
public class DaisyFooterView extends RelativeLayout implements FootContract {

    private TextView mTxtLoading;
    private ImageView mImgDaisy;
    private ObjectAnimator mRotation;

    public DaisyFooterView(Context context) {
        this(context, null);
    }

    public DaisyFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_daisy, this);
        mTxtLoading = findViewById(R.id.txt_loading);
        mTxtLoading.setText("上拉加载更多...");
        mImgDaisy = findViewById(R.id.img_daisy);

        mRotation = ObjectAnimator.ofFloat(mImgDaisy, "rotation", 0, 360).setDuration(800);
        mRotation.setRepeatCount(ValueAnimator.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());

    }

    @Override
    public void onPushEnable(boolean enable) {
        mTxtLoading.setText(enable ? "松开加载" : "上拉加载");
    }

    @Override
    public void onLoadMore() {
        mTxtLoading.setText("正在加载...");
        mRotation.start();
    }

    public void setLoadMore(boolean b) {
        if (b) {
            mRotation.start();
        } else {
            mRotation.pause();
        }
    }
}
