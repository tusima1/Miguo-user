package com.miguo.live.views.category.dialog;

import android.util.Log;
import android.widget.TextView;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.dialog.BaseDialog;
import com.miguo.live.views.dialog.LiveBackDialog;
import com.miguo.live.views.listener.dialog.DialogListener;
import com.miguo.live.views.listener.dialog.LiveBackDialogListener;

/**
 * Created by zlh on 2016/8/26.
 */
public class LiveBackDialogCategory extends DialogCategory{


    TextView tvSure;
    TextView tvCancel;


    public LiveBackDialogCategory(BaseDialog dialog) {
        super(dialog);
    }

    @Override
    protected void findViews() {
//        ViewUtils.inject(this, getDialog().getOwnerActivity());
        tvSure = findViewsById(R.id.sure_action);
        tvCancel = findViewsById(R.id.cancel_action);
    }

    @Override
    protected DialogListener initListener() {
        return new LiveBackDialogListener(this);
    }

    @Override
    protected void setListener() {
        Log.d(TAG, "tvSureis null" + tvSure);
        tvSure.setOnClickListener(listener);
        tvCancel.setOnClickListener(listener);
    }

    @Override
    protected void init() {
//        showToast("tv is null : " + (tvSure == null) + " ,onLiveBackClickListener is null: " + (getDialog().getOnLiveBackClickListener() == null));
    }

    public void clickSure(){
        if(getDialog().getOnLiveBackClickListener() != null){
            getDialog().getOnLiveBackClickListener().clickSure();
        }
    }

    public void clickCancel(){
        if(getDialog().getOnLiveBackClickListener() != null){
            getDialog().getOnLiveBackClickListener().clickCancel();
        }
    }

    public interface OnLiveBackClickListener{
        void clickSure();
        void clickCancel();
    }

    @Override
    public LiveBackDialog getDialog() {
        return (LiveBackDialog) super.getDialog();
    }
}
