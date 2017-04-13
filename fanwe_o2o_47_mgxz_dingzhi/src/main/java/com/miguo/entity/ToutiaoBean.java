package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry on 2017/4/13.
 */
public class ToutiaoBean implements Serializable {

    String message;

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

    public class Result implements Serializable{

        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{

            String ent_id;
            String user_name;
            String title;

            public String getEnt_id() {
                return ent_id;
            }

            public void setEnt_id(String ent_id) {
                this.ent_id = ent_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }
        }

    }

}
