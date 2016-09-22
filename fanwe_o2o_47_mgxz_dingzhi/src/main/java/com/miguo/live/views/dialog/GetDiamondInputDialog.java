package com.miguo.live.views.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class GetDiamondInputDialog extends BaseDialog {
    EditText etCode;
    Button btnSubmit;
    ImageView ivClose;

    public GetDiamondInputDialog(Context context) {
        super(context);
        preView();
    }

    public void setSubmitListener(View.OnClickListener onClickListenerSubmit) {
        btnSubmit.setOnClickListener(onClickListenerSubmit);
    }

    public void setCloseListener(View.OnClickListener onClickListenerClose) {
        ivClose.setOnClickListener(onClickListenerClose);
    }

    public String getCode() {
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            code = "";
        }
        return code;
    }

    private void preView() {
        etCode = (EditText) findViewById(R.id.et_code_dialog_get_diamond);
        btnSubmit = (Button) findViewById(R.id.btn_submit_dialog_get_diamond);
        ivClose = (ImageView) findViewById(R.id.iv_close_dialog_get_diamond);
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_get_diamond_input;
    }

}
