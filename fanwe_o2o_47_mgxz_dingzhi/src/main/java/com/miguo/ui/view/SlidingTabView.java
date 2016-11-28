package com.miguo.ui.view;

/**
 * Created by Administrator on 2016/11/22.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.utils.BaseUtils;


/**
 * 自定义Tab View
 */
public class SlidingTabView extends LinearLayout {

    private static final float DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0.5f;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;

    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 0; //分割线的宽度 设置为0即不显示，默认为1
    private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;

    private final float bottomBorderThickness;
    private final Paint bottomBorderPaint;

    private final int selectedIndicatorThickness;
    private final Paint selectedIndicatorPaint;

    private final Paint dividerPaint;
    private final float dividerHeight;

    private int selectedPosition;
    private float selectionOffset;
    private String tag = "SlidingTabView";

    private int lineColor = R.color.text_base_color_2; //地下那根长线
    private int dividerColor = R.color.orange_f5; // 跟着滚动的线
    private int selectTextColor = R.color.orange_f5; //选中的字体颜色
    private int normalTextColor = R.color.gray_a6; //默认的字体颜色


    public SlidingTabView(Context context) {
        this(context, null);
    }

    public SlidingTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor =  outValue.data;

        bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        bottomBorderPaint = new Paint();
//        mBottomBorderPaint.setColor(getResources().getColor(R.color.color_line));
        if(lineColor != 0){
            bottomBorderPaint.setColor(ContextCompat.getColor(getContext(), lineColor)); //底下那根线
        }

        selectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        selectedIndicatorPaint = new Paint();
//        mSelectedIndicatorPaint.setColor(0xffff1322);
        if(dividerColor != 0){
            selectedIndicatorPaint.setColor(ContextCompat.getColor(getContext(), dividerColor));  //跟着滚动的Divider
        }
//spot_tab_text_select
        dividerHeight = DEFAULT_DIVIDER_HEIGHT;
        dividerPaint = new Paint();
//        mDividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
//        mDividerPaint.setColor(getResources().getColor(R.color.color_line));
//        mDividerPaint.setColor(getResources().getColor(R.color.spot_tab_text_normal)); //分割线
    }

    void viewPagerChange(int position, float offset){
        this.selectedPosition = position ;
        this.selectionOffset = offset ;
        if (offset == 0){
            for (int i = 0; i < getChildCount(); i++) {
                TextView child = (TextView) getChildAt(i);
//                child.setTextColor(0xff666666);
//                child.setTextColor(getResources().getColor(R.color.spot_tab_text_select));
                child.setTextColor(ContextCompat.getColor(getContext(), normalTextColor)); //设置所有tab的颜色为默认颜色
            }
            TextView selectedTitle = (TextView) getChildAt(selectedPosition);
//            selectedTitle.setTextColor(0xffff1322);
            selectedTitle.setTextColor(ContextCompat.getColor(getContext(), selectTextColor)); //当前选择tab的字体颜色
        }
        invalidate();
    }


    int textLength = 4;
    int right = 0;
    TextView title = null;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, dividerHeight), 1f) * height);

        if (childCount > 0) {
            TextView selectedTitle = (TextView) getChildAt(selectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
//            selectedTitle.setTextColor(blendColors(0xff666666,0xffff1322,mSelectionOffset));
            selectedTitle.setTextColor(blendColors(ContextCompat.getColor(getContext(), normalTextColor),ContextCompat.getColor(getContext() , selectTextColor),selectionOffset));

            if (selectionOffset > 0f && selectedPosition < (getChildCount() - 1)) {

                TextView nextTitle = (TextView) getChildAt(selectedPosition + 1);
                left = (int) (selectionOffset * nextTitle.getLeft() +
                        (1.0f - selectionOffset) * left);
                right = (int) (selectionOffset * nextTitle.getRight() +
                        (1.0f - selectionOffset) * right);
//                nextTitle.setTextColor(blendColors(0xffff1322,0xff666666,mSelectionOffset));
                nextTitle.setTextColor(blendColors(ContextCompat.getColor(getContext(), selectTextColor),ContextCompat.getColor(getContext() , normalTextColor),selectionOffset));

                if(this.right != right){
                    if(this.right < right){
                        title = nextTitle;
                    }else {
                        title = selectedTitle;
                    }
                    this.right = right;
                }


                textLength = title.getText().toString().length() != 0 ? title.getText().toString().length() : textLength;
                textLength = textLength > 4 ? 4 : textLength;
                Log.d(tag, "textview width: " + title.getText().toString() + " ,length: " + title.getText().toString().length());
            }


            int tabWidth = getTabWidth(6.5) / 4; //四个字符的长度
            tabWidth = tabWidth * textLength;
            int nowWidth = Math.abs(right - left);
            int cut = nowWidth - tabWidth;
            Log.d(tag, "tabWidth: " + tabWidth + " ,cut: " + cut + " ,left + cut : " + left + " ,right - cut: " + right);
            if(cut > 0){
                canvas.drawRect(left + cut / 2, height - selectedIndicatorThickness + 3, right - cut / 2,height, selectedIndicatorPaint);
            }else {
//        	  canvas.drawRect(left, height - selectedIndicatorThickness, right,height, selectedIndicatorPaint);
            }

        }

        /**
         * 画底下的线
         */
//        canvas.drawRect(0, height - bottomBorderThickness, getWidth(), height, bottomBorderPaint);

        int separatorTop = (height - dividerHeightPx) / 2;

        /**
         * 绘制分割线
         */
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
//            canvas.drawLine(child.getRight(), separatorTop, child.getRight(),separatorTop + dividerHeightPx, mDividerPaint);
        }
    }

    private int getTabWidth(double num){
        int screenWidth = BaseUtils.getWidth(getContext());
        int width = (int) (screenWidth / num);
        return width;
    }


    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public int getSelectTextColor() {
        return selectTextColor;
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }
}
