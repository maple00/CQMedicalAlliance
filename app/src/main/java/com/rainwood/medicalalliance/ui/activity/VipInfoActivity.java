package com.rainwood.medicalalliance.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.security.rp.RPSDK;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.base.BaseDialog;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.domain.ImageBean;
import com.rainwood.medicalalliance.io.IOUtils;
import com.rainwood.medicalalliance.json.JsonParser;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.request.RequestPost;
import com.rainwood.medicalalliance.ui.adapter.UploadImgAdapter;
import com.rainwood.medicalalliance.ui.dialog.MenuDialog;
import com.rainwood.medicalalliance.utils.CameraUtil;
import com.rainwood.medicalalliance.utils.CountDownTimerUtils;
import com.rainwood.medicalalliance.utils.ListUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.rainwood.medicalalliance.utils.CameraUtil.PHOTO_REQUEST_CAREMA;
import static com.rainwood.medicalalliance.utils.CameraUtil.RESULT_CAMERA_IMAGE;
import static com.rainwood.medicalalliance.utils.CameraUtil.uri_;

/**
 * @Author: shearson
 * @Time: 2020/3/9 17:28
 * @Desc: 会员基本信息页面
 */
public final class VipInfoActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_infos;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_checked_type)
    private TextView choiceType;
    @ViewById(R.id.cet_name)
    private ClearEditText name;         // 输入姓名
    @ViewById(R.id.iv_man)
    private ImageView manIv;
    @ViewById(R.id.tv_man)
    private TextView manTv;
    @ViewById(R.id.iv_woman)
    private ImageView womanIv;
    @ViewById(R.id.tv_woman)
    private TextView womanTv;
    @ViewById(R.id.cet_card_num)
    private ClearEditText cardNum;
    @ViewById(R.id.fl_card_front)       // 身份证正面
    private FrameLayout cardFront;
    @ViewById(R.id.iv_card_font)
    private ImageView cardFont;
    @ViewById(R.id.fl_card_reverse) //身份证背面
    private FrameLayout cardReverse;
    @ViewById(R.id.iv_behind)
    private ImageView cardBehind;
    @ViewById(R.id.cet_mobile_num)
    private ClearEditText mobileNum;
    @ViewById(R.id.btn_send_code)
    private Button sendCode;
    @ViewById(R.id.cet_verify_code)
    private ClearEditText verifyCode;
    @ViewById(R.id.pet_card_pwd)
    private PasswordEditText cardPwd;
    @ViewById(R.id.iv_checked)
    private ImageView service;
    @ViewById(R.id.tv_terms)            // 阅读条款
    private TextView terms;
    @ViewById(R.id.btn_next_step)
    private Button nextStep;        // 下一步

    // 家庭户口本
    @ViewById(R.id.ll_family)
    private LinearLayout familyLl;
    @ViewById(R.id.mgv_family_vip)
    private MeasureGridView familyImg;

    private List<ImageBean> mImageList = new ArrayList<>();

    // mHandler
    private final int FAMILY_SIZE = 0x0110;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("会员基本信息");
        cardFront.setOnClickListener(this);
        cardReverse.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        service.setOnClickListener(this);
        terms.setOnClickListener(this);
        manIv.setOnClickListener(this);
        manTv.setOnClickListener(this);
        womanIv.setOnClickListener(this);
        womanTv.setOnClickListener(this);
        nextStep.setOnClickListener(this);

        // 个人会员
        if (Contants.CLICK_POSITION_SIZE == 0x1005) {
            familyLl.setVisibility(View.GONE);
            choiceType.setText("已选个人会员");
        }
        // 家庭会员
        if (Contants.CLICK_POSITION_SIZE == 0x1006) {
            familyLl.setVisibility(View.VISIBLE);
            choiceType.setText("已选家庭会员");
            Message msg = new Message();
            msg.what = FAMILY_SIZE;
            mHandler.sendMessage(msg);
        }

        // 输入监听
        // 手机号监听 -- 手机号格式不正确则置灰
        InputTextHelper.with(this)
                .addView(mobileNum)
                .setMain(sendCode)
                .setListener(helper -> mobileNum.getText().toString().length() == 11).build();
    }

    /**
     * 记录填写的信息
     */
    private boolean isWoman;              // 记录是先生还是女士，默认为先生
    private boolean isRead;             // 是否阅读免责条款

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_man:
            case R.id.tv_man:
                // toast("先生");
                isWoman = !isWoman;
                womanIv.setImageResource(R.drawable.shape_uncheck_shape);
                manIv.setImageResource(R.drawable.shape_checked_shape);
                break;
            case R.id.iv_woman:
            case R.id.tv_woman:
                // toast("女士");
                isWoman = !isWoman;
                womanIv.setImageResource(R.drawable.shape_checked_shape);
                manIv.setImageResource(R.drawable.shape_uncheck_shape);
                break;
            case R.id.fl_card_front:
                // toast("上传身份证正面");
                Contants.CLICK_POSITION_SIZE = 0x1007;
                imageSelector();
                break;
            case R.id.fl_card_reverse:
