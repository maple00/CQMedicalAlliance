package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/6 13:42
 * @Desc: 头部分类
 */
public final class TopTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public TopTypeAdapter(Context context, List<PressBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_top_type, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getName());
        Glide.with(convertView).load(Integer.parseInt(getItem(position).getImgPath())).into(holder.iv_img);
        // 点击事件
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder{
        private LinearLayout ll_item;
        private ImageView iv_img;
        private TextView tv_name;
    }
}
