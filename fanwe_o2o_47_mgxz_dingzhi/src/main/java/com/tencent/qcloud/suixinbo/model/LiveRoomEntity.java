package com.tencent.qcloud.suixinbo.model;

import java.io.Serializable;

/**
 * 直播是实体类存储当前的直播房间信息。
 * Created by Administrator on 2016/9/27.
 */
public class LiveRoomEntity implements Serializable {
    /**
     * 房间ID
     */
    private  String roomId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 是否存活。
     */
    private boolean isAlive;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