//                toast("身份证背面");
                Contants.CLICK_POSITION_SIZE = 0x1008;
                imageSelector();
                break;
            case R.id.btn_send_code:                                 //toast("发送验/证码");
                if (TextUtils.isEmpty(mobileNum.getText())) {
                    toast("请输入手机号");
                    return;
                }

                RequestPost.getVerifyCode(mobileNum.getText().toString().trim(), this);
                CountDownTimerUtils.initCountDownTimer(60, sendCode, "可重新发送",
                        getResources().getColor(R.color.fontGray), getResources().getColor(R.color.green10));
                CountDownTimerUtils.countDownTimer.start();
                break;
            case R.id.iv_checked:
                isRead = !isRead;
                if (isRead) {
                    service.setImageResource(R.drawable.shape_checked_shape);
                } else {
                    service.setImageResource(R.drawable.shape_uncheck_shape);
                }
                break;
            case R.id.tv_terms:                                 // 点击了之后就表示阅读过了免责条款
                isRead = true;
                service.setImageResource(R.drawable.shape_checked_shape);
                openActivity(DisclaimerActivity.class);
                break;
            case R.id.btn_next_step:
                if (TextUtils.isEmpty(name.getText())) {
                    toast("请输入姓名");
                    return;
                }
//                if (TextUtils.isEmpty(cardNum.getText())) {
//                    toast("请输入身份证号");
//                    return;
//                }
//                if (TextUtils.isEmpty(mobileNum.getText())) {
//                    toast("请输入手机号");
//                    return;
//                }
                if (mFrontCard == null) {
                    toast("请上传身份证正面");
                    return;
                }
                if (mRearCard == null) {
                    toast("请上传身份证背面");
                    return;
                }
