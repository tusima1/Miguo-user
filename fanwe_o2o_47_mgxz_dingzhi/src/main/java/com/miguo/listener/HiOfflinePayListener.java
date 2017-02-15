package com.miguo.listener;

import android.view.View;
import android.widget.CompoundButton;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/14.
 */
public class HiOfflinePayListener extends Listener {

    public HiOfflinePayListener(Category category) {
        super(category);
    }


    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.do_not_participate_in_the_amount_of_concessions_text:
                clickWithoutDiscountAmount();
                break;
        }
    }

    private void clickWithoutDiscountAmount(){
        getCategory().clickWithoutDiscountAmount();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getCategory().onTextChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getCategory().onCheckedChanged(isChecked);
    }

    @Override
    public HiOfflinePayCategory getCategory() {
        return (HiOfflinePayCategory)super.getCategory();
    }
}
