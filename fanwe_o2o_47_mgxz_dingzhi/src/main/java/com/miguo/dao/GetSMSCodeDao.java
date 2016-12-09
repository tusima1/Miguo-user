package com.miguo.dao;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface GetSMSCodeDao extends BaseDao {
    void getSMSCode(String mobile, int type);
    void getSMSCodeForRegister(String mobile);
    void getSMSCodeForModifyPassword(String mobile);
    void getSMSCodeForWithDraw(String mobile);
    void getSMSCodeForQuickLoginAndBindMobile(String mobile);
}
