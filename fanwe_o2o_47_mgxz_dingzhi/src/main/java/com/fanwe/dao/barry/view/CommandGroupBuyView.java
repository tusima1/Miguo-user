package com.fanwe.dao.barry.view;

import com.fanwe.model.CommandGroupBuyBean;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface CommandGroupBuyView {
    void getCommandGroupBuyDaoListSuccess(CommandGroupBuyBean.Result result);
    void getCommandGroupBuyDaoListLoadMore(CommandGroupBuyBean.Result result);
    void getCommandGroupBuyDaoListError(String msg);
}
