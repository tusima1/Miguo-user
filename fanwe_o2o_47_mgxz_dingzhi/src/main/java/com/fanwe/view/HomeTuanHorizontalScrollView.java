package com.fanwe.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.live.views.customviews.RoundedImageView;
import com.miguo.live.views.utils.ToasUtil;

import java.util.List;

/**
 * Created by Barry/狗蛋哥 on 2016/10/10.
 */
public class HomeTuanHorizontalScrollView extends BaseHorizantalScrollView{

    LinearLayout content;
    List<SpecialListModel.Result.Body> datas;
    OnTimeLimitClickListener onTimeLimitClickListener;
    HomeTuanTimeLimitView homeTuanTimeLimitView;

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
        int margionSpace = dip2px(10);
        int width = (int)((screenWidth - margionSpace * 3) / (float)2.5);
        int height = width * 2 / 3;
        for(int i = 0; i< datas.size(); i++){
            RoundImageView img = new RoundImageView(getContext());
            LinearLayout.LayoutParams imgParams = getLinearLayoutParams(width, height);
            imgParams.setMargins(margionSpace, 0, 0, 0);
            img.setLayoutParams(imgParams);
            SDViewBinder.setImageView(getImagePath(i), img);
            img.setBackgroundColor(getColor(R.color.gray_ee));
            img.setRectAdius((float)dip2px(5));
            img.setOnClickListener(new HomeTuanHorizontalScrollViewListener());
            content.addView(img);
        }

        TextView more = new TextView(getContext());
        LinearLayout.LayoutParams moreParams = getLinearLayoutParams(height, height);
        moreParams.setMargins(margionSpace, 0, margionSpace, 0);
        more.setGravity(Gravity.CENTER);
        more.setLayoutParams(moreParams);
        more.setText("更多");
        more.setTextSize(16);
        more.setTextColor(Color.WHITE);
        more.setBackgroundResource(R.drawable.shape_cricle_gray_solid_333333);
        more.setOnClickListener(new HomeTuanHorizontalScrollViewListener());
        content.addView(more);
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
}
