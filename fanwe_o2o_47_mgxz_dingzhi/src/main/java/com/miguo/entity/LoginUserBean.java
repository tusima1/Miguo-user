package com.miguo.entity;

import com.fanwe.user.model.UserInfoNew;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 */
public class LoginUserBean implements Serializable{

    List<Result> result;
    int statusCode;
    String message;
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result implements Serializable{
        List<UserInfoNew> body;

        public List<UserInfoNew> getBody() {
            return body;
        }

        public void setBody(List<UserInfoNew> body) {
            this.body = body;
        }
    }

}
