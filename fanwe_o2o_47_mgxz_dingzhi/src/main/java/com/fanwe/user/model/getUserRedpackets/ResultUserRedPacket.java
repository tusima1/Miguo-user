package com.fanwe.user.model.getUserRedpackets;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class ResultUserRedPacket {
    private String page;
    private String title;
    private String redPackets;
    private String page_size;

    private List<ModelUserRedPacket> body;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRedPackets() {
        return redPackets;
    }

    public void setRedPackets(String redPackets) {
        this.redPackets = redPackets;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<ModelUserRedPacket> getBody() {
        return body;
    }

    public void setBody(List<ModelUserRedPacket> body) {
        this.body = body;
    }
}
