package com.miguo.live.model.postHandOutRedPacket;


import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class RootHandOutRedPacketPost {
    private List<ResultHandOutRedPacketPost> result;

    private String message;

    private String statusCode;

    private String token;

    public List<ResultHandOutRedPacketPost> getResult() {
        return result;
    }

    public void setResult(List<ResultHandOutRedPacketPost> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
