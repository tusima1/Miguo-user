package com.miguo.live.views.listener.dialog;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.category.dialog.LivePushDialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class LivePushDialogListener extends DialogListener{

    public LivePushDialogListener(DialogCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_record_ok:
                clickRecordOk();
                break;
            case R.id.btn_record_cancel:
                clickRecordCancel();
                break;
        }
    }

    private void clickRecordOk(){
        getCategory().clickRecordOk();
    }

    private void clickRecordCancel(){
        getCategory().clickRecordCancel();
    }

    @Override
    public LivePushDialogCategory getCategory() {
        return (LivePushDialogCategory) super.getCategory();
    }
}
