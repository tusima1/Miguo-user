package com.fanwe.commission.model.getWithdrawLog;

import java.util.List;

/**
 * Created by didik on 2016/8/29.
 */
public class RootWithdrawLog {
    private String message;
    private String token;
    private String statusCode;


    private List<ResultWithdrawLog> result;

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

    public List<ResultWithdrawLog> getResult() {
        return result;
    }

    public void setResult(List<ResultWithdrawLog> result) {
        this.result = result;
    }
}
