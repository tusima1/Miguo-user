package com.miguo.live.model.getWallet;

import java.util.List;

/**
 * Created by didik on 2016/9/19.
 */
public class RootMyWallet {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultMyWallet> result;

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

    public List<ResultMyWallet> getResult() {
        return result;
    }

    public void setResult(List<ResultMyWallet> result) {
        this.result = result;
    }
}
