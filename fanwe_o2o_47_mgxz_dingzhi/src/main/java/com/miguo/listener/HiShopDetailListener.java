package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiShopDetailCategory;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HiShopDetailListener extends Listener{

    public HiShopDetailListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call:
                clickCall();
                break;
            case R.id.location:
                clickLocation();
                break;
            case R.id.collect:
                clickCollect();
                break;
        }
    }

    private void clickCall(){
        getCategory().clickCall();
    }

    private void clickLocation(){
        getCategory().clickLocation();
    }

    private void clickCollect(){
        getCategory().clickCollect();
    }

    @Override
    public HiShopDetailCategory getCategory() {
        return (HiShopDetailCategory) super.getCategory();
    }
}
