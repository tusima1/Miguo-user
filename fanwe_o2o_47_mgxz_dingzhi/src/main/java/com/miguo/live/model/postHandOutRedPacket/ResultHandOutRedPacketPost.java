package com.miguo.live.model.postHandOutRedPacket;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHandOutRedPacketPost {
    private String title;
    private List<ModelHandOutRedPacketPost> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelHandOutRedPacketPost> getBody() {
        return body;
    }

    public void setBody(List<ModelHandOutRedPacketPost> body) {
        this.body = body;
    }
}
