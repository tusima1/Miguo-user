package com.fanwe.dao.barry;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface GetSpecialListDao{
    void getSpecialList(String httpUuid, String city_id, String cur_geo_x, String cur_geo_y, String page);
}
