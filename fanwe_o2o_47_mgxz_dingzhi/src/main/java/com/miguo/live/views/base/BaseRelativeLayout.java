package com.miguo.live.views.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miguo.live.views.utils.BaseUtils;

import java.util.List;


/**
 * Created by zlh on 16/7/15.
 */
public abstract class BaseRelativeLayout extends RelativeLayout{

    List datas;
    protected String tag = getClass().getSimpleName();

    public BaseRelativeLayout(Context context) {
        super(context);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected abstract void init();

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

    public LayoutParams getRelativeLayoutParams(int width, int height){
        return new LayoutParams(width, height);
    }

    public int wrapContent(){
        return LayoutParams.WRAP_CONTENT;
    }

    public int matchParent(){
        return LayoutParams.MATCH_PARENT;
    }

    public LinearLayout.LayoutParams getLinearLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    public Object getItem(int position){
        return (null != datas && datas.size() > 0) ? datas.get(position) : null;
    }

    public Resources getResources(){
        return getContext().getResources();
    }


    public int getCount(){
        return (null != datas && datas.size() > 0) ? datas.size() : 0;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }
}
