package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.BuyRecordBean;
import com.rainwood.medicalalliance.domain.SubRecordBean;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/9 22:08
 * @Desc: 购买记录
 */
public final class BuyRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<SubRecordBean> mList;

    public BuyRecordAdapter(Context mContext, List<SubRecordBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SubRecordBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_buy_record,  parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_detail = convertView.findViewById(R.id.tv_detail);
            holder.tv_renewal = convertView.findViewById(R.id.tv_renewal);
            holder.tv_vip_num = convertView.findViewById(R.id.tv_vip_num);
            holder.mgv_item = convertView.findViewById(R.id.mgv_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getGrade());
        holder.tv_vip_num.setText(getItem(position).getCardNum());
        SubBuyRecordAdapter recordAdapter = new SubBuyRecordAdapter(mContext, getItem(position).getList());
        holder.mgv_item.setAdapter(recordAdapter);
        holder.mgv_item.setNumColumns(2);
        // 点击事件
        holder.tv_detail.setOnClickListener(v -> mOnClickButton.onClickDetail(position));
        holder.tv_renewal.setOnClickListener(v -> mOnClickButton.onClickRenewal(position));
        return convertView;
    }

    public interface  OnClickButton{
        // 查看详情
        void onClickDetail(int position);
        // 续费
        void onClickRenewal(int position);
    }

    private OnClickButton mOnClickButton;

    public void setOnClickButton(OnClickButton onClickButton) {
        mOnClickButton = onClickButton;
    }

    private class ViewHolder{
        private TextView tv_name, tv_detail, tv_renewal,tv_vip_num;
        private MeasureGridView mgv_item;

    }
}
