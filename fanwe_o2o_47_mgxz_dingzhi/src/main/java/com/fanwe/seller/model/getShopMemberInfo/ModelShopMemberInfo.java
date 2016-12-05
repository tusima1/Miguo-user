package com.fanwe.seller.model.getShopMemberInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelShopMemberInfo {
    private ArrayList<ModelFans> fansInfoList;
    private ArrayList<ModelMember> shopMemberInfoList;

    public ArrayList<ModelFans> getFansInfoList() {
        return fansInfoList;
    }

    public void setFansInfoList(ArrayList<ModelFans> fansInfoList) {
        this.fansInfoList = fansInfoList;
    }

    public ArrayList<ModelMember> getShopMemberInfoList() {
        return shopMemberInfoList;
    }

    public void setShopMemberInfoList(ArrayList<ModelMember> shopMemberInfoList) {
        this.shopMemberInfoList = shopMemberInfoList;
    }
}
