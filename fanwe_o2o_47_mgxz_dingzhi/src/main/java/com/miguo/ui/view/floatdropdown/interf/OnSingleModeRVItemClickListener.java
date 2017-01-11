package com.miguo.ui.view.floatdropdown.interf;

import android.view.View;

import com.miguo.entity.SingleMode;


/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public interface OnSingleModeRVItemClickListener<T extends SingleMode> {
    void onItemClick(View v, int position, T model);
}
