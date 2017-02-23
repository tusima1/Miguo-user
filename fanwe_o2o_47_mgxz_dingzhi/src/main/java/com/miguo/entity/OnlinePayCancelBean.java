package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public class OnlinePayCancelBean implements Serializable{

    int statusCode;
    String message;
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

    public class Result implements Serializable{

        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{

        }

    }

}
