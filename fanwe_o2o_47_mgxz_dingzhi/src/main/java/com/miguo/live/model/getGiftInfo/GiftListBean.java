package com.miguo.live.model.getGiftInfo;

/**
 * Created by didik on 2016/9/12.
 */
public class GiftListBean {
//    "price":"30.00",     价格（果钻）
//            "icon":"1",          图标地址（七牛云）
//            "name":"测试礼物1",  礼物名称
//    "id":"1",            礼物id，用于和效果图做映射
//    "type":"2",          类型：1、弹幕 2、小礼物 3、大礼物
//    "is_delete":"0"      删除标识：0、存在，1、删除
    private String price;
    private String icon;
    private String name;
    private String id;
    private String type;
    private String is_delete;

    private String userName;
    private String userAvatar;
    private String userId;
    private int num;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
