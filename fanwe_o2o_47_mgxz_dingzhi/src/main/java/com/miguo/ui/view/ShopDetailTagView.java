package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseLinearLayout;

import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 */
public class ShopDetailTagView extends BaseLinearLayout{

    public ShopDetailTagView(Context context) {
        super(context);
    }

    public ShopDetailTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopDetailTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void init(String[] tags){
        if(tags == null || tags.length == 0){
            return ;
        }

        for(int i = 0; i<tags.length; i++){
            TextView tag = new TextView(getContext());
            LinearLayout.LayoutParams tagParams = getLinearLayoutParams(wrapContent(), wrapContent());
            if(i != 0){
                tagParams.setMargins(dip2px(10), 0, 0, 0);
            }
            tag.setLayoutParams(tagParams);
            tag.setPadding(dip2px(5), dip2px(2), dip2px(5), dip2px(2));
            tag.setTextSize(12);
            tag.setTextColor(Color.WHITE);
            tag.setBackgroundResource(R.drawable.shape_cricle_bg_black);
            tag.setText(tags[i]);
            tag.setGravity(Gravity.CENTER);
            addView(tag);
        }

    }

}
