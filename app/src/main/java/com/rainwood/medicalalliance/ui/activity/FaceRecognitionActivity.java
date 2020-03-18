package com.rainwood.medicalalliance.ui.activity;

import com.alibaba.security.rp.RPSDK;
import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.json.JsonParser;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/18 9:17
 * @Desc: 人脸识别activity
 */
public final class FaceRecognitionActivity extends BaseActivity implements OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_recognition;
    }

    @Override
    protected void initView() {

        RPSDK.start(verifyToken, this, new RPSDK.RPCompletedListener() {
            @Override
            public void onAuditResult(RPSDK.AUDIT audit, String code) {
                //Toast.makeText(ParametersActivity.this, audit + "", Toast.LENGTH_SHORT).show();

                if (audit == RPSDK.AUDIT.AUDIT_PASS) {
                    // 认证通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
                    // do something
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

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getCaptcha")) {

                }
            }else {
                toast(body.get("data"));
            }
        }
    }
}
