package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页精选推荐列表接口
 */
public interface FeaturedGrouponDao extends BaseDao{

    void getFeaturedGroupBuy(String cityId, String pageNum, String pageSize, String keyword, String m_longitude, String m_latitude);

}
