package com.miguo.listener.dialog;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.DialogFragmentCategory;
import com.miguo.category.dialog.OfflinePayContinueOrderDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OfflinePayContinueOrderDialogListener extends DialogFragmentListener {

    public OfflinePayContinueOrderDialogListener(DialogFragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.cancel:
            case R.id.cancel_order:
                clickCancel();
                break;
            case R.id.continue_order:
                clickContinue();
                break;
        }
    }

    private void clickCancel(){
        getCategory().clickCancel();
    }

    private void clickContinue(){
        getCategory().clickContinue();
    }

    @Override
    public OfflinePayContinueOrderDialogCategory getCategory() {
        return (OfflinePayContinueOrderDialogCategory)super.getCategory();
    }
}
