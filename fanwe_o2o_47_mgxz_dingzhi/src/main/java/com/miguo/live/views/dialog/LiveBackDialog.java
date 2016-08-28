package com.miguo.live.views.dialog;

import android.content.Context;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.category.dialog.LiveBackDialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class LiveBackDialog extends BaseDialog{

    LiveBackDialogCategory.OnLiveBackClickListener onLiveBackClickListener;

    public LiveBackDialog(Context context) {
        super(context);
    }

    @Override
    protected DialogCategory initCategory() {
        return new LiveBackDialogCategory(this);
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_live_host_exit_1;
    }

    public LiveBackDialogCategory.OnLiveBackClickListener getOnLiveBackClickListener() {
        return onLiveBackClickListener;
    }

    public LiveBackDialog setOnLiveBackClickListener(LiveBackDialogCategory.OnLiveBackClickListener onLiveBackClickListener) {
        this.onLiveBackClickListener = onLiveBackClickListener;
        return this;
    }
}
