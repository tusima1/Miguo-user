package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.RoundImageView;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.live.views.view.FunnyFragment;

import java.util.List;

/**
 * Created by 有趣页类型显示 on 2016/11/11.
 */
public class FunnyTypeHorizantalScrollView extends BaseHorizantalScrollView{

    FunnyTypeContentLinearLayout content;
    int lastIndex = 0;
    OnFunnyTypeChangeListener onFunnyTypeChangeListener;
    List<ModelHomeClassifyList> mData;

    FunnyFragment funnyFragment;

    public FunnyTypeHorizantalScrollView(Context context) {
        super(context);
        init();
    }

    public FunnyTypeHorizantalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FunnyTypeHorizantalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setHorizontalScrollBarEnabled(false);
        content = new FunnyTypeContentLinearLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(wrapContent(), wrapContent());
        content.setLayoutParams(params);
        content.setBackgroundColor(getColor(R.color.c_F2F2F2));
        addView(content);
    }

    public void init(List<ModelHomeClassifyList> mData){
        content.removeAllViews();
        if(mData == null || mData.size() == 0){
            return;
        }
        this.mData = mData;

        for(int i = 0;i < mData.size(); i++){
            FunnyTypeGroupView group = new FunnyTypeGroupView(getContext());
            LinearLayout.LayoutParams groupParams = getLinearLayoutParams(getImageHeight(), getImageHeight());
            groupParams.setMargins(i == 0 ? dip2px(17) : dip2px(15), dip2px(15), i == mData.size() - 1 ? dip2px(17) : 0, dip2px(15));
            group.setLayoutParams(groupParams);
            content.addView(group);

            RoundImageView roundedImageView = new RoundImageView(getContext());
            roundedImageView.setRectAdius((float)dip2px(8));
            RelativeLayout.LayoutParams imgParams = getRelativeLayoutParams(matchParent(), matchParent());
            roundedImageView.setLayoutParams(imgParams);
            roundedImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            SDViewBinder.setImageView(mData.get(i).getImg(), roundedImageView);
            group.addView(roundedImageView);

            RoundImageView mask = new RoundImageView(getContext());
            mask.setRectAdius((float)dip2px(8));
            RelativeLayout.LayoutParams maskParams = getRelativeLayoutParams(matchParent(), matchParent());
            mask.setLayoutParams(maskParams);
            mask.setBackgroundColor(getColor(i == 0 ? R.color.black_01 : R.color.black_80));
            group.addView(mask);

            TextView title = new TextView(getContext());
            RelativeLayout.LayoutParams titleParmas = getRelativeLayoutParams(wrapContent(), wrapContent());
            titleParmas.addRule(RelativeLayout.CENTER_IN_PARENT);
            title.setTextSize(12);
            title.setTextColor(getColor(R.color.white));
            title.setLayoutParams(titleParmas);
            title.setText(mData.get(i).getName());
            group.addView(title);
            group.setOnClickListener(new FunnyTypeListener(i));
        }
    }

    private int getImageHeight(){
        return (int)((getScreenWidth() - 4 * dip2px(13) - dip2px(17)) / 4.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                handlerActionMove();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handlerActionCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void handlerActionDown(){
        Log.d(tag, "handler down..");
        if(getFunnyFragment() != null){
            getFunnyFragment().setTouchDisableMove(true);
        }
    }

    public void handlerActionMove(){
        Log.d(tag, "handler move..");
        if(getFunnyFragment() != null) {
            Log.d(tag, "handlerActionMove parent disable : " + getFunnyFragment().isTouchDisableMove());
            getFunnyFragment().setTouchDisableMove(true);
        }
    }

    public void handlerActionCancel(){
        Log.d(tag, "handler cancel..");
        if(getFunnyFragment() != null) {
            getFunnyFragment().setTouchDisableMove(false);
            Log.d(tag, "parent disable : " + getFunnyFragment().isTouchDisableMove());

        }
    }

    public FunnyFragment getFunnyFragment() {
        return funnyFragment;
    }

    public void setFunnyFragment(FunnyFragment funnyFragment) {
        this.funnyFragment = funnyFragment;
    }

    class FunnyTypeListener implements View.OnClickListener{

        int position;

        public FunnyTypeListener(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View v) {
            try{

            }catch (Exception e){

            }
            (((ViewGroup)content.getChildAt(lastIndex)).getChildAt(1)).setBackgroundColor(getColor(R.color.black_80));
            lastIndex = position;
            (((ViewGroup)content.getChildAt(lastIndex)).getChildAt(1)).setBackgroundColor(getColor(R.color.black_01));
            if(onFunnyTypeChangeListener != null){
                onFunnyTypeChangeListener.onTypeChanged(mData.get(position));
            }
        }
    }

    public void setOnFunnyTypeChangeListener(OnFunnyTypeChangeListener onFunnyTypeChangeListener) {
        this.onFunnyTypeChangeListener = onFunnyTypeChangeListener;
    }

    public interface OnFunnyTypeChangeListener{
        void onTypeChanged(ModelHomeClassifyList model);
    }

}
