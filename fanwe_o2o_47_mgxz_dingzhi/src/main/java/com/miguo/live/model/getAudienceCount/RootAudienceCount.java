package com.miguo.live.model.getAudienceCount;


import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class RootAudienceCount {
    private List<ResultAudienceCount> result;

    private String message;

    private String statusCode;

    private String token;

    public List<ResultAudienceCount> getResult() {
        return result;
    }

    public void setResult(List<ResultAudienceCount> result) {
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
