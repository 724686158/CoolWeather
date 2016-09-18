package com.example.admin.coolweather.util;

/**
 * Created by admin on 2016/9/18.
 */
public interface HttpCallbackListener {
    void OnFinish(String response);
    void OnError(Exception e);
}
