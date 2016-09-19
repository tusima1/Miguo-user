package com.fanwe.home.model;

import com.fanwe.base.Body;

/**
 * 房间信息
 * Created by Administrator on 2016/7/27.
 */
public class Room extends Body {
    private String cover;

    private String create_time;

    private String chat_room_id;

    private Host host;

    private String av_room_id;
    private String inner_message;

    private String id;

    private String title;

    private Lbs lbs;

    public String getInner_message() {
        return inner_message;
    }

    public void setInner_message(String inner_message) {
        this.inner_message = inner_message;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getAv_room_id() {
        return av_room_id;
    }

    public void setAv_room_id(String av_room_id) {
        this.av_room_id = av_room_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Lbs getLbs() {
        return lbs;
    }

    public void setLbs(Lbs lbs) {
        this.lbs = lbs;
    }
}
