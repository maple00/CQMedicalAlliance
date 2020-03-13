package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.UsuallyBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:45
 * @Desc: VIP卡详情中的卡片信息
 */
public final class SubItemCardInfosAdapter extends BaseAdapter {

    private Context mContext;
    private List<UsuallyBean> mList;

    public SubItemCardInfosAdapter(Context context, List<UsuallyBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public UsuallyBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_card_info, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        holder.tv_content.setText(getItem(position).getContent());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_content;
    }
}
