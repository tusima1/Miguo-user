package com.fanwe.sellTemp.model;

/**
 * Created by Administrator on 2016/8/4.
 */
public class SellerTempConstants {
    /**
     * 商家按批次取得，生成，删除红包
     */
    public static final String CREATE_RED_PACKET = "CreateRedPacket";
    public static final String GET_CREATE_RED_PACKET = "GetCreateRedPacket";
    public static final String POST_CREATE_RED_PACKET = "PostCreateRedPacket";
    public static final String DELETE_CREATE_RED_PACKET = "DeleteCreateRedPacket";
    /**
     * 商家分配指定批次的红包给旗下的代言人（先平均后随机）
     */
    public static final String DISTRIBUTION_RED_PACKET = "DistributionRedPacket";
}
