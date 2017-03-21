package com.miguo.listener;

import com.fanwe.view.LoadMoreRecyclerView;
import com.miguo.category.Category;
import com.miguo.category.HiSystemNotificationCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/15.
 *  * 系统消息列表
 */
public class HiSystemNotificationListener extends Listener implements LoadMoreRecyclerView.OnRefreshEndListener{

    public HiSystemNotificationListener(Category category) {
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
    public HiSystemNotificationCategory getCategory() {
        return (HiSystemNotificationCategory)super.getCategory();
    }
}
