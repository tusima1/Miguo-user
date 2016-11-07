package com.fanwe.view;
 
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 圆角ImageView
 * 
 */
public class RoundImageView extends ImageView {
	
	 
    private final RectF roundRect = new RectF();
    private float rectAdius = 6;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    RoundImageViewOnTouchListener roundImageViewOnTouchListener;
 
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public RoundImageView(Context context) {
        super(context);
        init();
    }
 
    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        
        float density = getResources().getDisplayMetrics().density;
        rectAdius = rectAdius * density;
    }
 
    public void setRectAdius(float adius) {
        rectAdius = adius;
        invalidate();
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int height = getHeight();
        roundRect.set(0, 0, width, height);
    }
 
    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rectAdius, rectAdius, zonePaint);
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handlerActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handlerActionCancel(event);
                    break;
        }
        return super.onTouchEvent(event);
    }

    private void handlerActionDown(MotionEvent event){
        if(getRoundImageViewOnTouchListener() != null){
            getRoundImageViewOnTouchListener().onActionDown(event);
        }
    }

    private void handlerActionMove(MotionEvent event){
        if(getRoundImageViewOnTouchListener() != null){
            getRoundImageViewOnTouchListener().onActionMove(event);

        }
    }

    private void handlerActionCancel(MotionEvent event){
        if(getRoundImageViewOnTouchListener() != null){
            getRoundImageViewOnTouchListener().onActionCancel(event);
        }
    }

    public interface RoundImageViewOnTouchListener{
        void onActionDown(MotionEvent ev);
        void onActionMove(MotionEvent ev);
        void onActionCancel(MotionEvent ev);
    }

    public RoundImageViewOnTouchListener getRoundImageViewOnTouchListener() {
        return roundImageViewOnTouchListener;
    }

    public void setRoundImageViewOnTouchListener(RoundImageViewOnTouchListener roundImageViewOnTouchListener) {
        this.roundImageViewOnTouchListener = roundImageViewOnTouchListener;
    }
}