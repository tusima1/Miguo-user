package com.miguo.live.views.dialog;

import android.content.Context;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.category.dialog.LivePushDialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class LivePushDialog extends BaseDialog{

    LivePushDialogCategory.OnLivePushClickListener onLivePushClickListener;

    public LivePushDialog(Context context) {
        super(context);
    }

    @Override
    protected DialogCategory initCategory() {
        return new LivePushDialogCategory(this);
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.push_dialog_layout;
    }

    public LivePushDialogCategory.OnLivePushClickListener getOnLivePushClickListener() {
        return onLivePushClickListener;
    }

    public void setOnLivePushClickListener(LivePushDialogCategory.OnLivePushClickListener onLivePushClickListener) {
        this.onLivePushClickListener = onLivePushClickListener;
    }
}
