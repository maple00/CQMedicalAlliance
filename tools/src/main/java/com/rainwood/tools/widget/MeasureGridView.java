package com.rainwood.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Author: a797s
 * @Date: 2019/12/13 17:54
 * @Desc: 重写MeasureGridView
 */
public class MeasureGridView extends GridView {

    public MeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MeasureGridView(Context context) {
        super(context);
    }
    public MeasureGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
