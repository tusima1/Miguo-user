package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.miguo.live.views.base.BaseRelativeLayout;

/**
 * Created by Administrator on 2016/11/14.
 */

public class FunnyTypeGroupView extends BaseRelativeLayout{

    public FunnyTypeGroupView(Context context) {
        super(context);
    }

    public FunnyTypeGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FunnyTypeGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                handlerActionMove();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handlerActionCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void handlerActionDown(){
        if(getFunnyTypeHorizontalScrollView() != null){
            getFunnyTypeHorizontalScrollView().handlerActionDown();
        }
    }

    public void handlerActionMove(){
        if(getFunnyTypeHorizontalScrollView() != null){
            getFunnyTypeHorizontalScrollView().handlerActionMove();
        }
    }

    public void handlerActionCancel(){
        if(getFunnyTypeHorizontalScrollView() != null){
            getFunnyTypeHorizontalScrollView().handlerActionCancel();
        }
    }

    public FunnyTypeHorizantalScrollView getFunnyTypeHorizontalScrollView(){
        try{
            return (FunnyTypeHorizantalScrollView) getParent().getParent();
        }catch (Exception e){
            return null;
        }
    }

}
