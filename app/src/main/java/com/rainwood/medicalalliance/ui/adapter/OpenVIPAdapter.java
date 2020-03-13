package com.rainwood.medicalalliance.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.VIPPriceBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/3/10 11:50
 * @Desc: 开通会员类型
 */
public final class OpenVIPAdapter extends BaseAdapter {

    private Context mContext;
    private List<VIPPriceBean> mList;

    public OpenVIPAdapter(Context context, List<VIPPriceBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public VIPPriceBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_open_vip_type, parent, false);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_discount = convertView.findViewById(R.id.tv_discount);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.iv_choice = convertView.findViewById(R.id.iv_choice);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).isSelector()){
            holder.rl_item.setBackground(mContext.getResources().getDrawable(R.drawable.shap_orange55_5));
        }else {
            holder.rl_item.setBackground(mContext.getResources().getDrawable(R.drawable.shap_gray_5));
        }
        holder.tv_name.setText(getItem(position).getGrade());
        if (getItem(position).isSelector()){
            holder.iv_choice.setVisibility(View.VISIBLE);
        }else {
            holder.iv_choice.setVisibility(View.GONE);
        }
        // 折扣价
        holder.tv_discount.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.orange50) + " size='"
                + FontDisplayUtil.dip2px(mContext, 15f) + "'>￥</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.orange50) + " size='"
                + FontDisplayUtil.dip2px(mContext, 25f) + "'>"
                + getItem(position).getPrice() + "</font>"));
        // 原价
        holder.tv_price.setText("原价 ￥" + getItem(position).getOldPrice());
        holder.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        // 点击事件
        holder.rl_item.setOnClickListener(v -> {
            mOnClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
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
        private RelativeLayout rl_item;
        private TextView tv_name, tv_discount, tv_price;
        private ImageView iv_choice;
    }
}
