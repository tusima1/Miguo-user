package com.miguo.live.views.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * 提示用户直播中
 */
public class HintMemberDialog extends BaseDialog {
    Button btnSure;
    Button btnClose;

    public HintMemberDialog(Context context) {
        super(context);
        preView();
    }

    public void setCloseListener(View.OnClickListener onClickListenerClose) {
        btnClose.setOnClickListener(onClickListenerClose);
    }

    public void setSureListener(View.OnClickListener onClickListenerClose) {
        btnSure.setOnClickListener(onClickListenerClose);
    }

    private void preView() {
        btnSure = (Button) findViewById(R.id.sure_action);
        btnClose = (Button) findViewById(R.id.cancel_action);
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_hint_member;
    }

}