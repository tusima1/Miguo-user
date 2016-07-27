package com.fanwe.base;

/**
 * Created by Administrator on 2016/7/27.
 */
public class Root<T> {

    private Result<T> result;


    private String message;


    private String token;


    private String statusCode;

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
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