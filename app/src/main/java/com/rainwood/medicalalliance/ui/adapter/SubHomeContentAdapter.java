package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.DynamicBean;
import com.rainwood.medicalalliance.utils.ListUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/6 15:03
 * @Desc: 首页子类content
 */
public class SubHomeContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<DynamicBean> mList;
    private int parentPos;

    public SubHomeContentAdapter(Context context, List<DynamicBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public DynamicBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_home_content, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_text = convertView.findViewById(R.id.tv_text);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        // content本身有顺序
        for (int i = 0; i < ListUtils.getSize(getItem(position).getArticle()); i++) {
            while (!TextUtils.isEmpty(getItem(position).getArticle().get(i).getWord())) {        // 找到一段文字进行显示
                holder.tv_text.setText(getItem(position).getArticle().get(i).getWord());
                break;
            }
        }
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(10));
        // 缩略图异步加载
        Glide.with(convertView).load(Contants.ROOT_URI + getItem(position).getPhotoSrc())
                .apply(options)                         // 设置圆角
                .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                .into(holder.iv_img);
        // 点击事件     -- 查看单个item的详情
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(parentPos, position));
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int parentPos, int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(int parentPos, OnClickItem onClickItem) {
        this.parentPos = parentPos;
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_title, tv_text;
        private ImageView iv_img;
    }
}
