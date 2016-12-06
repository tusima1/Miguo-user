package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/3.
 */

public class UserCheckExistBean implements Serializable{

    String message;
    String token;
    int statusCode;
    List<Result> result;

    public boolean exist(){
        return null == result || null == result.get(0) || null == result.get(0).getBody() || null == result.get(0).getBody().get(0) ? false     : result.get(0).getBody().get(0).getExist() == 1;
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
            /**
             *  0 表示校验对象不存在
             *  1 表示校验对象存在
             */
            int exist;

            public int getExist() {
                return exist;
            }

            public void setExist(int exist) {
                this.exist = exist;
            }
        }

    }

}
