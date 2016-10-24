package com.example.administrator.coolweather.util;

/**
 * Created by Administrator on 2016/10/23.
 */
public interface HttpCallbackListener {
    void onFinished(String response);
    void onError(Exception e);
}
