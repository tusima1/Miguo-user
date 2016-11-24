package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/9.
 */
public class CheckCitySignBean implements Serializable{

    String message;
    int statusCode;
    List<Result> result;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
            /**
             * 1有商家0没有商家
             */
            int is_sign;

            public int getIs_sign() {
                return is_sign;
            }

            public void setIs_sign(int is_sign) {
                this.is_sign = is_sign;
            }
        }

    }

}
