package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.domain.BaseInfoBean;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/13 9:50
 * @Desc: VIP详情中的基本资料adapter
 */
public final class SubItemBaseInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<BaseInfoBean> mList;

    public SubItemBaseInfoAdapter(Context context, List<BaseInfoBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public BaseInfoBean getItem(int position) {
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ImageHolderOne imageHolderOne = null;
        ImageHolderTwo imageHolderTwo = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_card_info, parent, false);
                    holder.tv_title = convertView.findViewById(R.id.tv_title);
                    holder.tv_content = convertView.findViewById(R.id.tv_content);
                    convertView.setTag(holder);
                    break;
                case 1:
                    imageHolderOne = new ImageHolderOne();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_image, parent, false);
                    imageHolderOne.mgv_image_list = convertView.findViewById(R.id.mgv_image_list);
                    imageHolderOne.tv_title = convertView.findViewById(R.id.tv_title);
                    convertView.setTag(imageHolderOne);
                    break;
                case 2:
                    imageHolderTwo = new ImageHolderTwo();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_image_book, parent, false);
                    imageHolderTwo.mgv_image_list = convertView.findViewById(R.id.mgv_image_list);
                    imageHolderTwo.tv_title = convertView.findViewById(R.id.tv_title);
                    convertView.setTag(imageHolderTwo);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    holder = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    imageHolderOne = (ImageHolderOne) convertView.getTag();
                    break;
                case 2:
                    imageHolderTwo = (ImageHolderTwo) convertView.getTag();
                    break;
            }
        }
        // 加载数据 -- 数据只能放在外边展示，否则刷新的时候布局会混乱
        switch (type) {
            case 0:
                holder.tv_title.setText(getItem(position).getTitle());
                holder.tv_content.setText(getItem(position).getLabel());
                break;
            case 1:
                imageHolderOne.tv_title.setText(getItem(position).getTitle());
                // 图片
                ImageAdapter imageAdapter = new ImageAdapter(mContext, getItem(position).getIdCardList());
                imageHolderOne.mgv_image_list.setAdapter(imageAdapter);
                imageHolderOne.mgv_image_list.setNumColumns(2);
                break;
            case 2:
                imageHolderTwo.tv_title.setText(getItem(position).getTitle());
                // 图片
                ImageAdapter imageAdapter1 = new ImageAdapter(mContext, getItem(position).getBookList());
                imageHolderTwo.mgv_image_list.setAdapter(imageAdapter1);
                imageHolderTwo.mgv_image_list.setNumColumns(3);
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_content;
    }

    private class ImageHolderOne {
        private TextView tv_title;
        private MeasureGridView mgv_image_list;
    }

    private class ImageHolderTwo {
        private TextView tv_title;
        private MeasureGridView mgv_image_list;
    }
}
