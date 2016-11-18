package com.fanwe.groupon.model.getFeaturedGroupBuy;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelFeaturedGroupBuy {
    private String area_name;

    private String tuan_price;

    private String img;

    private String distance;

    private List<Tag> tag_list;

    private String name;

    private String cate_name;

    private String id;

    private String origin_price;

    private String salary;

    private String biz_id;
    private String tuan_price_with_unit;//带单位的

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getTuan_price() {
        return tuan_price;
    }

    public void setTuan_price(String tuan_price) {
        this.tuan_price = tuan_price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Tag> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<Tag> tag_list) {
        this.tag_list = tag_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBiz_id() {
        return biz_id;
    }

    public void setBiz_id(String biz_id) {
        this.biz_id = biz_id;
    }

    public String getTuan_price_with_unit() {
        return tuan_price_with_unit;
    }

    public void setTuan_price_with_unit(String tuan_price_with_unit) {
        this.tuan_price_with_unit = tuan_price_with_unit;
    }
}
