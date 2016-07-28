package com.fanwe.base;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class Root<T> {

    private List<Result> result ;

    private String message;

    private String statusCode;

    private String token;

    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setStatusCode(String statusCode){
        this.statusCode = statusCode;
    }
    public String getStatusCode(){
        return this.statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}