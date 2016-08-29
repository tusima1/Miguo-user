package com.fanwe.user.model.postUserUpgradeOrder;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class RootPostUserUpgradeOrder {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultPostUserUpgradeOrder> result;

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

    public List<ResultPostUserUpgradeOrder> getResult() {
        return result;
    }

    public void setResult(List<ResultPostUserUpgradeOrder> result) {
        this.result = result;
    }
}
