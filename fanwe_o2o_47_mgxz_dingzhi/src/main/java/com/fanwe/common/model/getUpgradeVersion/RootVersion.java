package com.fanwe.common.model.getUpgradeVersion;

import java.util.List;

/**
 * Created by didik on 2016/10/10.
 */

public class RootVersion {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultVersion> result;

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

    public List<ResultVersion> getResult() {
        return result;
    }

    public void setResult(List<ResultVersion> result) {
        this.result = result;
    }
}
