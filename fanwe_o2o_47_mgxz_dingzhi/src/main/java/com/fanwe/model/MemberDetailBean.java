package com.fanwe.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh on 2016/9/29.
 */
public class MemberDetailBean implements Serializable{

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
            String html;

            public String getHtml() {
                return html;
            }

            public void setHtml(String html) {
                this.html = html;
            }
        }

    }

}
