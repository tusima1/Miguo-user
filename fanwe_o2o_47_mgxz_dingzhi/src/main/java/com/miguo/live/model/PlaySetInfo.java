package com.miguo.live.model;

import java.io.Serializable;

/**
 * 点播地址。
 * Created by Administrator on 2016/9/21.
 */
public class PlaySetInfo implements Serializable {

    private String url;

    /**
     * 格式， 0: ["", "原始"],
     * 1: ["带水印", "原始"],
     * 10: ["手机", "mp4"],
     * 20: ["标清", "mp4"],
     * 30: ["高清", "mp4"],
     * 110: ["手机", "flv"],
     * 120: ["标清", "flv"],
     * 130: ["高清", "flv"]
     */
    private int definition;


    private int vbitrate;

    /**
     *高度。
     */
    private int vheight;

    /**
     * 宽度 。
     */
    private int vwidth;


    public void setUrl(String url){

        this.url = url;

    }

    public String getUrl(){

        return this.url;

    }

    public void setDefinition(int definition){

        this.definition = definition;

    }

    public int getDefinition(){

        return this.definition;

    }

    public void setVbitrate(int vbitrate){

        this.vbitrate = vbitrate;

    }

    public int getVbitrate(){

        return this.vbitrate;

    }

    public void setVheight(int vheight){

        this.vheight = vheight;

    }

    public int getVheight(){

        return this.vheight;

    }

    public void setVwidth(int vwidth){

        this.vwidth = vwidth;

    }

    public int getVwidth(){

        return this.vwidth;

    }
}
