package com.fanwe.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jirentianxiang on 2/29/16.
 */
public class BadgeView extends View {
    int count=0;

    private void setup(Context context){
    }

    public BadgeView(Context context){
        super(context);
        setup(context);
    }

    public BadgeView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        setup(context);
    }

    public void setCount(int count){
        this.count=count;
        invalidate();
    }

    public int getCount(){
        return count;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (count<=0) return;
        int width=getWidth();
        int height=getHeight();
        float radius=Math.min(width, height)/2f;
        float centerX=width/2f;
        float centerY=height/2f;
        float left=centerX-radius;
        float right=centerX+radius;
        float top=centerY-radius;
        float bottom=centerY+radius;
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawOval(new RectF(left, top, right, bottom), paint);
    }
}
