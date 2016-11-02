package com.fanwe.customview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/10/31.
 */

public class MGProgressDialog extends ProgressDialog {

    private Activity mAttachActivity;
    public MGProgressDialog(Context context) {
        this(context,R.style.MGProgressDialog);
    }

    public MGProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
//        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    public MGProgressDialog needFinishActivity(Activity activity){
        mAttachActivity=activity;
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        this.dismiss();
        if (mAttachActivity!=null){
            mAttachActivity.finish();
        }
    }
}
