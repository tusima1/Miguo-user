package com.miguo.live.model.getHandOutRedPacket;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHandOutRedPacket {
    private String title;
    private List<ModelHandOutRedPacket> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelHandOutRedPacket> getBody() {
        return body;
    }

    public void setBody(List<ModelHandOutRedPacket> body) {
        this.body = body;
    }
}
