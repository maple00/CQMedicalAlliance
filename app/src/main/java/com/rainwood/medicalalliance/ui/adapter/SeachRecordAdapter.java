package com.rainwood.medicalalliance.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseRecycleAdapter;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/5 13:24
 * @Desc: 含历史记录搜索框adapter
 */
public final class SeachRecordAdapter extends BaseRecycleAdapter<String> {

    public SeachRecordAdapter(List<String> datas, Context mContext) {
        super(datas, mContext);
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {

        TextView textView = (TextView) holder.getView(R.id.tv_record);
        textView.setText(datas.get(position));
        // 历史记录列表点击
        textView.setOnClickListener(v -> onClickRecord.onClickRecord(textView.getText().toString()));

        // 删除记录
        holder.getView(R.id.iv_delete).setOnClickListener(view -> {
            if (null != mRvItemOnclickListener) {
                mRvItemOnclickListener.RvItemOnclick(position);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_search;
    }

    public interface OnClickRecord {
        void onClickRecord(String text);
    }

    private OnClickRecord onClickRecord;

    public void setOnClickRecord(OnClickRecord onClickRecord) {
        this.onClickRecord = onClickRecord;
    }
}