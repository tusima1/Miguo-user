package com.fanwe.user.model.getPersonHomePage;

import java.util.List;

public class RootPersonHomePage {
    private List<ResultPersonHomePage> result;

    private String message;

    private String token;

    private String statusCode;

    public void setResult(List<ResultPersonHomePage> result) {
        this.result = result;
    }

    public List<ResultPersonHomePage> getResult() {
        return this.result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return this.statusCode;
    }
}
