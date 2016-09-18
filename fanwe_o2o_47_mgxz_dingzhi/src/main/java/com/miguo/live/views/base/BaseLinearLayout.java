package com.miguo.live.views.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.utils.BaseUtils;

import java.util.List;


/**
 * Created by mac on 16/7/15.
 */
public abstract class BaseLinearLayout extends LinearLayout{

    protected List datas;
    protected String tag = this.getClass().getSimpleName();

    public BaseLinearLayout(Context context) {
        super(context);
        init();
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public RelativeLayout.LayoutParams getRelativeLayoutParams(int width, int height){
        return new RelativeLayout.LayoutParams(width, height);
    }

    public LayoutParams getLinearLayoutParams(int width, int height){
        return new LayoutParams(width, height);
    }

    public Resources getResources(){
        return getContext().getResources();
    }

    public int matchParent(){
        return LayoutParams.MATCH_PARENT;
    }

    public int wrapContent(){
        return LayoutParams.WRAP_CONTENT;
    }


    public Object getItem(int position){
        return (null != datas && datas.size() > 0) ? datas.get(position) : null;
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
