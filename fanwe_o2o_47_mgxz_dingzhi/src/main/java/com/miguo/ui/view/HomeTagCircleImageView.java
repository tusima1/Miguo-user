package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by zlh/狗蛋哥/Barry on 2016/11/2.
 */
public class HomeTagCircleImageView extends CircleImageView{

    HomeTagsView homeTagsView;

    public HomeTagCircleImageView(Context context) {
        super(context);
    }

    public HomeTagCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeTagCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        if(getHomeTagsView() != null){
            getHomeTagsView().handlerActionDown();
        }
    }

    public void handlerActionMove(){
        if(getHomeTagsView() != null){
            getHomeTagsView().handlerActionMove();
        }
    }

    public void handlerActionCancel(){
        if(getHomeTagsView() != null){
            getHomeTagsView().handlerActionCancel();
        }
    }

    public HomeTagsView getHomeTagsView() {
        return homeTagsView;
    }

    public void setHomeTagsView(HomeTagsView homeTagsView) {
        this.homeTagsView = homeTagsView;
    }
}
