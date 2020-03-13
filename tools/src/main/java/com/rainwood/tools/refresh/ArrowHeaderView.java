package com.rainwood.tools.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.tools.R;

/**
 * Description: <ArrowFooterView>
 * Author: mxdl
 * Date: 2019/2/25
 * Version: V1.0.0
 * Update:
 */
public class ArrowHeaderView extends RelativeLayout implements HeadContract {
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;

    public ArrowHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public ArrowHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_head, this);
        progressBar = findViewById(R.id.pb_view);
        textView = findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down_arrow);
        progressBar.setVisibility(View.GONE);
    }

    public void onPullEnable(boolean enable) {
        progressBar.setVisibility(View.GONE);
        textView.setText(enable ? "松开刷新" : "下拉刷新");
        imageView.setVisibility(View.VISIBLE);
        imageView.setRotation(enable ? 180 : 0);
    }

    public void onRefresh() {
        textView.setText("正在刷新");
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

}
