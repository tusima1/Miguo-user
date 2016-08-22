package com.fanwe.user.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.StatusBarCompat;

/**
 * Created by didik on 2016/8/22.
 * 新的含有标题的父Activity
 */
public abstract class MGBaseActivity extends Activity implements View.OnClickListener {
    private int mTitleGravity= Gravity.CENTER;
    private ImageButton ib_left;
    private ImageButton ib_right;
    private TextView tv_middle;

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
        ib_right = (ImageButton) container.findViewById(R.id.ib_right);
        tv_middle = (TextView) container.findViewById(R.id.tv_middle);

        boolean clickable = setMiddleClickable();
        if (clickable){
            tv_middle.setClickable(clickable);
            tv_middle.setOnClickListener(this);
        }

        int resId = setRightImageSrcId();
        if (resId!=0){
            ib_right.setImageResource(resId);
            ib_right.setOnClickListener(this);
        }

        ib_left.setOnClickListener(this);
    }

    protected int setRightImageSrcId(){
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

    protected void setTitleText(CharSequence title){
        tv_middle.setText(title);
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
}
