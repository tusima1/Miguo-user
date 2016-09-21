package com.miguo.live.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * Created by Administrator on 2016/8/26.
 */
public abstract class BaseDialog extends Dialog{

    int layoutId;
    DialogCategory category;

    public BaseDialog(Context context) {
        this(context, R.style.floag_dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initThis();
    }

    private void initThis(){
        initParams();
        layoutId = setDialogContentView();
        setContentView(layoutId);
        category = initCategory();

    }

    private void initParams(){
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
    }

    protected abstract DialogCategory initCategory();

    protected abstract int setDialogContentView();

    public BaseDialog setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside){
        super.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }
}
