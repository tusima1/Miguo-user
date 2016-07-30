package com.miguo.live.model.getAudienceList;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelAudienceList {
    private String start_time;

    private String live_record_id;

    private String user_id;

    private String end_time;

    private String nickname;

    private String id;

    private String join_status;

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public void setLive_record_id(String live_record_id) {
        this.live_record_id = live_record_id;
    }

    public String getLive_record_id() {
        return this.live_record_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setJoin_status(String join_status) {
        this.join_status = join_status;
    }

    public String getJoin_status() {
        return this.join_status;
    }
}
