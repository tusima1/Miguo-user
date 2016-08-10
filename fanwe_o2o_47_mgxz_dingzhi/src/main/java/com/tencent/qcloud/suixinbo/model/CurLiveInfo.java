package com.tencent.qcloud.suixinbo.model;


import com.fanwe.seller.model.getShopList.ModelShopList;

/**
 * 当前直播信息页面
 */
public class CurLiveInfo {
    private static int members = 0;//房间人数
    private static int admires = 0;//心的数量
    private static String title;
    private static double lat1;
    private static double long1;
    private static String address = "";
    private static String coverurl = "";

    public static int roomNum;//room id(chatRoomId 其实就是房间id的string格式)

    /**
     * 主播房间id信息
     **/
    public static String hostID;
    public static String hostName;
    public static String hostAvator;

    public static int currentRequestCount = 0;
    /**
     * 门店ID.
     */
    public static String shopID;

    public static int getCurrentRequestCount() {
        return currentRequestCount;
    }

    public static int getIndexView() {
        return indexView;
    }

    public static void setIndexView(int indexView) {
        CurLiveInfo.indexView = indexView;
    }

    public static int indexView = 0;
    ///店铺信息
    public static ModelShopList modelShop = new ModelShopList();

    public static void setCurrentRequestCount(int currentRequestCount) {
        CurLiveInfo.currentRequestCount = currentRequestCount;
    }

    public static String getHostID() {
        return hostID;
    }

    public static void setHostID(String hostID) {
        CurLiveInfo.hostID = hostID;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        CurLiveInfo.hostName = hostName;
    }

    public static String getHostAvator() {
        return hostAvator;
    }

    public static void setHostAvator(String hostAvator) {
        CurLiveInfo.hostAvator = hostAvator;
    }


    public static int getMembers() {
        return members;
    }

    public static void setMembers(int members) {
        CurLiveInfo.members = members;
    }

    public static int getAdmires() {
        return admires;
    }

    public static void setAdmires(int admires) {
        CurLiveInfo.admires = admires;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        CurLiveInfo.title = title;
    }

    public static double getLat1() {
        return lat1;
    }

    public static void setLat1(double lat1) {
        CurLiveInfo.lat1 = lat1;
    }

    public static double getLong1() {
        return long1;
    }

    public static void setLong1(double long1) {
        CurLiveInfo.long1 = long1;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        CurLiveInfo.address = address;
    }

    public static int getRoomNum() {
        return roomNum;
    }

    public static void setRoomNum(int roomNum) {
        CurLiveInfo.roomNum = roomNum;
    }

    public static String getCoverurl() {
        return coverurl;
    }

    public static void setCoverurl(String coverurl) {
        CurLiveInfo.coverurl = coverurl;
    }

    public static String getChatRoomId() {
        return "" + roomNum;
    }

    public static ModelShopList getModelShop() {
        return modelShop;
    }

    public static void setModelShop(ModelShopList modelShop) {
        CurLiveInfo.modelShop = modelShop;
    }

    public static String getShopID() {
        return shopID;
    }

    public static void setShopID(String shopID) {
        CurLiveInfo.shopID = shopID;
    }
}
