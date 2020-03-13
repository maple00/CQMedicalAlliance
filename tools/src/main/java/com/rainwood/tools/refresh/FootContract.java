package com.rainwood.tools.refresh;

/**
 * Description: <上拉加载更多的协议>
 * Author:      mxdl
 * Date:        2019/2/25
 * Version:     V1.0.0
 * Update:
 */
public interface FootContract {
    /**
     * 手指上滑下滑的回调
     * @param enable
     */
    void onPushEnable(boolean enable);

    /**
     * 手指松开的回调
     */
    void onLoadMore();
}
