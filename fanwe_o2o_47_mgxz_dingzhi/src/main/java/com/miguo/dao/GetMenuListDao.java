package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页菜单列表接口
 */
public interface GetMenuListDao extends BaseDao{

    void getMenuList(String terminal_type, String menu_type);

}
