package com.fanwe.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/11/25.
 */

public class MGGradientView extends View {
    public static final int TOP= 0;
    public static final int BOTTOM= 1;
    private int mGravity=TOP;//default is top
    private int mAlpha=160;//0~255

    public MGGradientView(Context context) {
        this(context,null);
    }

    public MGGradientView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MGGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MGGradientView);
        mAlpha=ta.getInteger(R.styleable.MGGradientView_gradient_alpha,160);
        mGravity = ta.getInt(R.styleable.MGGradientView_gradient_gravity, TOP);
        ta.recycle();
        startFlow();
    }

    private void setGradientBackground(){
        int bgResId= mGravity ==TOP ? R.drawable.shape_gradient_up :R.drawable.shape_gradient_down;
        this.setBackgroundResource(bgResId);
    }

    public void setGravity(int gravity){
        mGravity = gravity == BOTTOM ? BOTTOM :TOP;
        setGradientBackground();
    }

    public void setGradientAlpha(int alpha){
        if (alpha>255 || alpha <0)return;
        mAlpha=alpha;
        float innerAlpha = (float) (mAlpha * 1.0 / 255);
        this.setAlpha(innerAlpha);
    }

    private void startFlow() {
        Log.e("test","TOP:"+TOP +" BOTTOM: "+BOTTOM);
        setGradientAlpha(mAlpha);
        setGradientBackground();
    }

}
