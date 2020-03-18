package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.HospitalBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/9 8:55
 * @Desc: 医院介绍 --- 医院列表
 */
public class HospitalListAdapter extends BaseAdapter {

    private Context mContext;
    private List<HospitalBean> mList;

    public HospitalListAdapter(Context context, List<HospitalBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public HospitalBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hospital_list, parent, false);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_distance = convertView.findViewById(R.id.tv_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 缩略图异步加载
        Glide.with(convertView).load(Contants.ROOT_URI + getItem(position).getLogoSrc())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())  // 设置圆角
                .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                .into(holder.iv_img);
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_address.setText(getItem(position).getAddress());
        holder.tv_distance.setText(getItem(position).getDistance());
        // 点击事件 -- 查看详情
        holder.rl_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        private RelativeLayout rl_item;
        private ImageView iv_img;
        private TextView tv_name, tv_address, tv_distance;
    }
}
