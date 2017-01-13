package com.miguo.ui.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zlh on 2017/1/11.
 */

public class RepresentAppBarLayout extends AppBarLayout implements AppBarLayout.OnOffsetChangedListener {

    protected String tag = "RepresentAppBarLayout";
    int verticalOffset;

    public RepresentAppBarLayout(Context context) {
        super(context);
        init();
    }

    public RepresentAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        setVerticalOffset(verticalOffset);
    }

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.d(tag, "move y: " + event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean canRefresh(){
        return getVerticalOffset() == 0;
    }

}
