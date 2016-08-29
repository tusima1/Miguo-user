package com.fanwe.commission.model.getUserAccount;

import java.util.List;

/**
 * Created by didik on 2016/8/29.
 */
public class RootUserAccount {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultUserAccount> result;

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

    public List<ResultUserAccount> getResult() {
        return result;
    }

    public void setResult(List<ResultUserAccount> result) {
        this.result = result;
    }
}
