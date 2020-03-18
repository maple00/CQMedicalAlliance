package com.rainwood.medicalalliance.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.DoctorBean;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.widget.ExpandTextView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/9 10:29
 * @Desc: 医生团队
 */
public class DoctorListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<DoctorBean> mList;

    public DoctorListAdapter(Activity activity, List<DoctorBean> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public DoctorBean getItem(int position) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_doctor_list, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_depart = convertView.findViewById(R.id.tv_depart);
            holder.etv_desc = convertView.findViewById(R.id.etv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 缩略图异步加载
        Glide.with(convertView).load(Contants.ROOT_URI + getItem(position).getHeadSrc())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))  // 设置圆角
                .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                .into(holder.iv_img);
        holder.tv_name.setText(getItem(position).getDoctorName());
        holder.tv_depart.setText(getItem(position).getSectionName());
        // 设置最大行数
        holder.etv_desc.initWidth(mActivity.getWindowManager().getDefaultDisplay().getWidth() - FontDisplayUtil.dip2px(mActivity, 16 * 2));
        holder.etv_desc.setMaxLines(3);
        holder.etv_desc.setCloseText(getItem(position).getText());
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_depart;
        private ExpandTextView etv_desc;
    }
}

