package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/26.
 * 首页广告baner和topic接口
 */
public interface GetAdspaceListDao extends BaseDao{

    void getAdspaceList(String httpUuid, String city_id,String type, String terminal_type);

}
