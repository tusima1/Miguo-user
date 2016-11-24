package com.fanwe.common.model.getHomeClassifyList;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelHomeClassifyList {
    /**
     * 已选中图片
     */
    private String img;

    private String name;

    private String is_effect;

    private String id;

    private String sort;

    private String is_delete;
    /**
     * 未选中图片。
     */
    private String uncheck_img;

    private boolean is_checked=false;

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
    }

    public String getIs_effect() {
        return this.is_effect;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getIs_delete() {
        return this.is_delete;
    }

    public String getUncheck_img() {
        return uncheck_img;
    }

    public void setUncheck_img(String uncheck_img) {
        this.uncheck_img = uncheck_img;
    }

    public boolean is_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}
