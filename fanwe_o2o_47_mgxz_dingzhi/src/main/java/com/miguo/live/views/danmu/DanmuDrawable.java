package com.miguo.live.views.danmu;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by zlh on 2016/9/12.
 * 自定义弹幕内容Drawable
 */
public class DanmuDrawable extends Drawable{

    Bitmap avatartBitmap;
    Bitmap roundBitmap;
    Paint paint;
    int radius;
    int borderWidth;

    public DanmuDrawable(Bitmap avatartBitmap) {
        this.avatartBitmap = avatartBitmap;
        initPaint();

    }

    private void initPaint(){
        setRadius(6);
        setBorderWidth(6);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(getBitmapShader());
        paint.setStrokeWidth(radius);
    }

    private BitmapShader getBitmapShader(){
        return new BitmapShader(avatartBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        /**
         * 防止边缘的锯齿
         */
        paint.setAntiAlias(true);
        /**
         * 画笔颜色
         */
        paint.setColor(Color.WHITE);
        /**
         * 圆形ImageView的半径为布局中的ImageView定义大小
         */
        roundBitmap = getCroppedBitmap(avatartBitmap, getBitmapWidth());

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(getBitmapWidth() / 2, getBitmapHeight() / 2, getBitmapWidth() / 2 , paint);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        //第三个参数减去的数值为白边的宽度.
        canvas.drawCircle(sbmp.getWidth() / 2 ,sbmp.getHeight() / 2 , sbmp.getWidth() / 2 - getBorderWidth(), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    @Override
    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public int getBitmapWidth(){
        return avatartBitmap.getWidth();
    }

    public int getBitmapHeight(){
        return avatartBitmap.getHeight();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return getBitmapWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return getBitmapHeight();
    }

    @Override
    public void setColorFilter(int color, PorterDuff.Mode mode) {
        super.setColorFilter(color, mode);
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void destory(){
        if(avatartBitmap != null && !avatartBitmap.isRecycled()){
            avatartBitmap.recycle();
            avatartBitmap = null;
        }
        if(roundBitmap != null && !roundBitmap.isRecycled()){
            roundBitmap.recycle();
            roundBitmap = null;
        }
        if(paint != null){
            paint = null;
        }
    }
}
