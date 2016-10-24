package com.fanwe.seller.model.getSpecialTopic;

import java.util.List;

/**
 * Created by didik on 2016/10/18.
 */

public class DetailListBean {
    private String area_name;
    private String distance;
    private String type_id;
    private String icon;
    private String is_effect;
    private String sort;
    private String title;
    private String type;
    private String salary;
    private String tuan_price;
    private String cate_name;
    private String topic_id;
    private String origin_price;
    private String descript;
    private String m_topic_content_id;
    /**
     * title : 随时退
     */

    private List<TagListBean> tag_list;

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIs_effect() {
        return is_effect;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTuan_price() {
        return tuan_price;
    }

    public void setTuan_price(String tuan_price) {
        this.tuan_price = tuan_price;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getM_topic_content_id() {
        return m_topic_content_id;
    }

    public void setM_topic_content_id(String m_topic_content_id) {
        this.m_topic_content_id = m_topic_content_id;
    }

    public List<TagListBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagListBean> tag_list) {
        this.tag_list = tag_list;
    }

    public static class TagListBean {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
