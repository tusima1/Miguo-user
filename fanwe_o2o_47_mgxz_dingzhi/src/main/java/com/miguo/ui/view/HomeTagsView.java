package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseHorizantalScrollView;
import com.miguo.live.views.customviews.RoundedImageView;

import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/18.
 */
public class HomeTagsView extends BaseHorizantalScrollView{

    LinearLayout content;
    OnHomeTagsClickListener onHomeTagsClickListener;

    public HomeTagsView(Context context) {
        super(context);
        init();
    }

    public HomeTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeTagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initContent();
    }

    private void initContent(){
        setHorizontalScrollBarEnabled(false);
        content = new LinearLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(matchParent(), wrapContent());
        content.setOrientation(LinearLayout.HORIZONTAL);
        content.setLayoutParams(params);
        addView(content);
    }

    public void init(List tags){
        if(tags == null || tags.size()==0){
            return ;
        }

        content.removeAllViews();

        int iconWidth = getScreenWidth() / 8;
        int iconHeight = iconWidth;
        for(int i = 0; i < tags.size(); i++){
            LinearLayout group = new LinearLayout(getContext());
            LinearLayout.LayoutParams grouParams = getLinearLayoutParams(wrapContent(), wrapContent());
            group.setLayoutParams(grouParams);
            group.setGravity(Gravity.CENTER);
            group.setPadding(dip2px(15), dip2px(15), dip2px(15), dip2px(15));
            group.setOrientation(LinearLayout.VERTICAL);
            group.setOnClickListener(new HomeTagsViewListener(i));

            RoundedImageView icon = new RoundedImageView(getContext());
            LinearLayout.LayoutParams iconParams = getLinearLayoutParams(iconWidth, iconHeight);
            icon.setLayoutParams(iconParams);
            icon.setOval(true);

            SDViewBinder.setImageView("http://img.xiaoneiit.com/avatar/5.jpg", icon);
            group.addView(icon);

            TextView title = new TextView(getContext());
            LinearLayout.LayoutParams titleParams = getLinearLayoutParams(wrapContent(), wrapContent());
            titleParams.setMargins(0, dip2px(3), 0, 0);
            title.setLayoutParams(titleParams);
            title.setTextColor(getColor(R.color.text_base_color));
            title.setTextSize(12);
            title.setText("米果小站");
            group.addView(title);

            content.addView(group);
        }


        View line = new View(getContext());
        LinearLayout.LayoutParams lineParams = getLinearLayoutParams(matchParent(), 1);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(getColor(R.color.text_base_color_2));
        content.addView(line);
    }

    class HomeTagsViewListener implements View.OnClickListener{

        int position;

        public HomeTagsViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(onHomeTagsClickListener != null){
                onHomeTagsClickListener.onTagsClick();
            }
        }
    }

    public interface OnHomeTagsClickListener{
        void onTagsClick();
    }

    public void setOnHomeTagsClickListener(OnHomeTagsClickListener onHomeTagsClickListener) {
        this.onHomeTagsClickListener = onHomeTagsClickListener;
    }
}
