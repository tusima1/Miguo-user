package com.fanwe.seller.model.checkShopCollect;


import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class RootCheckShopCollect {
    private List<ResultCheckShopCollect> result;

    private String message;

    private String statusCode;

    private String token;

    public List<ResultCheckShopCollect> getResult() {
        return result;
    }

    public void setResult(List<ResultCheckShopCollect> result) {
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
