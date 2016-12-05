package com.miguo.model.guidelive;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideOutModel extends GuideMark {
//            "extend":"",//扩展子u按
//            "img":"图片",//封面图
//            "create_time":"111111111",
//            "is_effect":"1",
//            "id":"主键",
//            "video":"视频地址",
//            "sort":"1",
//            "title":"标题",
//            "descript":"描述"

    private String extend;
    private String img;
    private String create_time;
    private String is_effect;
    private String id;
    private String video;
    private String sort;
    private String title;
    private String descript;

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_effect() {
        return is_effect;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
