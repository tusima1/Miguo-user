package com.fanwe.user.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.customviews.ImageTextView;
import com.miguo.utils.StatusBarCompat;

/**
 * Created by didik on 2016/8/22.
 * 新的含有标题的父Activity
 */
public abstract class MGBaseActivity extends Activity implements View.OnClickListener {
    private int mTitleGravity= Gravity.CENTER;
    private ImageButton ib_left;
    private ImageTextView ib_right;
    private TextView tv_middle;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout container = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.act_mg_base, null);
        initTitle(container);
        int resId = addContentView();
        View contentView = LayoutInflater.from(this).inflate(resId, null);
        container.addView(contentView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(container);
//        StatusBarUtil.setTransparent(this);
        StatusBarCompat.setStatusBarColor(this, Color.WHITE,38);
        init();
    }

    /**
     * onCreate方法的入口
     */
    protected abstract void init();

    protected void initTitle(LinearLayout container){
        ib_left = (ImageButton) container.findViewById(R.id.ib_left);
        ib_right = (ImageTextView) container.findViewById(R.id.ib_right);
        tv_middle = (TextView) container.findViewById(R.id.tv_middle);

        //设置标题
        CharSequence charSequence = setTitleText();

        tv_middle.setText(charSequence);
        boolean clickable = setMiddleClickable();
        if (clickable){
            tv_middle.setClickable(clickable);
            tv_middle.setOnClickListener(this);
        }

        Object resId = setRightImageSrcId();
        if (resId instanceof Integer){
            int newResid= (int) resId;
            if (newResid!=0){
                ib_right.setImageResource(newResid);
                ib_right.setOnClickListener(this);
            }
        }
        if (resId instanceof String){
            String txt= (String) resId;
            if (!TextUtils.isEmpty(txt)){
                ib_right.setText(txt);
                ib_right.setTextSize(16);
                ib_right.setOnClickListener(this);
            }else {
                ib_right.setText(txt);
                ib_right.setTextSize(16);
            }
        }

        ib_left.setOnClickListener(this);
    }

    protected Object setRightImageSrcId(){
        return 0;
    }


    /**
     *
     * @return  boolean 设置是否可以点击,只有可点击才会有事件回调
     */
    protected boolean setMiddleClickable(){
        return false;
    }

    /**
     * 设置中间字体的位置
     * @param gravity
     */
    protected void setTitleGravity(int gravity){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity=gravity;
        tv_middle.setLayoutParams(layoutParams);
    }

    protected CharSequence setTitleText(){
        return title;
    }


    protected void setTitleTextStyle(){
        //wo...
    }

    /**
     * 设置内容布局
     * @return
     */
    protected abstract int addContentView();

    @Override
    public void onClick(View v) {
        if (v==ib_left){
            onLeftImageClick(v);
        }else if (v==ib_right){
            onRightImageClick(v);
        }else if (v==tv_middle){
            onMiddleTextClick(v);
        }
    }

    protected  void onLeftImageClick(View v){
        onBackPressed();
    }
    protected  void onMiddleTextClick(View v){}
    protected  void onRightImageClick(View v){}



    public void setTitle(String title) {
        this.title = title;
    }
}
