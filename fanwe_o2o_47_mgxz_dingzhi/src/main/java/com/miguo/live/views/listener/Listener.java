package com.miguo.live.views.listener;

import android.view.View;

import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.category.Category;

/**
 * Created by Administrator on 2016/8/26.
 */
public class Listener implements View.OnClickListener{

    Category category;

    public Listener(Category category){
        this.category = category;
    }


    @Override
    public void onClick(View v) {

    }

    protected Category getCategory(){
        return category;
    }


}
