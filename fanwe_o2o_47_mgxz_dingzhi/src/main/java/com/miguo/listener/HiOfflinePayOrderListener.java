package com.miguo.listener;

import android.view.View;
import android.widget.CompoundButton;

import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiOfflinePayOrderActivity;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayOrderCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class HiOfflinePayOrderListener extends Listener {

    public HiOfflinePayOrderListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.pay_order:
                clickPay();
                break;
        }
    }

    private void clickPay(){
        getCategory().clickPay();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        super.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public HiOfflinePayOrderCategory getCategory() {
        return (HiOfflinePayOrderCategory)super.getCategory();
    }
}
