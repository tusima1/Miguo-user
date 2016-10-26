package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.LiveRecordListener;
import com.tencent.av.TIMAvManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;

/**
 * Created by didik on 2016/7/26.
 * 直播的录制
 */
public class LiveRecordDialogHelper {
    private static final String TAG = "LiveRecordDialogHelper";
    private LiveHelper mLiveHelper;
    private Activity mActivity;
    private Dialog recordDialog;
    private TIMAvManager.RecordParam mRecordParam;
    private String filename = "";
    private String tags = "";
    private String classId = "";
    private EditText filenameEditText, tagEditText, classEditText;
    private CheckBox trancodeCheckBox, screenshotCheckBox, watermarkCheckBox;

    public LiveRecordDialogHelper(Activity activity, LiveHelper liveHelper) {
        this.mActivity = activity;
        this.mLiveHelper = liveHelper;
        createDialog();
    }

    private void createDialog() {
        recordDialog = new Dialog(mActivity, R.style.dialog);
        recordDialog.setContentView(R.layout.record_param);
        mRecordParam = TIMAvManager.getInstance().new RecordParam();

        filenameEditText = (EditText) recordDialog.findViewById(R.id.record_filename);
        tagEditText = (EditText) recordDialog.findViewById(R.id.record_tag);
        classEditText = (EditText) recordDialog.findViewById(R.id.record_class);
        trancodeCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_tran_code);
        screenshotCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_screen_shot);
        watermarkCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_water_mark);

        if (filename.length() > 0) {
            filenameEditText.setText(filename);
        }
        filenameEditText.setText("" + CurLiveInfo.getRoomNum());

        if (tags.length() > 0) {
            tagEditText.setText(tags);
        }

        if (classId.length() > 0) {
            classEditText.setText(classId);
        }
        Button recordOk = (Button) recordDialog.findViewById(R.id.btn_record_ok);
        recordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename = filenameEditText.getText().toString();
                mRecordParam.setFilename(filename);
                tags = tagEditText.getText().toString();
                classId = classEditText.getText().toString();
                if (TextUtils.isEmpty(classId)) {
                    return;
                }
                mRecordParam.setClassId(Integer.parseInt(classId));
                mRecordParam.setTransCode(trancodeCheckBox.isChecked());
                mRecordParam.setSreenShot(screenshotCheckBox.isChecked());
                mRecordParam.setWaterMark(watermarkCheckBox.isChecked());
                mLiveHelper.startRecord(mRecordParam);
                if (mListener != null) {
                    mListener.startRecord();
                }
                recordDialog.dismiss();
            }
        });
        Button recordCancel = (Button) recordDialog.findViewById(R.id.btn_record_cancel);
        recordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDialog.dismiss();
            }
        });
        Window dialogWindow = recordDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        recordDialog.setCanceledOnTouchOutside(false);
    }


    /**
     * 展示出来
     */
    public void show() {
        if (recordDialog.isShowing()) {

        } else {
            recordDialog.show();
        }
    }

    LiveRecordListener mListener;

    public void setOnLiveRecordListener(LiveRecordListener listener) {
        this.mListener = listener;
    }


}
