package com.miguo.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.OfflinePaySuccessDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/20.
 */

public class OfflinePaySuccessDialog extends HiBaseDialog {

    OfflinePaySuccessDialogListener offlinePaySuccessDialogListener;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_activity_offline_pay_success, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        setOnKeyListener();
        category = new OfflinePaySuccessDialogCategory(cacheView, this);
    }

    private void setOnKeyListener(){
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }else if(keyCode == KeyEvent.KEYCODE_MENU) {

                    return true;
                }
                return false;
            }
        });
    }

    public interface OfflinePaySuccessDialogListener{
        void onConfirm();
    }

    @Override
    public void dismiss() {
        if(null != getOfflinePaySuccessDialogListener()){
            getOfflinePaySuccessDialogListener().onConfirm();
        }
        super.dismiss();

    }

    public OfflinePaySuccessDialogListener getOfflinePaySuccessDialogListener() {
        return offlinePaySuccessDialogListener;
    }

    public void setOfflinePaySuccessDialogListener(OfflinePaySuccessDialogListener offlinePaySuccessDialogListener) {
        this.offlinePaySuccessDialogListener = offlinePaySuccessDialogListener;
    }
}
