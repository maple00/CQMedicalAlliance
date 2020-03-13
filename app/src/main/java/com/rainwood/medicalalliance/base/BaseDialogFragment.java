package com.rainwood.medicalalliance.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.rainwood.tools.toast.ToastUtils;

/**
 * @Author: shearson
 * @time: 2019/11/29 16:34
 * @des: 项目中dialog  基类
 */
public class BaseDialogFragment {

    public static class Builder<B extends BaseDialogFragment.Builder>
            extends MyDialogFragment.Builder<B> {

        public Builder(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public B setContentView(@NonNull View view) {
            // 使用 View 注解
            return super.setContentView(view);
        }

        /**
         * 显示吐司
         */
        public void toast(CharSequence text) {
            ToastUtils.show(text);
        }

        public void toast(@StringRes int id) {
            ToastUtils.show(id);
        }

        public void toast(Object object) {
            ToastUtils.show(object);
        }
    }
}
