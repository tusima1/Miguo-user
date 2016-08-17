package com.fanwe.seller.model.getShopInfo;

import java.io.Serializable;
import java.util.List;

public class CommentModelShopInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String shop_id;

    private String comment_time;

    private String user_id;

    private String is_enable;

    private String comment;

    private String id;

    private String point;
    private List<ImageShopInfo> images;
    private List<ImageShopInfo> oimages;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public List<ImageShopInfo> getImages() {
        return images;
    }

    public void setImages(List<ImageShopInfo> images) {
        this.images = images;
    }

    public List<ImageShopInfo> getOimages() {
        return oimages;
    }

    public void setOimages(List<ImageShopInfo> oimages) {
        this.oimages = oimages;
    }
}