package com.miguo.live.views.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.OnMeiProgressFinishListener;

/**
 * Created by didik on 2016/8/5.
 */
public class MeiSeekBarDialog{
    private Activity mActivity;
    private Dialog dialog;
    private TextView mProgress;
    private TextView mTitle;
    private SeekBar mSeekBar;
    private OnMeiProgressFinishListener mListener;
    private Button mBt_ensure;

    public MeiSeekBarDialog(Activity activity) {
        this.mActivity=activity;
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog(mActivity, R.style.dialog);
        dialog.setContentView(R.layout.dialog_live_mei_mei);
        mProgress = ((TextView) dialog.findViewById(R.id.tv_progress));
        mTitle = ((TextView) dialog.findViewById(R.id.tv_title));
        mBt_ensure = ((Button) dialog.findViewById(R.id.sure_action));

        mSeekBar = ((SeekBar) dialog.findViewById(R.id.seekbar));

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBt_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    String s = mProgress.getText().toString();
                    String substring = s.substring(0, s.length() - 1);
                    int progress=-1;
                    try {
                        progress=Integer.valueOf(substring);
                    }catch (Exception e){
                        MGToast.showToast("剪切字符串错误!");
                    }
                    mListener.finalProgress(progress);
                }
                dialog.dismiss();
            }
        });


        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(){
        if (dialog!=null){
            dialog.show();
        }
    }

    public void setTitle(String title){
        if (!TextUtils.isEmpty(title)){
            if ("美颜".equals(title)){
                title="美 颜";
            }else if ("美白".equals(title)){
                title="美 白";
            }
            mTitle.setText(title);
        }
    }

    public void setOnMeiProgressFinishListener(OnMeiProgressFinishListener listener){
        this.mListener=listener;
    }

}
