package com.fanwe.user.model.getAttentionFocus;

import java.util.List;

public class RootAttentionFocus {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultAttentionFocus> result;

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

    public List<ResultAttentionFocus> getResult() {
        return result;
    }

    public void setResult(List<ResultAttentionFocus> result) {
        this.result = result;
    }
}
