package com.fanwe.user.model.getDistrInfo;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class RootDistrInfo {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultDistrInfo> result;

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

    public List<ResultDistrInfo> getResult() {
        return result;
    }

    public void setResult(List<ResultDistrInfo> result) {
        this.result = result;
    }
}
