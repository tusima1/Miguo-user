package com.miguo.dao;

/**
 * Created by Administrator on 2016/11/23.
 */

public interface HiFunnyTabDao extends BaseDao{

    /**
     *  获取有趣页直播和指南的标签列表
     * @param tab_set 0 直播 1 指南
     */
    void getFunnyTab(String tab_set);

}
