package com.miguo.category.dialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.dialog.HiBaseDialog;
import com.miguo.dialog.OfflinePayContinueOrderDialog;
import com.miguo.listener.dialog.OfflinePayContinueOrderDialogListener;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OfflinePayContinueOrderDialogCategory extends DialogFragmentCategory {

    @ViewInject(R.id.content_layout)
    RelativeLayout content;

    @ViewInject(R.id.cancel)
    ImageView cancel;

    @ViewInject(R.id.cancel_order)
    TextView cancelOrder;

    @ViewInject(R.id.continue_order)
    TextView continueOrder;

    public OfflinePayContinueOrderDialogCategory(View view, HiBaseDialog fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new OfflinePayContinueOrderDialogListener(this);
    }

    @Override
    protected void setFragmentListener() {
        cancel.setOnClickListener(listener);
        cancelOrder.setOnClickListener(listener);
        continueOrder.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initContentPosition();
    }

    private void initContentPosition(){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(dip2px(273), dip2px(173));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, (int)(getScreenHeight() * 0.20), 0, 0);
        content.setLayoutParams(params);
    }

    public void clickCancel(){
        dismiss();
    }

    public void clickContinue(){
        if(null != getDialog().getOnOfflinePayContinueOrderDialogListener()){
            getDialog().getOnOfflinePayContinueOrderDialogListener().continueOrder();
        }
        dismiss();
    }

    @Override
    public OfflinePayContinueOrderDialog getDialog() {
        return (OfflinePayContinueOrderDialog)super.getDialog();
    }
}
