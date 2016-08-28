package com.miguo.live.views.category.dialog;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.dialog.BaseDialog;
import com.miguo.live.views.dialog.LivePushDialog;
import com.miguo.live.views.listener.dialog.DialogListener;
import com.miguo.live.views.listener.dialog.LivePushDialogListener;
import com.tencent.av.TIMAvManager;

/**
 * Created by zlh on 2016/8/26.
 */
public class LivePushDialogCategory extends DialogCategory{

    Button recordOk;
    Button recordCancel;
    EditText pushfileNameInput;
    RadioGroup radgroup;
    TIMAvManager.StreamParam mStreamParam;


    public LivePushDialogCategory(BaseDialog dialog) {
        super(dialog);
    }

    @Override
    protected void findViews() {
        pushfileNameInput = findViewsById(R.id.push_filename);
        recordOk = findViewsById(R.id.btn_record_ok);
        recordCancel = findViewsById(R.id.btn_record_cancel);
        radgroup = findViewsById(R.id.push_type);
    }

    @Override
    protected DialogListener initListener() {
        return new LivePushDialogListener(this);
    }

    @Override
    protected void setListener() {
        recordOk.setOnClickListener(listener);
        recordCancel.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        mStreamParam = TIMAvManager.getInstance().new StreamParam();
    }

    public void clickRecordOk(){
        if (pushfileNameInput.getText().toString().equals("")) {
            Toast.makeText(getDialog().getContext(), "name can't be empty", Toast.LENGTH_SHORT);
            return;
        } else {
            mStreamParam.setChannelName(pushfileNameInput.getText().toString());
        }

        if (radgroup.getCheckedRadioButtonId() == R.id.hls) {
            mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS);
        } else {
            mStreamParam.setEncode(TIMAvManager.StreamEncode.RTMP);
        }
        if(getDialog().getOnLivePushClickListener() != null){
            getDialog().getOnLivePushClickListener().clickRecordOk(mStreamParam);
        }
        clickRecordCancel();
    }

    public void clickRecordCancel(){
        getDialog().dismiss();
    }

    public interface OnLivePushClickListener{
        void clickRecordOk(TIMAvManager.StreamParam streamParam);
    }

    @Override
    public LivePushDialog getDialog() {
        return (LivePushDialog) super.getDialog();
    }
}
