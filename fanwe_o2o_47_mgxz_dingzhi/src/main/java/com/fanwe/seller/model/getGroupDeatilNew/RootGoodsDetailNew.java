package com.fanwe.seller.model.getGroupDeatilNew;

import java.util.List;

/**
 * Created by didik on 2016/10/17.
 */

public class RootGoodsDetailNew {


    private String message;
    private String statusCode;
    private String token;

    private List<ResultGoodsDetailNew> result;

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

    public List<ResultGoodsDetailNew> getResult() {
        return result;
    }

    public void setResult(List<ResultGoodsDetailNew> result) {
        this.result = result;
    }
}
