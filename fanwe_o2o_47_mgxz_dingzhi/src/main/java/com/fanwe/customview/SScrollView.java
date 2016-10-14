package com.fanwe.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by didik on 2016/10/13.
 */

public class SScrollView extends ScrollView {
    public SScrollView(Context context) {
        super(context);
    }

    public SScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnScrollChangedListener{
        public void onScrollChanged(int x, int y, int oldX, int oldY);
    }
    private OnScrollChangedListener onScrollChangedListener;
    /**
     *
     * @param onScrollChangedListener
     */
    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener){
        this.onScrollChangedListener=onScrollChangedListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY){
        super.onScrollChanged(x, y, oldX, oldY);
        if(onScrollChangedListener!=null){
            onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
        }
    }

    /******************************* is bottom or top *****************************/

    /**
     *
     * @return
     */
    public boolean isAtTop(){
        return getScrollY()<=0;
    }
    /**
     *
     * @return
     */
    public boolean isAtBottom(){
        return getScrollY()==getChildAt(getChildCount()-1).getBottom()+getPaddingBottom()-getHeight();
    }

    /******************************* isChildVisible ************************************/
    /**
     *
     * @param child
     * @return
     */
    public boolean isChildVisible(View child){
        if(child==null){
            return false;
        }
        Rect scrollBounds = new Rect();
        getHitRect(scrollBounds);
        return child.getLocalVisibleRect(scrollBounds);
    }
}
