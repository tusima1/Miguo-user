package com.fanwe.seller.model;

/**
 * 门店信息详情，主要放在主播主场里面的内容 。
 * Created by Administrator on 2016/8/9.
 */
public class SellerDetailInfo {
    /**
     * 门店图片。
     */
    private String img;
    /**
     * 门店地址。
     */
    private String address;
    /**
     * 区域名称。
     */
    private String areaName;
    /**
     * 主要业务介绍。
     */
    private String main_buss;
    /**
     * 评论数。
     */
    private String avg_grade_num;
    /**
     * 电话 。
     */
    private String tel;
    /**
     * ID
     */
    private String id;
    /**
     * 消费说明。
     */

    private String mobile_brief;
    /**
     * 门店名
     */
    private String title;
    /**
     * 评分等级
     */
    private String avg_grade;
    /**
     * 标签 。
     */
    private String tags;
    /**
     * 平均消费价格。
     */
    private String ref_avg_price ;



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMain_buss() {
        return main_buss;
    }

    public void setMain_buss(String main_buss) {
        this.main_buss = main_buss;
    }

    public String getAvg_grade_num() {
        return avg_grade_num;
    }

    public void setAvg_grade_num(String avg_grade_num) {
        this.avg_grade_num = avg_grade_num;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_brief() {
        return mobile_brief;
    }

    public void setMobile_brief(String mobile_brief) {
        this.mobile_brief = mobile_brief;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvg_grade() {
        return avg_grade;
    }

    public void setAvg_grade(String avg_grade) {
        this.avg_grade = avg_grade;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRef_avg_price() {
        return ref_avg_price;
    }

    public void setRef_avg_price(String ref_avg_price) {
        this.ref_avg_price = ref_avg_price;
    }
}
