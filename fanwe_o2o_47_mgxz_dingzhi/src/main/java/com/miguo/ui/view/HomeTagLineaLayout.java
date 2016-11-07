package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.miguo.live.views.base.BaseLinearLayout;

/**
 * Created by 首页 on 2016/11/2.
 */
public class HomeTagLineaLayout extends BaseLinearLayout{

    HomeTagsView homeTagsView;

    public HomeTagLineaLayout(Context context) {
        super(context);
    }

    public HomeTagLineaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeTagLineaLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
//            case MotionEvent.ACTION_MOVE:
//                handlerActionMove();
//                break;
        }
        return super.onTouchEvent(event);
    }

    private void handlerActionDown(){
        if(getHomeTagsView() != null){
            getHomeTagsView().handlerActionDown();
        }
    }
    private void handlerActionMove(){
        if(getHomeTagsView() != null){
            getHomeTagsView().handlerActionMove();
        }
    }

    public HomeTagsView getHomeTagsView() {
        return homeTagsView;
    }

    public void setHomeTagsView(HomeTagsView homeTagsView) {
        this.homeTagsView = homeTagsView;
    }
}
