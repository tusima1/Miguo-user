package com.fanwe.user.model.getOrderInfo;

import java.util.List;

/**
 * Created by didik on 2016/8/23.
 */
public class RootOrderInfo {
    private String message;
    private String statusCode;
    private String token;

    private List<ResultOrderInfo> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ResultOrderInfo> getResult() {
        return result;
    }

    public void setResult(List<ResultOrderInfo> result) {
        this.result = result;
    }
}
