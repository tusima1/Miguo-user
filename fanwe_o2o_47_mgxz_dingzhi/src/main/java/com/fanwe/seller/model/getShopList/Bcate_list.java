package com.fanwe.seller.model.getShopList;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Bcate_list {
    private String icon_img;

    private String iconfont;

    private String name;

    private String id;

    private List<Bcate_type> bcate_type;

    private String iconcolor;

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public String getIcon_img() {
        return this.icon_img;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    public String getIconfont() {
        return this.iconfont;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setBcate_type(List<Bcate_type> bcate_type) {
        this.bcate_type = bcate_type;
    }

    public List<Bcate_type> getBcate_type() {
        return this.bcate_type;
    }

    public void setIconcolor(String iconcolor) {
        this.iconcolor = iconcolor;
    }

    public String getIconcolor() {
        return this.iconcolor;
    }
}
