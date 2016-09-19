package com.fanwe.user.model.putAttention;

import java.util.List;

/**
 * Created by didik on 2016/9/18.
 */
public class RootAttention {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultAttention> result;

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

    public List<ResultAttention> getResult() {
        return result;
    }

    public void setResult(List<ResultAttention> result) {
        this.result = result;
    }
}
