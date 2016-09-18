package com.miguo.live.views.gift;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.miguo.live.views.base.BaseLinearLayout;

/**
 * Created by zlh on 2016/9/18.
 */
public class SmallGifView extends BaseLinearLayout{

    public SmallGifView(Context context) {
        super(context);
    }

    public SmallGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setOrientation(VERTICAL);
    }


}
