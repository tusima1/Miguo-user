package com.fanwe.home.model;

/**
 * 房间信息
 * Created by Administrator on 2016/7/27.
 */
public class Room {
    private int createTime;

    private String title;

    private String cover;

    private Lbs lbs;

    private Host host;

    private int admireCount;

    private String chatRoomId;

    private int avRoomId;

    private int timeSpan;

    private int watchCount;

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return this.cover;
    }

    public void setLbs(Lbs lbs) {
        this.lbs = lbs;
    }

    public Lbs getLbs() {
        return this.lbs;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Host getHost() {
        return this.host;
    }

    public void setAdmireCount(int admireCount) {
        this.admireCount = admireCount;
    }

    public int getAdmireCount() {
        return this.admireCount;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomId() {
        return this.chatRoomId;
    }

    public void setAvRoomId(int avRoomId) {
        this.avRoomId = avRoomId;
    }

    public int getAvRoomId() {
        return this.avRoomId;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public int getTimeSpan() {
        return this.timeSpan;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getWatchCount() {
        return this.watchCount;
    }
}
