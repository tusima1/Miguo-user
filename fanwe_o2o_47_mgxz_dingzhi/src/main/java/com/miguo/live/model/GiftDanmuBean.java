package com.miguo.live.model;

import java.io.Serializable;

/**
 * Created by zlh on 2016/9/18.
 * 弹幕花钻石回调
 */
public class GiftDanmuBean implements Serializable{

    String message;
    int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
