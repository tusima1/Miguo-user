package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.view.RoundImageView;
import com.miguo.entity.AdspaceListBean;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.utils.DisplayUtil;

import java.util.List;
import java.util.Random;


/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/18.
 */
public class HomeADView3 extends BaseHorizantalScrollView{

    RelativeLayout content;
    List<AdspaceListBean.Result.Body> ads;
    OnTopicAdsClickListener onTopicAdsClickListener;

    public HomeADView3(Context context) {
        super(context);
        init();
    }

    public HomeADView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeADView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        setHorizontalScrollBarEnabled(false);
        content = new RelativeLayout(getContext());
        LayoutParams params = new LayoutParams(matchParent(), wrapContent());
        content.setLayoutParams(params);
        content.setBackgroundColor(Color.WHITE);
        addView(content);
    }

    public void init(List<AdspaceListBean.Result.Body> ads){
        content.removeAllViews();
        if(ads == null || ads.size() == 0){
            return ;
        }

        this.ads = ads;


        int screenWidth = getScreenWidth();
        int margionSpace = dip2px(5);
        int width = (int)((screenWidth - margionSpace * 3) / (float)2.8);
        int height = width * 65 / 100;
        int index = 2;

        for(int i = 0; i < ads.size(); i++){

            /**
             * 遮罩
             */
            RoundImageView view = new RoundImageView(getContext());
//            view.setBackgroundColor(Color.argb(64, 0, 0, 0));
            view.setRectAdius((float)dip2px(5));


            /**
             * 标题
             */
            TextView title = new TextView(getContext());
            title.setText(getItem(i).getTitle());
            title.setTextSize(14);
            title.setMaxLines(2);
            title.setTextColor(Color.WHITE);
            title.setLineSpacing(0, 1.3f);
//            title.setBackgroundColor(Color.argb(64, 0, 0, 0));
            title.setPadding(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
            title.setGravity(Gravity.CENTER);

            /**
             * 背景图
             */
            RoundImageView img = new RoundImageView(getContext());
            img.setRectAdius((float)dip2px(5));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setId(new Random().nextInt(999999) + 100000);
            RelativeLayout.LayoutParams imgParams = getRelativeLayoutParams(width, height);

            /**
             * 设置图片间距
             */
            imgParams.setMargins(dip2px(5),0 , 0, 0);

            /**
             * 第一张为1
             * 如果是顶排的图片，并且不是第一个
             * 那应该是3 , 5, 7 ,9..图片
             */
            if(i != 0){
                imgParams.addRule(RelativeLayout.RIGHT_OF, content.getChildAt((i - 1) * index).getId());
                imgParams.addRule(RelativeLayout.ALIGN_TOP, content.getChildAt((i - 1) * index).getId());
            }

            img.setLayoutParams(imgParams);

            if(ads.get(i).getResId() > 0){
                img.setImageResource(ads.get(i).getResId());
            }else {
                String url =getItem(i).getIcon();
                if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
                    url = DisplayUtil.qiniuUrlExchange(url,200,100);
                }
                SDViewBinder.setImageView(url, img);
            }

            title.setLayoutParams(imgParams);
            view.setLayoutParams(imgParams);

            img.setBackgroundColor(Color.GRAY);
            content.addView(img);
//            content.addView(view);
            content.addView(title);

            img.setOnClickListener(new TopicAdsListener(i));

        }
    }

    class TopicAdsListener implements OnClickListener{

        int position;

        public TopicAdsListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(onTopicAdsClickListener != null){
                onTopicAdsClickListener.onTopicAdsClick(getItem(position));
            }
        }
    }

    public void setOnTopicAdsClickListener(OnTopicAdsClickListener onTopicAdsClickListener) {
        this.onTopicAdsClickListener = onTopicAdsClickListener;
    }

    public interface OnTopicAdsClickListener{
        void onTopicAdsClick(AdspaceListBean.Result.Body ad);
    }

    public AdspaceListBean.Result.Body getItem(int position){
        return ads.get(position);
    }

}
