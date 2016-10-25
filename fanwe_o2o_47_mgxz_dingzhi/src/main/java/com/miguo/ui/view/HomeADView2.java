package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseHorizantalScrollView;

import java.util.List;
import java.util.Random;


/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/18.
 */
public class HomeADView2 extends BaseHorizantalScrollView{

    RelativeLayout content;
    String[] urls = {
      "http://img.xiaoneiit.com/mgxz/ad2_1.jpg",
      "http://img.xiaoneiit.com/mgxz/ad2_2.jpg",
      "http://img.xiaoneiit.com/mgxz/ad2_3.jpg",
      "http://img.xiaoneiit.com/mgxz/ad2_4.jpg",
      "http://img.xiaoneiit.com/mgxz/ad2_5.jpg",
      "http://img.xiaoneiit.com/mgxz/ad2_6.jpg"
    };

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

    public void init(List ads){
        if(ads == null || ads.size() == 0){
            return ;
        }

        content.removeAllViews();

        int width = getScreenWidth() * 3 / 4;
        int height = width / 2;
        int index = 3;

        for(int i = 0; i < ads.size(); i++){
            /**
             * 标题
             */
            TextView title = new TextView(getContext());
            title.setText("啊~西湖的水~你的菜 我的饭 他的谁 还差一碗水…");
            title.setTextSize(16);
            title.setTextColor(Color.WHITE);
            title.setPadding(dip2px(30), dip2px(30), dip2px(30), dip2px(30));
            title.setGravity(Gravity.CENTER);

            /**
             * 遮罩
             */
            View view = new View(getContext());
            view.setBackgroundColor(getColor(R.color.gray_text_99_trans_46));

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

            imgParams.setMargins((i % 2 == 0 && i != 0) ? dip2px(10) : 0, i == 1 ? dip2px(10) : 0 , 0, 0);

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
            SDViewBinder.setImageView(urls[i], img);


            title.setLayoutParams(imgParams);
            view.setLayoutParams(imgParams);

            img.setBackgroundColor(Color.GRAY);
            content.addView(img);
            content.addView(view);
            content.addView(title);
        }

    }

}
