package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiUpdateUserCategory;

/**
 * Created by zlh on 2016/12/14.
 */

public class HiUpdateUserListener extends Listener {

    public HiUpdateUserListener(Category category) {
        super(category);
    }

    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.update_by_withholding:
                clickUpdateByWithholding();
                break;
            case R.id.update_by_wechat:
                clickUpdateByWechat();
                break;
            case R.id.update_by_alipay:
                clickByUpdateByAlipay();
                break;
        }
    }

    private void clickUpdateByWithholding(){
        getCategory().clickUpdateByWithholding();
    }

    private void clickUpdateByWechat(){
        getCategory().clickUpdateByWechat();
    }

    private void clickByUpdateByAlipay(){
        getCategory().clickByUpdateByAlipay();
    }

    @Override
    public HiUpdateUserCategory getCategory() {
        return (HiUpdateUserCategory)super.getCategory();
    }
}
