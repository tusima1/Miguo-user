package com.fanwe.user.model.getNameCardQR;

import java.util.List;

/**
 * Created by didik on 2016/8/29.
 */
public class RootNameCardQR {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultNameCardQR> result;

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

    public List<ResultNameCardQR> getResult() {
        return result;
    }

    public void setResult(List<ResultNameCardQR> result) {
        this.result = result;
    }
}
