package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.HomePageBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/6 14:52
 * @Desc: 首页内容Adapter
 */
public class HomeContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomePageBean> mList;

    public HomeContentAdapter(Context context, List<HomePageBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public HomePageBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_content, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_more = convertView.findViewById(R.id.tv_more);
            holder.mlv_content_list = convertView.findViewById(R.id.mlv_content_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (position == 0){
            SubHomeContentAdapter subAdapter = new SubHomeContentAdapter(mContext, getItem(position).getDongtai());
            holder.mlv_content_list.setAdapter(subAdapter);
            // 查看详情
            subAdapter.setOnClickItem(position, mSubItem);
        }else {
            SubHomeContentAdapter subAdapter = new SubHomeContentAdapter(mContext, getItem(position).getHuodong());
            holder.mlv_content_list.setAdapter(subAdapter);
            // 查看详情
            subAdapter.setOnClickItem(position, mSubItem);
        }
        // 查看更多
        holder.tv_more.setOnClickListener(v -> mClickItem.onClickMore(position));
        return convertView;
    }

    public interface OnClickItem {
        // 查看更多
        void onClickMore(int position);
    }

    private OnClickItem mClickItem;

    public void setClickItem(OnClickItem clickItem) {
        mClickItem = clickItem;
    }

    private SubHomeContentAdapter.OnClickItem mSubItem;

    public void setSubItem(SubHomeContentAdapter.OnClickItem subItem) {
        mSubItem = subItem;
    }

    private class ViewHolder {
        private TextView tv_title, tv_more;
        private MeasureListView mlv_content_list;
    }
}
