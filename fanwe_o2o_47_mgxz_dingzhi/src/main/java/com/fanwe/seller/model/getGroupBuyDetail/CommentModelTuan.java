package com.fanwe.seller.model.getGroupBuyDetail;

import com.fanwe.seller.model.getShopInfo.ImageShopInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CommentModelTuan {

    private String id;

    private String tuan_id;

    private String user_id;

    private String point;

    private String content;

    private String parent_comment;

    private String comment_time;

    private String is_enable;

    private String has_image;

    private List<ImageShopInfo> images;

    private List<ImageShopInfo> oimages;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTuan_id() {
        return tuan_id;
    }

    public void setTuan_id(String tuan_id) {
        this.tuan_id = tuan_id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParent_comment() {
        return parent_comment;
    }

    public void setParent_comment(String parent_comment) {
        this.parent_comment = parent_comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getHas_image() {
        return has_image;
    }

    public void setHas_image(String has_image) {
        this.has_image = has_image;
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
