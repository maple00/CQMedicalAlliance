package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.ImageBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 10:07
 * @Desc: 图片adapter
 */
public final class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageBean> mList;

    public ImageAdapter(Context context, List<ImageBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ImageBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
            holder.iv_image = convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getPath())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                .into(holder.iv_image);
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_image;
    }
}
