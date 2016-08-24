package com.fanwe.user.model.getGroupBuyCoupon;

import java.util.List;

/**
 * Created by didik on 2016/8/24.
 */
public class RootGroupCoupon {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultGroupCoupon> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<ResultGroupCoupon> getResult() {
        return result;
    }

    public void setResult(List<ResultGroupCoupon> result) {
        this.result = result;
    }

}
