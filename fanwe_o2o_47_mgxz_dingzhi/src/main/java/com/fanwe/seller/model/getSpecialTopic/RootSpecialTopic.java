package com.fanwe.seller.model.getSpecialTopic;

import java.util.List;

/**
 * Created by didik on 2016/10/18.
 */

public class RootSpecialTopic {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultSpecialTopic> result;

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

    public List<ResultSpecialTopic> getResult() {
        return result;
    }

    public void setResult(List<ResultSpecialTopic> result) {
        this.result = result;
    }
}
