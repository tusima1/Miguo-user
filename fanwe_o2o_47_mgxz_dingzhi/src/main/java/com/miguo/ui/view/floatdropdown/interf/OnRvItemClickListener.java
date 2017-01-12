package com.miguo.ui.view.floatdropdown.interf;

import android.view.View;

/**
 * Created by didik 
 * Created time 2017/1/12
 * Description: 
 */

public interface OnRvItemClickListener<T> {
    void onRvItemClick(View view,int position,T t);
}
