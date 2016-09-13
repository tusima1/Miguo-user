package com.miguo.utils.test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/9/12.
 */
public class MGDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView mTv_title;
    private TextView mTv_Content;
    private Button mBt_cancel;
    private Button mBt_sure;
    private String mTitle;
    private int mTitleDrawableId;
    private String mSureText;
    private String mCancelText;
    private Object mTitleObj;
    private String mContentText;
    private OnCancelClickListener mCancelListener;
    private OnSureClickListener mSureListener;

    public MGDialog(Context context) {
        super(context,R.style.dialog);
        this.mContext=context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_normal_v2);

        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_Content = (TextView) findViewById(R.id.tv_content);
        mBt_cancel = (Button) findViewById(R.id.cancel_action);
        mBt_sure = (Button) findViewById(R.id.sure_action);

        mBt_cancel.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);


        setCancelActionText(mCancelText);
        setSureActionText(mSureText);
        setContentText(mContentText);
        setMGTitle(mTitleObj);
    }

    public MGDialog setSureActionText(String sure){
        this.mSureText=sure;
        if (!TextUtils.isEmpty(mSureText) && mBt_sure!=null) {
            mBt_sure.setText(mSureText);
        }
        return this;
    }

    public MGDialog setCancelActionText(String cancel){
        this.mCancelText=cancel;
        if (!TextUtils.isEmpty(mCancelText) && mBt_cancel!=null) {
            mBt_cancel.setText(mCancelText);
        }
        return this;
    }
    public MGDialog setContentText(String contentText){
        this.mContentText=contentText;
        if (!TextUtils.isEmpty(mContentText) && mTv_Content!=null) {
            mTv_Content.setText(mContentText);
        }
        return this;
    }

    public MGDialog setMGTitle(Object o){
        this.mTitleObj=o;
        if (mTv_title==null){
            return this;
        }
        if (mTitleObj instanceof String){
            mTitle = mTitleObj.toString();
        }else if (mTitleObj instanceof Integer){
            mTitleDrawableId = ((Integer) mTitleObj).intValue();
        }
        if (!TextUtils.isEmpty(mTitle)){
            mTv_title.setText(mTitle);
        }else if (mTitleDrawableId!=0){
            mTv_title.setBackgroundResource(mTitleDrawableId);
        }else {
            mTitle="标题";
            mTv_title.setText(mTitle);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.cancel_action){
            if (mCancelListener!=null){
                mCancelListener.onCancel(MGDialog.this);
            }else {
                dismiss();
            }
        }else if (id==R.id.sure_action){
            if (mSureListener!=null){
                mSureListener.onSure(MGDialog.this);
            }
        }
    }

    public MGDialog setOnCancelClickListener(OnCancelClickListener cancelClickListener){
        this.mCancelListener=cancelClickListener;
        return this;
    }
    public MGDialog setOnSureClickListener(OnSureClickListener sureClickListener){
        this.mSureListener=sureClickListener;
        return this;
    }

    public interface OnCancelClickListener{
        void onCancel(MGDialog dialog);
    }

    public interface OnSureClickListener{
        void onSure(MGDialog dialog);
    }
}
