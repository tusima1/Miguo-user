package com.miguo.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseLinearLayout;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 */
public class ShopDetailTagView extends BaseLinearLayout{

    String[] tags;
    int colum = 7;

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
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void init(String[] tags){
        removeAllViews();
        if(tags == null || tags.length == 0){
            return ;
        }
        this.tags = tags;
        int index = -1;
        for(int i = 0; i< tags.length; i++){
            if(i == 0 || i % colum == 0){
                LinearLayout group = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wrapContent(), wrapContent());
                params.setMargins(0, i != 0 ? dip2px(5) : 0 , 0, 0);
                group.setLayoutParams(params);
                group.setOrientation(LinearLayout.HORIZONTAL);
                addView(group);
                if(index++ > 0){
                    break;
                }
            }

            TextView tag = new TextView(getContext());
            LinearLayout.LayoutParams tagParams = getLinearLayoutParams(wrapContent(), wrapContent());
            if(i % colum != 0){
                tagParams.setMargins(dip2px(10), 0, 0, 0);
            }
            tag.setLayoutParams(tagParams);
            tag.setPadding(dip2px(5), dip2px(2), dip2px(5), dip2px(2));
            tag.setTextSize(12);
            tag.setTextColor(Color.WHITE);
            tag.setBackgroundResource(R.drawable.shape_cricle_bg_black);
            tag.setText(tags[i]);
            tag.setGravity(Gravity.CENTER);
            ((LinearLayout)getChildAt(index)).addView(tag);
        }
        postView();
    }

    private void postView(){
        post(new Runnable() {
            @Override
            public void run() {
                Log.d("ShopDetailTag", "screen width : " + getScreenWidth());
                for(int i = 0; i< getChildCount(); i++){
                    Log.d("ShopDetailTag", "child at: " + i + "width: " + getChildAt(i).getMeasuredWidth());
                    if(getChildAt(i).getMeasuredWidth() > getScreenWidth() - dip2px(34)){
                        colum--;
                        init(tags);
                        break;
                    }
                }
            }
        });
    }

}
