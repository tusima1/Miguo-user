package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.miguo.live.views.base.BaseLinearLayout;

/**
 * Created by Administrator on 2016/11/14.
 */

public class FunnyTypeContentLinearLayout extends BaseLinearLayout{

    public FunnyTypeContentLinearLayout(Context context) {
        super(context);
    }

    public FunnyTypeContentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FunnyTypeContentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        }
        return super.onTouchEvent(event);
    }

    private void handlerActionDown(){
        if(getFunnyTypeHorizantalScrollView() != null){
            getFunnyTypeHorizantalScrollView().handlerActionDown();
        }
    }

    public FunnyTypeHorizantalScrollView getFunnyTypeHorizantalScrollView(){
        try{
            return (FunnyTypeHorizantalScrollView)getParent();
        }catch (Exception e){
            return null;
        }
    }

}