//                if (TextUtils.isEmpty(verifyCode.getText())) {
//                    toast("请输入验证码");
//                    return;
//                }
//                if (TextUtils.isEmpty(cardPwd.getText())) {
//                    toast("请设置会员卡密码");
//                    return;
//                }
                // 家庭类型才有户口本
                // TODO: 新增会员卡信息
                // 文件上传
                // 户口本主页
                // 户口本子页
                showLoading("新增中");
                if (ListUtils.getSize(residenceList) != 0) {                // 家庭会员类型
                    Log.d(TAG, "家庭：" + residenceList.toString());
                    List<File> subFile = new ArrayList<>(residenceList.subList(1, residenceList.size()));
                    RequestPost.addVIPInfos((Contants.CLICK_POSITION_SIZE == 0x1005 ? "个人" : "家庭"), name.getText().toString().trim(),
                            (isWoman ? "女" : "男"), cardNum.getText().toString().trim(), mFrontCard, mRearCard, cardPwd.getText().toString().trim(),
                            (isRead ? "是" : "否"), mobileNum.getText().toString().trim(), residenceList.get(0),
                            subFile, this);
                } else {                                                                 // 个人会员类型
                    Log.d(TAG, "个人：" + residenceList.toString());
                    RequestPost.getPersonalVIP((Contants.CLICK_POSITION_SIZE == 0x1006 ? "个人" : "家庭"), name.getText().toString().trim(),
                            (isWoman ? "女" : "男"), cardNum.getText().toString().trim(), mFrontCard, mRearCard, cardPwd.getText().toString().trim(),
                            (isRead ? "是" : "否"), mobileNum.getText().toString().trim(), this);
                }
                break;
        }
    }

    private List<File> residenceList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FAMILY_SIZE:
                    // 默认的第一项
                    if (ListUtils.getSize(mImageList) == 0) {
                        ImageBean image = new ImageBean();
                        image.setHasAdd(true);
                        image.setPath("");
                        mImageList.add(image);
                    }
                    Log.d(TAG, "image ---" + mImageList.toString());
                    UploadImgAdapter imgAdapter = new UploadImgAdapter(VipInfoActivity.this, mImageList);
                    familyImg.setAdapter(imgAdapter);
                    familyImg.setNumColumns(3);
                    imgAdapter.setOnClickImage(() -> {
                        //toast("添加图片");
                        imageSelector();
                    });
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, "result ---" + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getCaptcha")) {               // 获取手机验证码
                    toast(body.get("warn"));
                    Log.d(TAG, " --- 验证码 -- " + body.get("data"));
                }
                if (result.url().contains("library/mData.php?type=homeMessage")) {               // 家庭会员
                    dismissDialog();
                    getALiToken(body);
                }
                if (result.url().contains("library/mData.php?type=personalMessage")) {              // 个人会员
                    Log.d(TAG, " -- 个人会员 ---" + body.get("data"));
                    dismissDialog();
                    getALiToken(body);
                }

                if (result.url().contains("library/mData.php?type=aliSDK")) {                // 活体检测
                    Log.d(TAG, "data + " + body.get("data"));
                    Map<String, String> data = JsonParser.parseJSONObject(body.get("data"));
                    String verifyToken = data.get("VerifyToken");
                    String requestId = data.get("RequestId");
                    //
                    RPSDK.start(verifyToken, this, new RPSDK.RPCompletedListener() {
                        @Override
                        public void onAuditResult(RPSDK.AUDIT audit, String code) {
                            //Toast.makeText(ParametersActivity.this, audit + "", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, " audit：" + audit + " ---- code -- " + code);
                            if (audit == RPSDK.AUDIT.AUDIT_PASS) {
                                // 认证通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
                                // do something -- 去支付页面
                                openActivity(OpenVIPTypeActivity.class);
                            } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) {
                                // 认证不通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
                                // do something
                            } else if (audit == RPSDK.AUDIT.AUDIT_NOT) {
                                // 未认证，具体原因可通过code来区分（code取值参见下方表格），通常是用户主动退出或者姓名身份证号实名校验不匹配等原因，导致未完成认证流程
                                // do something
                            }
                        }
                    });
                }

            }
        } else {
            toast(body.get("warn"));
        }
    }

    /**
     * request residence token
     * @param body
     */
    private void getALiToken(Map<String, String> body) {
        toast("新增成功");
        Map<String, String> map = JsonParser.parseJSONObject(body.get("data"));
        Log.d(TAG, " map --- " + map);
        String customID = map.get("khMxId");
        String url = map.get("url");
        // 请求活体检测token
        RequestPost.getVerifyToken(url, this);
    }

    /**
     * 从相机、相册中选择图片
     */

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, RESULT_CAMERA_IMAGE);
    }

    private File mFrontCard;
    private File mRearCard;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case RESULT_CAMERA_IMAGE:                                       // 从相册返回的数据
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        if (Contants.CLICK_POSITION_SIZE == 0x1007) {           // 0x1007:正面
                            cardFont.setImageURI(uri);
                            mFrontCard = IOUtils.decodeUri(this, uri);
                        } else if (Contants.CLICK_POSITION_SIZE == 0x1008) {        // 0x1008: 反面
                            cardBehind.setImageURI(uri);
                            mRearCard = IOUtils.decodeUri(this, uri);
                        } else {     // 户口本照片
                            residenceImg(uri);
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:                                                  //照的相片
                    try {
                        //图片解析成Bitmap对象 -- uri转换成Bitmap对象的时候会出现图片旋转的问题
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri_));
                        if (Contants.CLICK_POSITION_SIZE == 0x1007) {
                            cardFont.setImageURI(uri_);
                            mFrontCard = IOUtils.decodeUri(this, uri_);
                        } else if (Contants.CLICK_POSITION_SIZE == 0x1008) {
                            cardBehind.setImageURI(uri_);
                            mRearCard = IOUtils.decodeUri(this, uri_);
                        } else {     // 户口本照片
                            residenceImg(uri_);
                        }
                        Log.d(TAG, " uri --  " + uri_);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 户口本照片
     * @param uri_ 照片uri
     */
    private void residenceImg(Uri uri_) {
        String path = CameraUtil.getPath(this, uri_);
        File file = IOUtils.decodeUri(this, uri_);
        residenceList.add(file);
        ImageBean image = new ImageBean();
        image.setPath(path);
        image.setHasAdd(false);
        mImageList.add(image);
        // 刷新UI
        Message msg = new Message();
        msg.what = FAMILY_SIZE;
        mHandler.sendMessage(msg);
    }

    private String[] selectors = {"相机", "相册"};

    private void imageSelector() {
        List<String> data = new ArrayList<>(Arrays.asList(selectors));
        // 先权限检查
        XXPermissions.with(getActivity())
                .constantRequest()
                .permission(Permission.Group.STORAGE)       // 读写权限
                .permission(Permission.CAMERA)              // 相机权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            new MenuDialog.Builder(getActivity())
                                    // 设置null 表示不显示取消按钮
                                    .setCancel(R.string.common_cancel)
                                    // 设置点击按钮后不关闭弹窗
                                    .setAutoDismiss(false)
                                    // 显示的数据
                                    .setList(data)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MenuDialog.OnListener<String>() {
                                        @Override
                                        public void onSelected(BaseDialog dialog, int position, String text) {
                                            dialog.dismiss();
                                            switch (position) {
                                                case 0:         // 拍照
                                                    // toast("相机");
                                                    CameraUtil.openCamera(VipInfoActivity.this);
                                                    break;
                                                case 1:         // 相册
                                                    // toast("相册");
                                                    gallery();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

}
