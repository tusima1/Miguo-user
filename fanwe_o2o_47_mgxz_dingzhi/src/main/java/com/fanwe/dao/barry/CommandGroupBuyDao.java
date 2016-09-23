package com.fanwe.dao.barry;

/**
 * Created by 狗蛋哥/zlh on 2016/9/23.
 * 获取首页团购列表接口
 */
public interface CommandGroupBuyDao {
    void getCommandGroupBuyDaoList(int page, int page_size, String tag, String keyword,  String city);
}
