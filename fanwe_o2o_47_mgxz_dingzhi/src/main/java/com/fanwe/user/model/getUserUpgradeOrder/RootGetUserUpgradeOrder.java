package com.fanwe.user.model.getUserUpgradeOrder;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class RootGetUserUpgradeOrder {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultGetUserUpgradeOrder> result;

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

    public List<ResultGetUserUpgradeOrder> getResult() {
        return result;
    }

    public void setResult(List<ResultGetUserUpgradeOrder> result) {
        this.result = result;
    }
}
