package com.miguo.ui.view.customviews;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by didik 
 * Created time 2017/2/13
 * Description: 
 */

public class ArcDrawable extends Drawable {
    private final Paint mPaint;
    private int mColor = Color.BLACK;
    public ArcDrawable() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mColor = Color.parseColor("#D23838");
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();

        final int w = bounds.right - bounds.left;
        final int h = bounds.bottom - bounds.top;
        float r = calculateR(w, h);

//        float offset = (float) (r - w * 1.0 /2);
//        float left = -offset;
//        float right = w + offset;
//        float top = 0;
//        float bottom = 2*r;
        float offset = (float) (r - w * 1.0 /2);
        float left = -offset;
        float right = w + offset;
        float top = h - 2* r;
        float bottom = h;
//        Log.e("test", "left: " + left);
//        Log.e("test", "right: " + right);
//        Log.e("test", "top: " + top);
//        Log.e("test", "bottom: " + bottom);

        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(left,top,right,bottom,0,180,false,mPaint);//(270 - d/2) - 0.5
    }

    private float calculateR(int w, int h) {
        return (float) (((4 * h *h + w * w)*1.0)/(8*h*1.0));
    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
