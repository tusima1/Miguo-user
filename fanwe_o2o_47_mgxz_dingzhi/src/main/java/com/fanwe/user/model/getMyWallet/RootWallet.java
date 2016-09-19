package com.fanwe.user.model.getMyWallet;

import java.util.List;

/**
 * Created by didik on 2016/9/18.
 */
public class RootWallet {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultWallet> result;

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

    public List<ResultWallet> getResult() {
        return result;
    }

    public void setResult(List<ResultWallet> result) {
        this.result = result;
    }

}
