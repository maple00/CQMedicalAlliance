package com.rainwood.medicalalliance.okhttp;

import com.rainwood.medicalalliance.common.Contants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Relin
 * on 2018-09-21.
 */

public class OnOkHttpListener implements Callback {

    private String url;
    private RequestParams params;
    private HttpHandler httpHandler;
    private OnHttpListener listener;

    public OnOkHttpListener(HttpHandler httpHandler, RequestParams params, String url, OnHttpListener listener) {
        this.params = params;
        this.url = url;
        this.listener = listener;
        this.httpHandler = httpHandler;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        httpHandler.sendExceptionMsg(params, url, -1, e, listener);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (!response.isSuccessful()) {
            httpHandler.sendExceptionMsg(params, url, response.code(), new IOException(Contants.HTTP_MSG_RESPONSE_FAILED + response.code()), listener);
            return;
        }
        try {
            httpHandler.sendSuccessfulMsg(params, url, response.code(), response.body().string(), listener);
        } catch (IOException e) {
            e.printStackTrace();
            httpHandler.sendExceptionMsg(params, url, response.code(), e, listener);
        }
    }
}
