package com.miguo.live.model;

import java.util.List;

/**
 * Created by didik on 2016/7/28.
 */
public class RoomIDEntity {
    /**
     * room_id : dc7542f8-23f8-4b1d-9465-55f234953a93
     * result : []
     * message : 操作成功
     * token : 422c19fd43f2052685d9a280ff05c548
     * statusCode : 200
     */

    private String room_id;
    private String message;
    private String token;
    private String statusCode;
    private List<?> result;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

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

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }
}
