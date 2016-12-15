package com.fanwe.seller.model.getGroupDeatilNew;

import com.fanwe.seller.model.getSpecialTopic.TagBean;

import java.util.List;

/**
 * Created by didik on 2016/10/17.
 */

public class ModelGoodsDetailNew {
    private String is_my_collect;
    private String notes;
    /**
     * clickurl : http://m.dev.mgxz.com/index/detail/id/004c0d20-43f8-4ef8-ae7f-a15d8359655f
     * summary : 仅售15元！价值20元的代金券1张，提供免费WiFi。
     * imageurl : ./public/attachment/201512/02/16/565eb02d40788.jpg
     * title : 【嘉兴】隆鑫麻辣烫 仅售15元！价值20元的代金券1张，提供免费WiFi。
     */

    private ShareInfoBean share_info;
    private String tuan_price;
    private String popularity;
    private String name;
    private String recommend_desc;
    private String tuan_price_with_unit;
    private String tuan_detail;
    private String short_name;
    private String origin_price;
    //TODO 10/19 add
    private String time_status;
    private String max_num;
    private String is_first_price;
    private String is_first;
    private String icon;
    /**
     * image : ./public/attachment/201512/02/16/565eb031bf572.jpg
     */

    private List<ImagesBean> images;
    /**
     * geo_y : 119
     * geo_x : 113
     * address : 星光大道885号
     * tel : 18667932385
     * id : 4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b1
     * shop_name : 京东商城2号
     */

    private List<ShopListBean> shop_list;
    private List<TagBean> tag_list;

    private String salary;

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTime_status() {
        return time_status;
    }

    public void setTime_status(String time_status) {
        this.time_status = time_status;
    }

    public String getMax_num() {
        return max_num;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public String getIs_first_price() {
        return is_first_price;
    }

    public void setIs_first_price(String is_first_price) {
        this.is_first_price = is_first_price;
    }

    public String getIs_first() {
        return is_first;
    }

    public void setIs_first(String is_first) {
        this.is_first = is_first;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIs_my_collect() {
        return is_my_collect;
    }

    public void setIs_my_collect(String is_my_collect) {
        this.is_my_collect = is_my_collect;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ShareInfoBean getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfoBean share_info) {
        this.share_info = share_info;
    }

    public String getTuan_price() {
        return tuan_price;
    }

    public void setTuan_price(String tuan_price) {
        this.tuan_price = tuan_price;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecommend_desc() {
        return recommend_desc;
    }

    public void setRecommend_desc(String recommend_desc) {
        this.recommend_desc = recommend_desc;
    }

    public String getTuan_price_with_unit() {
        return tuan_price_with_unit;
    }

    public void setTuan_price_with_unit(String tuan_price_with_unit) {
        this.tuan_price_with_unit = tuan_price_with_unit;
    }

    public String getTuan_detail() {
        return tuan_detail;
    }

    public void setTuan_detail(String tuan_detail) {
        this.tuan_detail = tuan_detail;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<ShopListBean> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<ShopListBean> shop_list) {
        this.shop_list = shop_list;
    }

    public List<TagBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagBean> tag_list) {
        this.tag_list = tag_list;
    }
}
