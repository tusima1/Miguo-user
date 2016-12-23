package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.entity.AdspaceListBean;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.utils.DisplayUtil;

import java.util.List;
import java.util.Random;


/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/18.
 */
public class HomeADView2 extends BaseHorizantalScrollView{

    RelativeLayout content;
    List<AdspaceListBean.Result.Body> ads;
    OnTopicAdsClickListener onTopicAdsClickListener;

    public HomeADView2(Context context) {
        super(context);
        init();
    }

    public HomeADView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeADView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        setHorizontalScrollBarEnabled(false);
        content = new RelativeLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(matchParent(), wrapContent());
        content.setLayoutParams(params);
        addView(content);
    }

    public void init(List<AdspaceListBean.Result.Body> ads){
        content.removeAllViews();
        if(ads == null || ads.size() == 0){
            return ;
        }

        this.ads = ads;


        int width = getScreenWidth() * 3 / 4;
        int height = width / 2;
        int index = 3;

        for(int i = 0; i < ads.size(); i++){

            /**
             * 遮罩
             */
            View view = new View(getContext());
            view.setBackgroundColor(Color.argb(150, 0, 0, 0));

            /**
             * 标题
             */
            TextView title = new TextView(getContext());
            title.setText(getItem(i).getTitle());
            title.setTextSize(16);
            title.setTextColor(Color.argb(255, 220, 222, 223));
            title.setLineSpacing(0, 1.3f);
            title.setPadding(dip2px(50), dip2px(30), dip2px(50), dip2px(30));
            title.setGravity(Gravity.CENTER);

            /**
             * 背景图
             */
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setId(new Random().nextInt(999999) + 100000);
            RelativeLayout.LayoutParams imgParams = getRelativeLayoutParams(width, height);

            /**
             * 第一张为1
             * 2 、4 、 6 、 8……
             * 坐标的图片处理
             */
            if(i % 2 == 1){
                imgParams.addRule(RelativeLayout.BELOW, content.getChildAt((i - 1) * index).getId());
                imgParams.addRule(RelativeLayout.ALIGN_LEFT, content.getChildAt((i - 1) * index).getId());
                if(i - 2 > 0){
                    imgParams.addRule(RelativeLayout.ALIGN_BOTTOM, content.getChildAt((i - 2) * index).getId());
                    imgParams.addRule(RelativeLayout.ALIGN_TOP, content.getChildAt((i - 2) * index).getId());
                }
            }

            /**
             * 设置图片间距
             */
            imgParams.setMargins((i % 2 == 0 && i != 0) ? dip2px(5) : 0, i == 1 ? dip2px(5) : 0 , 0, 0);

            /**
             * 第一张为1
             * 如果是顶排的图片，并且不是第一个
             * 那应该是3 , 5, 7 ,9..图片
             */
            if(i % 2 == 0 && i != 0){
                imgParams.addRule(RelativeLayout.RIGHT_OF, content.getChildAt((i - 1) * index).getId());
                imgParams.addRule(RelativeLayout.ALIGN_TOP, content.getChildAt((i - 2) * index).getId());
            }

            img.setLayoutParams(imgParams);

            String url =getItem(i).getIcon();
            if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
                url = DisplayUtil.qiniuUrlExchange(url,400,200);
            }
            SDViewBinder.setImageView(url, img);


            title.setLayoutParams(imgParams);
            view.setLayoutParams(imgParams);

            img.setBackgroundColor(Color.GRAY);
            content.addView(img);
            content.addView(view);
            content.addView(title);

            img.setOnClickListener(new TopicAdsListener(i));

        }
    }

    class TopicAdsListener implements View.OnClickListener{

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
