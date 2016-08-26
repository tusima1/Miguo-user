package com.fanwe.user.model.getRefundPage;

import java.util.List;

/**
 * Created by didik on 2016/8/26.
 */
public class RootRefundPage {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultRefundPage> result;

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

    public List<ResultRefundPage> getResult() {
        return result;
    }

    public void setResult(List<ResultRefundPage> result) {
        this.result = result;
    }
}
