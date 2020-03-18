package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.base.BaseDialog;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.dialog.MessageDialog;
import com.rainwood.medicalalliance.utils.DialogUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/13 17:40
 * @Desc: 修改资料
 */
public final class ModifyActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightTitle;
    @ViewById(R.id.ll_head_img)
    private LinearLayout headImg;
    @ViewById(R.id.iv_head)
    private ImageView head;
    @ViewById(R.id.cet_nick_name)
    private ClearEditText nickName;
    @ViewById(R.id.tv_modify_pwd)
    private TextView modifyPwd;
    @ViewById(R.id.btn_logout)
    private Button logout;

    private final int INITIAL_SIZE = 0x101;

    private DialogUtils mDialog;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("修改资料");
        rightTitle.setText("保存修改");
        rightTitle.setTextColor(getResources().getColor(R.color.green10));
        rightTitle.setOnClickListener(this);
        headImg.setOnClickListener(this);
        modifyPwd.setOnClickListener(this);
        logout.setOnClickListener(this);

        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        mDialog = new DialogUtils(this, "加载中");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:
                toast("保存修改");
                break;
            case R.id.ll_head_img:
                toast("修改头像");
                break;
            case R.id.tv_modify_pwd:
                // toast("修改密码");
                openActivity(ModifyLoginPwdActivity.class);
                break;
            case R.id.btn_logout:
                // toast("退出登录");
                mDialog = new DialogUtils(this, "退出中");
                new MessageDialog.Builder(this)
                        .setMessage("是否退出登录")
                        .setConfirm(getString(R.string.common_logout))
                        .setCancel(getString(R.string.common_cancel))
                        .setAutoDismiss(false)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                dialog.dismiss();
                                mDialog.showDialog();
                                RequestPost.logout(ModifyActivity.this);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    // 头像
                    Glide.with(ModifyActivity.this).load(R.drawable.icon_loading_fail)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())  // 设置圆角
                            .error(R.drawable.icon_loading_fail)        //异常时候显示的图片
                            .placeholder(R.drawable.icon_loading_fail) //加载成功前显示的图片
                            .fallback(R.drawable.icon_loading_fail)  //url为空的时候,显示的图片
                            .into(head);
                    nickName.setText("西瓜今天也很皮");

                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=logOut")) {           // 退出登录
                    toast("退出成功");
                    mDialog.dismissDialog();
                    openActivity(LoginMainActivity.class);
                }
            } else {
                mDialog.dismissDialog();
                toast(body.get("warn"));
            }
        }
    }
}
