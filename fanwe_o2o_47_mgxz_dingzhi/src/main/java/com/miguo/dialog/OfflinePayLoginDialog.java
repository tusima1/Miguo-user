package com.miguo.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.dialog.OfflinePayLoginDialogCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */

public class OfflinePayLoginDialog extends HiBaseDialog {

    OnOfflinePayDialogListener onOfflinePayDialogListener;

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_activity_offline_pay_login, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new OfflinePayLoginDialogCategory(cacheView, this);
    }

    public interface OnOfflinePayDialogListener{
        void loginSuccess();
    }

    public void setOnOfflinePayDialogListener(OnOfflinePayDialogListener onOfflinePayDialogListener) {
        this.onOfflinePayDialogListener = onOfflinePayDialogListener;
    }

    public OnOfflinePayDialogListener getOnOfflinePayDialogListener() {
        return onOfflinePayDialogListener;
    }
}
