package com.miguo.entity;

/**
 * Created by didik 
 * Created time 2017/2/17
 * Description: 
 */

public class StatusBean {

    /**
     * result : [{"body":[]}]
     * message : 操作成功
     * token : be460f2c05a2628ef797040eac77223c
     * statusCode : 200
     * timestamp : 1487211786099
     */

    private String message;
    private String token;
    private String statusCode;
    private long timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
