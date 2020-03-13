package com.rainwood.medicalalliance.okhttp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rainwood.medicalalliance.utils.DateTimeUtils;

/**
 * Created by Relin
 * on 2018-09-10.
 * Http异步处理类
 */
public class HttpHandler extends Handler {

    //网络请求失败的what
    public static final int WHAT_ON_FAILURE = 0xa01;
    //网络请求成功的what
    public static final int WHAT_ON_SUCCEED = 0xb02;
    //请求数据无返回的异常
    public static final String HTTP_NO_RESPONSE = "The server request is unresponsive code = ";

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        HttpResponse httpResult = (HttpResponse) msg.obj;
        OnHttpListener listener = httpResult.listener();
        //printDebugLog(httpResult);
        switch (msg.what) {
            case WHAT_ON_FAILURE:
                if (listener != null && httpResult != null && httpResult.body() != null) {
                    listener.onHttpFailure(httpResult);
                }
                break;
            case WHAT_ON_SUCCEED:
                if (listener != null && httpResult != null && httpResult.body() != null) {
                    listener.onHttpSucceed(httpResult);
                    Log.d("sxs-api-interface-time", DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ALL_TIME));
                }
                break;
        }
    }

    /**
     * 发送异常消息
     *
     * @param requestParams 请求参数
     * @param url           请求地址
     * @param code          请求结果代码
     * @param e             异常
     * @param listener      网络请求监听
     */
    public void sendExceptionMsg(RequestParams requestParams, String url, int code, Exception e, OnHttpListener listener) {
        Message msg = obtainMessage();
        msg.what = HttpHandler.WHAT_ON_FAILURE;
        HttpResponse response = new HttpResponse();
        response.requestParams(requestParams);
        response.url(url);
        response.exception(e);
        response.code(code);
        response.listener(listener);
        msg.obj = response;
        sendMessage(msg);
    }

    /**
     * 发送成功信息
     *
     * @param requestParams 请求参数
     * @param url           请求地址
     * @param code          请求结果代码
     * @param result        请求结果
     * @param listener      网络请求监听
     */
    public void sendSuccessfulMsg(RequestParams requestParams, String url, int code, String result, OnHttpListener listener) {
        HttpResponse response = new HttpResponse();
        response.body(result);
        response.url(url);
        response.requestParams(requestParams);
        response.code(code);
        response.listener(listener);
        Message msg = this.obtainMessage();
        msg.what = HttpHandler.WHAT_ON_SUCCEED;
        msg.obj = response;
        sendMessage(msg);
    }

}
