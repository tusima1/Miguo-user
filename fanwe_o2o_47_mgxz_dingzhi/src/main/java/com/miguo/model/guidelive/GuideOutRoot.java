package com.miguo.model.guidelive;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class GuideOutRoot {
    private String message;
    private String token;
    private String statusCode;
    private List<GuideOutResult> result;

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

    public List<GuideOutResult> getResult() {
        return result;
    }

    public void setResult(List<GuideOutResult> result) {
        this.result = result;
    }
}
