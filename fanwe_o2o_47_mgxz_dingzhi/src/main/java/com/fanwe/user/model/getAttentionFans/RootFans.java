package com.fanwe.user.model.getAttentionFans;

import java.util.List;

/**
 * Created by didik on 2016/9/18.
 */
public class RootFans {

    private String message;
    private String token;
    private String statusCode;

    private List<ResultFans> result;

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

    public List<ResultFans> getResult() {
        return result;
    }

    public void setResult(List<ResultFans> result) {
        this.result = result;
    }


}
