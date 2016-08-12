package com.fanwe.seller.model.getGroupBuyCollect;


import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class RootGroupBuyCollect {
    private List<ResultGroupBuyCollect> result;

    private String message;

    private String statusCode;

    private String token;

    public List<ResultGroupBuyCollect> getResult() {
        return result;
    }

    public void setResult(List<ResultGroupBuyCollect> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
