package com.miguo.ui.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

/**
 * Created by zlh on 2017/1/11.
 */

public class RepresentAppBarLayout extends AppBarLayout implements AppBarLayout.OnOffsetChangedListener {

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

    public boolean canRefresh(){
        return getVerticalOffset() == 0;
    }

}
