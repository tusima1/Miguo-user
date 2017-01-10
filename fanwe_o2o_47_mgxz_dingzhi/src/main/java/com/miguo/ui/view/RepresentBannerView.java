package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.base.BaseLinearLayout;

import java.util.List;

/**
 * Created by zlh on 2017/1/10.
 */

public class RepresentBannerView extends BaseLinearLayout {

    OnRepresentBannerClickListener onRepresentBannerClickListener;

    public RepresentBannerView(Context context) {
        super(context);
    }

    public RepresentBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RepresentBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        setOrientation(HORIZONTAL);
    }

    public void init(List banners){
        removeAllViews();
        if(SDCollectionUtil.isEmpty(banners)){
//            return;
        }

        int width = getImageWidth();
        int height = getImageHeight();
        int firstMarginLeft = getImageFirstMarginLeft();
        int marginLeft = getImageMarginLeft();
        int marginTop = getImageMarginTop();
        int count = banners.size() >= 3 ? 3 : banners.size();
        for(int i = 0; i < 3; i++){
            ImageView banner = new ImageView(getContext());
            LinearLayout.LayoutParams params = getLinearLayoutParams(width, height);
            params.setMargins( i == 0 ? firstMarginLeft : marginLeft , marginTop, 0, getImageMarginBottom());
//            SDViewBinder.setImageView("", banner);
            banner.setLayoutParams(params);
            banner.setBackgroundColor(Color.BLACK);
            banner.setOnClickListener(new OnRepresentBannerClick(i));
            addView(banner);
        }
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    private int getImageWidth(){
        return (int)(getScreenWidth() * 0.289);
    }

    private int getImageHeight(){
        return (int)(getScreenHeight() * 0.069);
    }

    private int getImageFirstMarginLeft(){
        return (int)(getScreenWidth() * 0.028);
    }

    private int getImageMarginLeft(){
        return (int)(getScreenWidth() * 0.039);
    }

    private int getImageMarginTop(){
        return (int)(getScreenWidth() * 0.042);
    }

    private int getImageMarginBottom(){
        return (int)(getScreenHeight() * 0.022);
    }

    class OnRepresentBannerClick implements View.OnClickListener{

        int position;

        public OnRepresentBannerClick(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(null != onRepresentBannerClickListener){
                onRepresentBannerClickListener.onBannerClick(getItem(position));
            }
        }
    }

    public void setOnRepresentBannerClickListener(OnRepresentBannerClickListener onRepresentBannerClickListener) {
        this.onRepresentBannerClickListener = onRepresentBannerClickListener;
    }

    public interface OnRepresentBannerClickListener{
        void onBannerClick(Object banner);
    }

}
