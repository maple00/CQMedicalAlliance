package com.rainwood.medicalalliance.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.RenewalBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 17:06
 * @Desc: 续费记录
 */
public final class RenewalAdapter extends BaseAdapter {

    private Context mContext;
    private List<RenewalBean> mList;

    public RenewalAdapter(Context context, List<RenewalBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RenewalBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_renewal, parent, false);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_renewal_time = convertView.findViewById(R.id.tv_renewal_time);
            holder.tv_due_time = convertView.findViewById(R.id.tv_due_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(getItem(position).getType());
        holder.tv_price.setText("￥" + getItem(position).getPrice());
        holder.tv_renewal_time.setText(getItem(position).getPayTime() + "续");
        holder.tv_due_time.setText(getItem(position).getOverTime() + "到期");
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_type, tv_price, tv_renewal_time, tv_due_time;
    }
}
