package com.miguo.live.views.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class GetDiamondLoginDialog extends BaseDialog {
    Button btnSubmit;
    ImageView ivClose;

    public GetDiamondLoginDialog(Context context) {
        super(context);
        preView();
    }

    public void setSubmitListener(View.OnClickListener onClickListenerSubmit) {
        btnSubmit.setOnClickListener(onClickListenerSubmit);
    }

    public void setCloseListener(View.OnClickListener onClickListenerClose) {
        ivClose.setOnClickListener(onClickListenerClose);
    }

    private void preView() {
        btnSubmit = (Button) findViewById(R.id.btn_submit_dialog_get_diamond_login);
        ivClose = (ImageView) findViewById(R.id.iv_close_dialog_get_diamond_login);
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_get_diamond_login;
    }

}
