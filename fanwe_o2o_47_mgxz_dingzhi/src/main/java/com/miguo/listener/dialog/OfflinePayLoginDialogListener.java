package com.miguo.listener.dialog;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.DialogFragmentCategory;
import com.miguo.category.dialog.OfflinePayLoginDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */

public class OfflinePayLoginDialogListener extends DialogFragmentListener {

    public OfflinePayLoginDialogListener(DialogFragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.cancel:
                clickCancel();
                break;
            case R.id.sure:
                clickSure();
                break;
            case R.id.get_sms_code:
                clickSMSCode();
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        getCategory().onTextChanged();
    }

    private void clickSMSCode(){
        getCategory().clickSMSCode();
    }

    private void clickSure(){
        getCategory().clickSure();
    }

    private void clickCancel(){
        getCategory().clickCancel();
    }

    @Override
    public OfflinePayLoginDialogCategory getCategory() {
        return (OfflinePayLoginDialogCategory)super.getCategory();
    }
}
