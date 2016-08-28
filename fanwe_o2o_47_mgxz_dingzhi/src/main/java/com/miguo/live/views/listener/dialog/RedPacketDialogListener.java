package com.miguo.live.views.listener.dialog;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.category.dialog.RedPacketDialogCategory;

/**
 * Created by Administrator on 2016/8/26.
 */
public class RedPacketDialogListener extends DialogListener{

    public RedPacketDialogListener(DialogCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit_dialog_red:
                clickSure();
                break;
            case R.id.cancel_btn:
                clickCancel();
                break;
        }
    }

    private void clickSure(){
        getCategory().clickSure();
    }

    private void clickCancel(){
        getCategory().clickCancel();
    }

    @Override
    public RedPacketDialogCategory getCategory() {
        return (RedPacketDialogCategory) super.getCategory();
    }
}
