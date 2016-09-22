package com.fanwe.seller.model.getRepresentMerchant;

/**
 * Created by Administrator on 2016/9/21.
 */
public class RootRepresentMerchant {

    private String message;

    private String token;

    private String statusCode;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return this.statusCode;
    }
}
