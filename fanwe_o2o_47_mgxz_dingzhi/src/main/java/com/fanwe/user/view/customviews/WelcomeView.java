package com.fanwe.user.view.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/9/29.
 */

public class WelcomeView extends FrameLayout {
    private Context mContext;
    private ImageView mIvImage;
    private ImageView mIvNext;
    private ImageView mIvSkip;
    private OnNextClickListener mOnNextClickListener;//最后页面的点击进入
    private OnSkipClickListener mOnSkipClickListener;//每个页面的

    public WelcomeView(Context context) {
        this(context, null);
    }

    public WelcomeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WelcomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }



    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.item_welcome, this);
        mIvImage = ((ImageView) findViewById(R.id.iv_img));
        mIvNext = ((ImageView) findViewById(R.id.tv_next));
//        mIvSkip = ((ImageView) findViewById(R.id.tv_skip));

        mIvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNextClickListener!=null){
                    mOnNextClickListener.onClick(v);
                }
            }
        });

//        mIvSkip.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnSkipClickListener!=null){
//                    mOnSkipClickListener.onClick(v);
//                }
//            }
//        });
    }

    public void setImageRes(int imageRes){
        if (imageRes!=0) {
            mIvImage.setImageResource(imageRes);
        }
    }

    public void setImageUrl(String url){
        SDViewBinder.setImageView(url,mIvImage);
    }

    public void setNextVisiable(){
        mIvNext.setVisibility(VISIBLE);
    }

    public interface OnNextClickListener{
        public void onClick(View v);
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener){
        this.mOnNextClickListener=onNextClickListener;
    }

    public interface OnSkipClickListener{
        public void onClick(View v);
    }

    public void setOnSkipClickListener(OnSkipClickListener onSkipClickListener){
        this.mOnSkipClickListener=onSkipClickListener;
    }

}
