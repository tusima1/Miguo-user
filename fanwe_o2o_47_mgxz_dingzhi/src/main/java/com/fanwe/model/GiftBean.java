package com.fanwe.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GiftBean implements Serializable{

    int statusCode;
    String token;
    String message;
    List<Result> result;

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

    public class Result implements Serializable{
        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            int gift_num;

            public int getGift_num() {
                return gift_num;
            }

            public void setGift_num(int gift_num) {
                this.gift_num = gift_num;
            }
        }


    }

}
