package com.fanwe.user.model.getUserChangeMobile;

/**
 * Created by didik on 2016/8/22.
 */
public class ModelUserChangeMobile {

    /**
     * result : [{"body":[]}] 省略了
     * message : 操作成功
     * token : aeb0ead880958fa2589f53f8968effac
     * statusCode : 200
     */

    private String message;
    private String token;
    private String statusCode;

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
}
