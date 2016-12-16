package com.miguo.dao;

/**
 * Created by zlh on 2016/12/13.
 * 获取会员等级权限
 */
public interface MemberInterestDao extends BaseDao {

    void getMemberInterest(int level);
    void getMemberInterest();

}
