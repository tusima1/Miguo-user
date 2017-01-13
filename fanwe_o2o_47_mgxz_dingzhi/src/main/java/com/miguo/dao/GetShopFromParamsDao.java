package com.miguo.dao;

import com.miguo.entity.RepresentFilterBean;

/**
 * Created by zlh on 2017/1/13.
 */

public interface GetShopFromParamsDao extends BaseDao{

    /**
     * 新版商家搜索
     *
     * @param area_one      区域一级
     * @param area_two      区域二级
     * @param category_one  类目一级
     * @param category_two  类目二级
     * @param filter        筛选
     * @param keyword       关键字
     * @param sort_type     排序类型
     * @param pageNum       页码
     * @param pageSize      页大小
     * @param merchant_type 商家类型：1，优惠商家  0，全部商家
     */
    void getShop(String area_one, String area_two, String category_one, String category_two, String filter,String keyword, String sort_type, int pageNum, int pageSize, String merchant_type);
    void getShop(RepresentFilterBean bean);
}
