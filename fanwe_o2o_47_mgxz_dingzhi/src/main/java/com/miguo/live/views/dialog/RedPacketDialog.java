package com.miguo.live.views.dialog;

import android.content.Context;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.category.dialog.RedPacketDialogCategory;

/**
 * Created by Administrator on 2016/8/26.
 */
public class RedPacketDialog extends BaseDialog{

    RedPacketDialogCategory.OnRedPacketClickListener onRedPacketClickListener;

    public RedPacketDialog(Context context) {
        super(context);
    }

    @Override
    protected DialogCategory initCategory() {
        return new RedPacketDialogCategory(this);
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_live_red_packet;
    }

    public RedPacketDialogCategory.OnRedPacketClickListener getOnRedPacketClickListener() {
        return onRedPacketClickListener;
    }

    public void setOnRedPacketClickListener(RedPacketDialogCategory.OnRedPacketClickListener onRedPacketClickListener) {
        this.onRedPacketClickListener = onRedPacketClickListener;
    }
}
