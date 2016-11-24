package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class HiFunnyTabBean implements Serializable{

    String message;
    String token;
    int statusCode;
    List<Result> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class Result implements Serializable{

        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            String tab_id;
            String title;

            public String getTab_id() {
                return tab_id;
            }

            public void setTab_id(String tab_id) {
                this.tab_id = tab_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

    }

}
