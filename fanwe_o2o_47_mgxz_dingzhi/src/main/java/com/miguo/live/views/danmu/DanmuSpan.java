package com.miguo.live.views.danmu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by zlh on 2016/9/12.
 * 弹幕图文魂牌span
 */
public class DanmuSpan extends ImageSpan{

    String TAG = DanmuSpan.class.getSimpleName();
    WeakReference<Drawable> wr;

    public DanmuSpan(Drawable d) {
        super(d);
        wr = new WeakReference<>(d);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
            // keep it the same as paint's fm
            fm.ascent = pfm.ascent;
            fm.descent = pfm.descent;
            fm.top = pfm.top;
            fm.bottom = pfm.bottom;
        }

        return rect.right;
//        return super.getSize(paint, text, start, end, fm);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();
        int transY;
        transY = b.getIntrinsicHeight() / 2 * -1;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = this.wr;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
        }

        return d;
    }

    public void destory(){
        Log.d(TAG, "wr is null? " + (wr == null));
        if(wr != null){
            DanmuDrawable d = (DanmuDrawable)wr.get();
            Log.d(TAG, "d is null? " + (d == null));
            if(d != null){
                d.destory();
            }
            d = null;
            wr.clear();
            wr = null;
        }
    }

}
