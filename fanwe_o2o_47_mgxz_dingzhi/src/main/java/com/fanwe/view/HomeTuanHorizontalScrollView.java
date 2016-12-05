package com.fanwe.view;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.utils.DisplayUtil;

import java.util.List;

/**
 * Created by Barry/狗蛋哥 on 2016/10/10.
 */
public class HomeTuanHorizontalScrollView extends BaseHorizantalScrollView implements RoundImageView.RoundImageViewOnTouchListener{

    LinearLayout content;
    List<SpecialListModel.Result.Body> datas;
    OnTimeLimitClickListener onTimeLimitClickListener;
    HomeTuanTimeLimitView homeTuanTimeLimitView;

    HomeTuanHorizontalScrollViewOnTouchListener homeTuanHorizontalScrollViewOnTouchListener;

    public HomeTuanHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public HomeTuanHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeTuanHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.d(tag, " action down");
                handlerActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(tag, " action move");
                handlerActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(homeTuanTimeLimitView != null){
                    homeTuanTimeLimitView.onTouchEvent(ev);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void init(){
        setHorizontalScrollBarEnabled(false);
    }

    public void init(List<SpecialListModel.Result.Body> datas){
        if(datas == null || datas.size() <= 0){
            return;
        }
        this.datas = datas;
        removeAllViews();
        initContent();

        int screenWidth = getScreenWidth();
        int margionSpaceFirst = dip2px(17);
        int margionSpace = dip2px(7);
        int width = (int)((screenWidth - margionSpace * 3) / (float)2.5);
        int height = width * 2 / 3;
        for(int i = 0; i< datas.size(); i++){
            RoundImageView img = new RoundImageView(getContext());
            LinearLayout.LayoutParams imgParams = getLinearLayoutParams(width, height);
            imgParams.setMargins(i == 0 ? margionSpaceFirst : margionSpace, 0, i == datas.size() - 1 ? margionSpaceFirst : 0, 0);
            img.setLayoutParams(imgParams);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url=getImagePath(i);
            if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
                url = DisplayUtil.qiniuUrlExchange(url,150,100);
            }
            SDViewBinder.setImageView(url, img);
            img.setBackgroundColor(getColor(R.color.gray_ee));
            img.setRectAdius((float)dip2px(5));
            img.setOnClickListener(new HomeTuanHorizontalScrollViewListener());
            img.setRoundImageViewOnTouchListener(this);
            content.addView(img);
        }
    }

    private void handlerActionDown(MotionEvent event){
        if(getHomeTuanHorizontalScrollViewOnTouchListener() != null){
            getHomeTuanHorizontalScrollViewOnTouchListener().onActionDown(event);
        }
    }

    private void handlerActionMove(MotionEvent event){
        if(getHomeTuanHorizontalScrollViewOnTouchListener() != null){
            getHomeTuanHorizontalScrollViewOnTouchListener().onActionMove(event);

        }
    }

    private void handlerActionCancel(MotionEvent event){
        if(getHomeTuanHorizontalScrollViewOnTouchListener() != null){
            getHomeTuanHorizontalScrollViewOnTouchListener().onActionCancel(event);
        }
    }

    public interface HomeTuanHorizontalScrollViewOnTouchListener{
        void onActionDown(MotionEvent ev);
        void onActionMove(MotionEvent ev);
        void onActionCancel(MotionEvent ev);
    }

    /**
     * RoundImageView onTouchEvent回调
     * @param ev
     */
    @Override
    public void onActionDown(MotionEvent ev) {
        handlerActionDown(ev);
    }

    /**
     * RoundImageView onTouchEvent回调
     * @param ev
     */
    @Override
    public void onActionMove(MotionEvent ev) {
        handlerActionMove(ev);
    }

    /**
     * RoundImageView onTouchEvent回调
     * @param ev
     */
    @Override
    public void onActionCancel(MotionEvent ev) {
        handlerActionCancel(ev);
    }

    private String getImagePath(int position){
        return datas.get(position) != null ? datas.get(position).getSpecial_icon() : "";
    }

    private void initContent(){
        content = new LinearLayout(getContext());
        HomeTuanHorizontalScrollView.LayoutParams params = new HomeTuanHorizontalScrollView.LayoutParams(matchParent(), wrapContent());
        content.setLayoutParams(params);
        content.setOrientation(LinearLayout.HORIZONTAL);
        addView(content);
    }

    class HomeTuanHorizontalScrollViewListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(onTimeLimitClickListener != null){
                onTimeLimitClickListener.onTimeLimitClick();
            }
        }
    }

    public interface OnTimeLimitClickListener{
        void onTimeLimitClick();
    }

    public void setHomeTuanTimeLimitView(HomeTuanTimeLimitView homeTuanTimeLimitView) {
        this.homeTuanTimeLimitView = homeTuanTimeLimitView;
    }

    public void setOnTimeLimitClickListener(OnTimeLimitClickListener onTimeLimitClickListener) {
        this.onTimeLimitClickListener = onTimeLimitClickListener;
    }

    public HomeTuanHorizontalScrollViewOnTouchListener getHomeTuanHorizontalScrollViewOnTouchListener() {
        return homeTuanHorizontalScrollViewOnTouchListener;
    }

    public void setHomeTuanHorizontalScrollViewOnTouchListener(HomeTuanHorizontalScrollViewOnTouchListener homeTuanHorizontalScrollViewOnTouchListener) {
        this.homeTuanHorizontalScrollViewOnTouchListener = homeTuanHorizontalScrollViewOnTouchListener;
    }
}
