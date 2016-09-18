package com.miguo.live.views.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.utils.BaseUtils;


/**
 * Created by mac on 16/7/18.
 */
public class BaseHorizantalScrollView extends HorizontalScrollView{


    protected String tag = getClass().getSimpleName();

    public BaseHorizantalScrollView(Context context) {
        super(context);
    }

    public BaseHorizantalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseHorizantalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int dip2px(int dp){
        return BaseUtils.dip2px(getContext(), dp);
    }

    public int px2dip(int px){
        return BaseUtils.px2dip(getContext(), px);
    }

    public Drawable getDrawable(int resId){
        return getResources().getDrawable(resId);
    }

    public int getColor(int resId){
        return getResources().getColor(resId);
    }

    public String getString(int resId){
        return getResources().getString(resId);
    }

    public int getDimensionPixelSize(int resId){
        return getResources().getDimensionPixelSize(resId);
    }

    public int getScreenWidth(){
        return BaseUtils.getWidth(getContext());
    }

    public int geScreenHeight(){
        return BaseUtils.getHeight(getContext());
    }

    public RelativeLayout.LayoutParams getRelativeLayoutParams(int width, int height){
        return new RelativeLayout.LayoutParams(width, height);
    }

    public LinearLayout.LayoutParams getLinearLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    public Resources getResources(){
        return getContext().getResources();
    }

}
