package com.miguo.live.views.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * 提示主播直播中
 */
public class HintHostDialog extends BaseDialog {
    Button btnClose;

    public HintHostDialog(Context context) {
        super(context);
        preView();
    }

    public void setCloseListener(View.OnClickListener onClickListenerClose) {
        btnClose.setOnClickListener(onClickListenerClose);
    }

    private void preView() {
        btnClose = (Button) findViewById(R.id.cancel_action);
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_hint_host;
    }

}