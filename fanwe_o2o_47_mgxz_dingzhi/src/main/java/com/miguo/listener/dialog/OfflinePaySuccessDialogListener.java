package com.miguo.listener.dialog;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.DialogFragmentCategory;
import com.miguo.category.dialog.OfflinePaySuccessDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/20.
 */

public class OfflinePaySuccessDialogListener extends DialogFragmentListener {

    public OfflinePaySuccessDialogListener(DialogFragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.cancel:
            case R.id.sure:
                clickSure();
                break;
        }
    }

    private void clickSure(){
        getCategory().clickSure();
    }

    @Override
    public OfflinePaySuccessDialogCategory getCategory() {
        return (OfflinePaySuccessDialogCategory)super.getCategory();
    }
}
