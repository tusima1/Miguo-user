package com.miguo.live.views.view.model;

/**
 * Created by Administrator on 2016/8/28.
 */
public interface StoreListModel extends BaseModel{

    /**
     * 请求门店列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @param cityId
     */
    void getStoreList(int pageNum, int pageSize, String type, String cityId);

}
