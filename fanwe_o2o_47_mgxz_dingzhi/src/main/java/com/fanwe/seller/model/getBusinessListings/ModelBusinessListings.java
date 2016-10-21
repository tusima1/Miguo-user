package com.fanwe.seller.model.getBusinessListings;

import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelBusinessListings {
    private String address;

    private String distance;

    private String fx_total_num;

    private String shop_name;

    private String fx_num;

    private String geo_y;

    private String geo_x;

    private String index_img;

    private String is_endorsement;

    private String ref_avg_price;

    private String tuan_name;

    private String tuan_num;

    private String id;

    private String avg_grade;

    public String calculateDistance() {
        double dis = 0;
        if (DataFormat.toDouble(geo_x) != 0 && DataFormat.toDouble(geo_y) != 0) {
            dis = BaiduMapManager.getInstance().getDistanceFromMyLocation(DataFormat.toDouble(geo_y), DataFormat.toDouble(geo_x));
        }
        return SDDistanceUtil.getFormatDistance(dis);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setFx_total_num(String fx_total_num) {
        this.fx_total_num = fx_total_num;
    }

    public String getFx_total_num() {
        return this.fx_total_num;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_name() {
        return this.shop_name;
    }

    public void setFx_num(String fx_num) {
        this.fx_num = fx_num;
    }

    public String getFx_num() {
        return this.fx_num;
    }

    public void setGeo_y(String geo_y) {
        this.geo_y = geo_y;
    }

    public String getGeo_y() {
        return this.geo_y;
    }

    public void setGeo_x(String geo_x) {
        this.geo_x = geo_x;
    }

    public String getGeo_x() {
        return this.geo_x;
    }

    public void setIndex_img(String index_img) {
        this.index_img = index_img;
    }

    public String getIndex_img() {
        return this.index_img;
    }

    public void setIs_endorsement(String is_endorsement) {
        this.is_endorsement = is_endorsement;
    }

    public String getIs_endorsement() {
        return this.is_endorsement;
    }

    public void setRef_avg_price(String ref_avg_price) {
        this.ref_avg_price = ref_avg_price;
    }

    public String getRef_avg_price() {
        return this.ref_avg_price;
    }

    public void setTuan_name(String tuan_name) {
        this.tuan_name = tuan_name;
    }

    public String getTuan_name() {
        return this.tuan_name;
    }

    public void setTuan_num(String tuan_num) {
        this.tuan_num = tuan_num;
    }

    public String getTuan_num() {
        return this.tuan_num;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setAvg_grade(String avg_grade) {
        this.avg_grade = avg_grade;
    }

    public String getAvg_grade() {
        return this.avg_grade;
    }

}
