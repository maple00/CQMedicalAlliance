package com.rainwood.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @Author: a797s
 * @Date: 2019/12/13 10:36
 * @Desc: 重写ListView的Measure 方法
 */
public final class MeasureListView extends ListView {

    public MeasureListView(Context context) {
        super(context);
    }

    public MeasureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    /**
     * 计算ListView 占据的高度问题
     */
    public static void getListViewSelfHeight(ListView listView) {
        // 获取ListView 对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        //
        if (listAdapter == null){
            return;
        }
        // 统计所有子项的总高度
        int totalHeight = 0;
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            // 返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 调用measure 方法 传0 是测量默认的大小
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // 通过父控件进行高度的申请
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listAdapter.getCount() - 1 从零开始 listView.getDividerHeight()获取子项间分隔符占用的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
