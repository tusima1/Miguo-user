package com.fanwe.sellTemp.model.getRedPacket;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultGetRedPacket {
    private List<ModelGetRedPacket> body;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public List<ModelGetRedPacket> getBody() {
        return body;
    }

    public void setBody(List<ModelGetRedPacket> body) {
        this.body = body;
    }
}
