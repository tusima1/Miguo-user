package com.miguo.dao;

/**
 * Created by 狗蛋哥 on 2016/11/23.
 * 获取有趣页对应tab的直播列表
 */
public interface InterestingLiveVideoDao extends BaseDao {
    void getInterestingLiveVideo(int page, int page_size, String tab_id, String city_id);
}
