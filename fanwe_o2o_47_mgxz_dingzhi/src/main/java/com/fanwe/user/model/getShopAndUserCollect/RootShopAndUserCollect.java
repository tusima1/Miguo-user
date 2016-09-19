package com.fanwe.user.model.getShopAndUserCollect;

import java.util.List;

public class RootShopAndUserCollect {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultShopAndUserCollect> result;

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

    public List<ResultShopAndUserCollect> getResult() {
        return result;
    }

    public void setResult(List<ResultShopAndUserCollect> result) {
        this.result = result;
    }
}
