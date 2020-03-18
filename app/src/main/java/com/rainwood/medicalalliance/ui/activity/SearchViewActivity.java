package com.rainwood.medicalalliance.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.medicalalliance.R;
import com.rainwood.medicalalliance.base.BaseActivity;
import com.rainwood.medicalalliance.common.Contants;
import com.rainwood.medicalalliance.helper.DbDao;
import com.rainwood.medicalalliance.okhttp.HttpResponse;
import com.rainwood.medicalalliance.okhttp.JsonParser;
import com.rainwood.medicalalliance.okhttp.OnHttpListener;
import com.rainwood.medicalalliance.ui.adapter.SeachRecordAdapter;
import com.rainwood.medicalalliance.ui.fragment.HospitalDescFragment;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2019/12/5 11:44
 * @Desc: 含历史记录的搜索框
 */
public final class SearchViewActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_searchview;
    }

    @ViewById(R.id.tv_search)
    private TextView mtvSearch;
    @ViewById(R.id.et_search)
    private ClearEditText met_search;

    @ViewById(R.id.iv_back)
    private ImageView pageBack;

    @ViewById(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewById(R.id.iv_deleteAll)
    private ImageView ivDeleteAll;
    @ViewById(R.id.tv_deleteAll)
    private TextView mtv_deleteAll;
    private SeachRecordAdapter mAdapter;

    // 数据库
    private DbDao mDbDao;

    @Override
    protected void initView() {
        mDbDao = new DbDao(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SeachRecordAdapter(mDbDao.queryData(""), this);
        if (Contants.CLICK_POSITION_SIZE == 0x1003) {    // 联盟动态
            met_search.setHint("搜索你想看的动态");
        } else if (Contants.CLICK_POSITION_SIZE == 0x1004) {     // 联盟活动
            met_search.setHint("搜索你想看的活动");
        }else {                                                                 // 搜索医院
            met_search.setHint("搜索医院名称");
        }
        // 查询历史记录
        mAdapter.setRvItemOnclickListener(position -> {
            mDbDao.delete(mDbDao.queryData("").get(position));
            mAdapter.updata(mDbDao.queryData(""));
        });
        mRecyclerView.setAdapter(mAdapter);
        // 历史记录点击
        mAdapter.setOnClickRecord(text -> {
            // toast(text);
            met_search.setText(text);
        });
        // 事件监听
        mtvSearch.setOnClickListener(this);
        pageBack.setOnClickListener(this);
        ivDeleteAll.setOnClickListener(this);
        mtv_deleteAll.setOnClickListener(this);
        // EditText自动聚焦
        showSoftInputFromWindow(met_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_deleteAll:
            case R.id.tv_deleteAll:
                mDbDao.deleteData();
                mAdapter.updata(mDbDao.queryData(""));
                break;
            case R.id.tv_search:
                //事件监听
                if (met_search.getText().toString().trim().length() != 0) {
                    // 查询数据库历史记录
                    boolean hasData = mDbDao.hasData(met_search.getText().toString().trim());
                    if (!hasData) {
                        mDbDao.insertData(met_search.getText().toString().trim());
                    } else {
                        // toast("该内容已在历史记录中");
                    }
                    mAdapter.updata(mDbDao.queryData(""));
                    // TODO: 搜索内容
                    Contants.Conditions = met_search.getText().toString().trim();
                    if (Contants.CLICK_POSITION_SIZE == 0x1003) {    // 联盟动态
                        setResult(Contants.DYNAMIC_RESULT_CODE);
                    } else if (Contants.CLICK_POSITION_SIZE == 0x1004) {     // 联盟活动
                        setResult(Contants.ACTIVITY_RESULT_CODE);
                    }else {                                                                 // 搜索医院
                        setResult(Contants.HOSPITAL_RESULT_CODE);
                    }
                    finish();
                } else {
                    toast("请输入内容");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Contants.Conditions = null;
        return super.onKeyDown(keyCode, event);
    }

    /**
     * EditText 自动聚焦且弹出软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("library/mData.php?type=getHospitalList")) {      // 查询医院列表

                }
            } else {
                toast(body.get("warn"));
            }
        }
    }
}
