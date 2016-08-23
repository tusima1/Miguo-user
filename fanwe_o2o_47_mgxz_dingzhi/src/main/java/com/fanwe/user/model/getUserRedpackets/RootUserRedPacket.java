package com.fanwe.user.model.getUserRedpackets;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class RootUserRedPacket {
    private String message;
    private String token;
    private String statusCode;
    private List<ResultUserRedPacket> result;

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

    public List<ResultUserRedPacket> getResult() {
        return result;
    }

    public void setResult(List<ResultUserRedPacket> result) {
        this.result = result;
    }
}
