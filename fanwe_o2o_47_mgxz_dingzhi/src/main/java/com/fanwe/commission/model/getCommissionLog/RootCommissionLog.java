package com.fanwe.commission.model.getCommissionLog;

import java.util.List;

/**
 * Created by didik on 2016/8/28.
 */
public class RootCommissionLog {
    private String message;
    private String token;
    private String statusCode;

    private List<ResultCommissionLog> result;

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

    public List<ResultCommissionLog> getResult() {
        return result;
    }

    public void setResult(List<ResultCommissionLog> result) {
        this.result = result;
    }

}
