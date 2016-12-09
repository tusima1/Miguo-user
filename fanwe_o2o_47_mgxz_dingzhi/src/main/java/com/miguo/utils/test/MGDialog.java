package com.miguo.utils.test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.DisplayUtil;

/**
 * Created by didik on 2016/9/12.
 */
public class MGDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private RelativeLayout dialog_layout;
    private TextView mTv_title;
    private TextView mTv_Content;

    private Button mBt_cancel;
    private Button mBt_sure;
    private Drawable sureBtndrawable;
    private Drawable cancelBtndrawable;
    private String mTitle;
    private int mTitleDrawableId;
    private String mSureText;
    private String mCancelText;
    private Object mTitleObj;
    private String mContentText;
    private OnCancelClickListener mCancelListener;
    private OnSureClickListener mSureListener;
    /**
     *  没有title时候的高度。
     */
    private int WITHOUTTITLEHEIGHT=282;
    /**
     * dialog高度。
     */
    private int height=280;
    /**
     * 是否显示标题，默认显示。
     */
    private boolean showTitle = true;

    public MGDialog(Context context) {
        super(context,R.style.floag_dialog);
        this.mContext=context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_normal_v2);
        dialog_layout = (RelativeLayout)findViewById(R.id.dialog_layout) ;
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_Content = (TextView) findViewById(R.id.tv_content);
        mBt_cancel = (Button) findViewById(R.id.cancel_action);
        mBt_sure = (Button) findViewById(R.id.sure_action);

        mBt_cancel.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        setCancelButtonDrawable(cancelBtndrawable);
        setSureButtonDrawable(sureBtndrawable);
        setCancelActionText(mCancelText);
        setSureActionText(mSureText);
        setContentText(mContentText);
        setMGTitle(mTitleObj);
        setDialogHeight(height);
    }

    public MGDialog setDialogHeight(int height){
        if(height>0){
            this.height =height;
        }
        if(dialog_layout==null){
            return this;
        }
        if(!showTitle){
            height = this.WITHOUTTITLEHEIGHT;
        }
        android.view.ViewGroup.LayoutParams pp =dialog_layout.getLayoutParams();
         pp.height = DisplayUtil.dp2px(mContext,(float)height);
         dialog_layout.setLayoutParams(pp);
        return this;
    }
    public void showTitle(boolean show){
        if(!show){
            mTv_title.setVisibility(View.GONE);
        }
    }

    /**
     * 设置取消按扭背景色。
     * @param drawable
     * @return
     */
    public MGDialog setCancelButtonDrawable(Drawable drawable){
        this.cancelBtndrawable = drawable;
        if(mBt_cancel!=null ) {
            mBt_cancel.setBackground(drawable);
        }
        return this;
    }

    /**
     * 设置确认按钮背景色。
     * @param drawable
     * @return
     */
    public MGDialog setSureButtonDrawable(Drawable drawable){
        this.sureBtndrawable = drawable;
        if(mBt_sure!=null ) {
            mBt_sure.setBackground(drawable);
        }
        return this;
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
            mTv_title.setVisibility(View.GONE);
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
