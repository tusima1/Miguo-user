package com.miguo.live.views.dialog;

import android.content.Context;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class GetDiamondInputDialog extends BaseDialog {

    public GetDiamondInputDialog(Context context) {
        super(context);
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
