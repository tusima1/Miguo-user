package com.miguo.listener;

import com.fanwe.view.LoadMoreRecyclerView;
import com.miguo.category.Category;
import com.miguo.category.HiCommissionNotifycationCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/16.
 * 佣金代言消息列表
 */
public class HiCommissionNotifycationListener extends Listener implements LoadMoreRecyclerView.OnRefreshEndListener {


    public HiCommissionNotifycationListener(Category category) {
        super(category);
    }

    @Override
    public void onLoadmore() {
        getCategory().onLoadmore();
    }

    @Override
    public void onMoveTop() {

    }

    @Override
    public HiCommissionNotifycationCategory getCategory() {
        return (HiCommissionNotifycationCategory)super.getCategory();
    }
}
