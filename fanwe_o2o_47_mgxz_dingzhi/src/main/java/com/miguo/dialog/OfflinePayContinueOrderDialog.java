package com.miguo.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.OfflinePayContinueOrderDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OfflinePayContinueOrderDialog extends HiBaseDialog {

    OnOfflinePayContinueOrderDialogListener onOfflinePayContinueOrderDialogListener;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_activity_offline_pay_continue_order, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new OfflinePayContinueOrderDialogCategory(cacheView, this);
    }

    public interface OnOfflinePayContinueOrderDialogListener{
        void continueOrder();
    }

    public void setOnOfflinePayContinueOrderDialogListener(OnOfflinePayContinueOrderDialogListener onOfflinePayContinueOrderDialogListener) {
        this.onOfflinePayContinueOrderDialogListener = onOfflinePayContinueOrderDialogListener;
    }

    public OnOfflinePayContinueOrderDialogListener getOnOfflinePayContinueOrderDialogListener() {
        return onOfflinePayContinueOrderDialogListener;
    }
}
