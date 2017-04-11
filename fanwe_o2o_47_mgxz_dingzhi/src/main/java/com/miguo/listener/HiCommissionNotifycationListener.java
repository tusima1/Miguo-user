package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                clickBack();
                break;
        }
    }


    private void clickBack(){
        getCategory().clickBack();
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
