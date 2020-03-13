package com.rainwood.tools.refresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Description: <带箭头的上拉下拉控件>
 * <li>在SuperSwipeRefreshLayout基础上进行了扩展
 * Author: mxdl
 * Date: 2019/2/25
 * Version: V1.0.0
 * Update:
 */
public class ArrowRefreshLayout extends BaseRefreshLayout {

  private ArrowHeaderView mArrowHeaderView;
  private ArrowFooterView mArrowFooterView;

  public ArrowRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    mArrowHeaderView = new ArrowHeaderView(context);
    mArrowFooterView = new ArrowFooterView(context);
    setHeaderView(mArrowHeaderView);
    setFooterView(mArrowFooterView);
    setOnPullRefreshListener(new OnPullRefreshListener() {
      @Override
      public void onRefresh() {
        mArrowHeaderView.onRefresh();
        if (mOnRefreshListener != null) {
          mOnRefreshListener.onRefresh();
        }
      }

      @Override
      public void onPullDistance(int distance) {

      }

      @Override
      public void onPullEnable(boolean enable) {
        mArrowHeaderView.onPullEnable(enable);
      }
    });
    setOnPushLoadMoreListener(new OnPushLoadMoreListener() {
      @Override
      public void onLoadMore() {
        mArrowFooterView.onLoadMore();
        if (mOnLoadMoreListener != null) {
          mOnLoadMoreListener.onLoadMore();
        }
      }

      @Override
      public void onPushDistance(int distance) {

      }

      @Override
      public void onPushEnable(boolean enable) {
        mArrowFooterView.onPushEnable(enable);
      }
    });
  }

  @Override
  public void showRefresh() {
    if (mArrowHeaderView != null) {
      mArrowHeaderView.onRefresh();
    }
  }
}
