package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik 
 * Created time 2017/1/18
 * Description: 
 */

public class TitleView extends RelativeLayout {

    protected ImageView mLeftImageView;
    protected TextView mCenterTitleText;
    protected FrameLayout mRightFlContainer;

    public TitleView(Context context) {
        this(context,null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_title_new,this);
        mLeftImageView = (ImageView) findViewById(R.id.left);
        mRightFlContainer = (FrameLayout) findViewById(R.id.right);
        mCenterTitleText = (TextView) findViewById(R.id.tv_title);
    }

}
