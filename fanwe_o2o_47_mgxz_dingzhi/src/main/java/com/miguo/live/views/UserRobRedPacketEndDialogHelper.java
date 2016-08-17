package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.RedPacketRobEndAdapter;
import com.miguo.live.interf.IHelper;

/**
 * Created by didik on 2016/8/17.
 */
public class UserRobRedPacketEndDialogHelper implements IHelper {

    private boolean mRob;
    private Activity mActivity;
    private Dialog dialog;

    public UserRobRedPacketEndDialogHelper(Activity activity, boolean isRob) {
        this.mActivity = activity;
        this.mRob = isRob;
        createDialog();
    }

    @Override
    public void onDestroy() {

    }

    private void createDialog() {
        dialog = new Dialog(mActivity, R.style.floag_dialog);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout
                .dialog_live_rob_red_packet_end, null);
        bindContentView(contentView);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void bindContentView(View contentView) {
        View robView = LayoutInflater.from(mActivity).inflate(mRob ? R.layout.dialog_item_robed :
                R.layout
                .dialog_item_unrob, null);
        FrameLayout container = (FrameLayout) contentView.findViewById(R.id.fr_container);
        container.addView(robView);

        RecyclerView mReList = (RecyclerView) robView.findViewById(R.id.re_list);
        mReList.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));

        //adapter
        RedPacketRobEndAdapter endAdapter=new RedPacketRobEndAdapter();
        mReList.setAdapter(endAdapter);

        robView.findViewById(R.id.tv_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
