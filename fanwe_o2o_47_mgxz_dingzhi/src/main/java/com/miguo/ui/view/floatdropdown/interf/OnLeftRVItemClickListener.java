package com.miguo.ui.view.floatdropdown.interf;

import android.view.View;

import com.miguo.entity.SingleMode;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public interface OnLeftRVItemClickListener {
    void onItemClick(View v, int position, List<SingleMode> singleModes);
}
