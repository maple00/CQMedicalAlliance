package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.VIPCardBean;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:17
 * @Desc: VIP卡详情
 */
public final class CardDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<VIPCardBean> mList;

    public CardDetailAdapter(Context context, List<VIPCardBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public VIPCardBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_vip_card_detail, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_label = convertView.findViewById(R.id.tv_label);
            holder.mgv_content_list = convertView.findViewById(R.id.mgv_content_list);
            holder.mlv_content_list = convertView.findViewById(R.id.mlv_content_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitles());
        holder.tv_label.setText(getItem(position).getLabel());
        //
        int type = getItemViewType(position);
        switch (type) {
            case 0:         // 卡片信息
                holder.mlv_content_list.setVisibility(View.GONE);
                holder.mgv_content_list.setVisibility(View.VISIBLE);
                SubItemCardInfosAdapter cardInfosAdapter = new SubItemCardInfosAdapter(mContext, getItem(position).getInfosList());
                holder.mgv_content_list.setAdapter(cardInfosAdapter);
                holder.mgv_content_list.setNumColumns(2);
                break;
            case 1:         // 基本资料
                holder.mlv_content_list.setVisibility(View.VISIBLE);
                holder.mgv_content_list.setVisibility(View.GONE);
                SubItemBaseInfoAdapter baseInfoAdapter = new SubItemBaseInfoAdapter(mContext, getItem(position).getInfo());
                holder.mlv_content_list.setAdapter(baseInfoAdapter);
                break;
            default:
                holder.mgv_content_list.setVisibility(View.GONE);
                holder.mlv_content_list.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_label;
        private MeasureGridView mgv_content_list;
        private MeasureListView mlv_content_list;
    }
}
