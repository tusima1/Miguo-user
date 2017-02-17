package com.miguo.ui.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;


/**
 * Created by didik on 2016/8/23.
 * imageView with text
 *
 * note: text length must be less!!! text width must be smaller than View's width.
 * note: no src,you should give a specified dimension
 */
public class ImageTextView extends ImageView {

    private Context mContext;
    private Paint mPaint;

    private String mText="";
    private boolean mBold =false;
    private int mGravity= CENTER;
    private int mColor= Color.BLACK;

    public static final int LEFT=0;
    public static final int CENTER=1;
    public static final int RIGHT=2;
    private float mTextSize=14;

    public ImageTextView(Context context) {
        this(context,null);
    }



    public ImageTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        initImageTextView(context,attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageTextView(context,attrs);
    }

    private void initImageTextView(Context context, AttributeSet attrs) {
        this.mContext=context;
        mPaint=new Paint();
        getAttrs(attrs);
        this.setClickable(true);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        float textSize = typedArray.getDimension(R.styleable.ImageTextView_textSize, 14);
        mTextSize=DisplayUtil.px2sp(mContext,textSize);
        mText=typedArray.getString(R.styleable.ImageTextView_text);
        mBold=typedArray.getBoolean(R.styleable.ImageTextView_textBold,false);
        mGravity=typedArray.getInt(R.styleable.ImageTextView_textGravity,1);
        mColor=typedArray.getColor(R.styleable.ImageTextView_textColor,Color.BLACK);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int textSize = DisplayUtil.sp2px(mContext, mTextSize);
        mPaint.setTextSize(textSize);

        if (mGravity==LEFT){
            mPaint.setTextAlign(Paint.Align.LEFT);
        }else if (mGravity==CENTER){
            mPaint.setTextAlign(Paint.Align.CENTER);
        }else if (mGravity==RIGHT){
            mPaint.setTextAlign(Paint.Align.RIGHT);
        }

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        float baseline = (measuredHeight - fontMetrics.bottom - fontMetrics.top) / 2;
        //is bold
        mPaint.setFakeBoldText(mBold);
        if (mGravity==LEFT){
            canvas.drawText(mText, 0, baseline, mPaint);
        }else if (mGravity==CENTER){
            canvas.drawText(mText, measuredWidth/2, baseline, mPaint);
        }else if (mGravity==RIGHT){
            canvas.drawText(mText, measuredWidth, baseline, mPaint);
        }

    }

    /**
     * set text
     * @param txt view Content
     */
    public void setText(String txt){
        mText=txt;
    }

    /**
     * 设置是否加粗
     * @param isBold false为normal
     */
    public void setTextBold(boolean isBold){
        mBold=isBold;
    }

    /**
     * 设置文字颜色
     * @param color integer
     */
    public void setTextColor(int color){
        mColor=color;
    }

    /**
     * 设置字体大小
     * @param sp 尺寸
     */
    public void setTextSize(float sp){
        mTextSize=sp;
    }

    /**
     * 设置文字显示的位置
     * @param gravity 只支持左中右(0,1,2)
     */
    public void setGravity(int gravity){
        if (gravity!=LEFT && gravity!=RIGHT){
            mGravity=CENTER;
        }else {
            mGravity=gravity;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode==MeasureSpec.AT_MOST && !TextUtils.isEmpty(mText)){
            //width
//            setMeasuredDimension(measureText(mText),);

        }else if (heightMode==MeasureSpec.AT_MOST && !TextUtils.isEmpty(mText)){
            //height

        }else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }

    }

    private float measureText(String txt){
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mTextSize);
        float ft = textPaint.measureText(txt);
        return ft;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int width  = measureDimension(DEFAULT_VIEW_WIDTH, widthMeasureSpec);
//        int height = measureDimension(DEFAULT_VIEW_HEIGHT, heightMeasureSpec);
//
//        setMeasuredDimension(width, height);
//    }
//
//    protected int measureDimension( int defaultSize, int measureSpec ) {
//
//        int result = defaultSize;
//
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//
//        //1. layout给出了确定的值，比如：100dp
//        //2. layout使用的是match_parent，但父控件的size已经可以确定了，比如设置的是具体的值或者match_parent
//        if (specMode == MeasureSpec.EXACTLY) {
//            result = specSize; //建议：result直接使用确定值
//        }
//        //1. layout使用的是wrap_content
//        //2. layout使用的是match_parent,但父控件使用的是确定的值或者wrap_content
//        else if (specMode == MeasureSpec.AT_MOST) {
//            result = Math.min(defaultSize, specSize); //建议：result不能大于specSize
//        }
//        //UNSPECIFIED,没有任何限制，所以可以设置任何大小
//        //多半出现在自定义的父控件的情况下，期望由自控件自行决定大小
//        else {
//            result = defaultSize;
//        }
//
//        return result;
//    }

    protected int mImageResId=0;
    @Override
    public void setImageResource(int resId) {
        this.mImageResId=resId;
        super.setImageResource(resId);
    }
}
