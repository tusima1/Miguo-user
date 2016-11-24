package com.miguo.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HomeBannerImageView extends ImageView{

    String tag = "HomeBannerImageView";

    public HomeBannerImageView(Context context) {
        super(context);
    }

    public HomeBannerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeBannerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(tag, "parent is viewpager: " +  (getParent() instanceof ViewPager));
        Log.d(tag, "parent's parent is viewpager: " +  (getParent().getParent() instanceof ViewPager));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getHomeViewPager().handlerActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                getHomeViewPager().handlerActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getHomeViewPager().handlerActionCancel(event);
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 当前ImageView是被LineaLayout包裹
     * LineaLayout是被ViewPager包裹
     * 所以获取ViewPager只需要获取两次parent就可以了
     * @return
     */
    public HomeBannerViewPager getHomeViewPager(){
        return (HomeBannerViewPager)getParent().getParent();
    }

}
