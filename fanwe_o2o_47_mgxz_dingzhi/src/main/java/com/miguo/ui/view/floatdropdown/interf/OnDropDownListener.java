package com.miguo.ui.view.floatdropdown.interf;

import android.util.Pair;

import com.miguo.entity.SingleMode;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/13
 * Description: 
 */

public interface OnDropDownListener {
    void onItemSelected(int index, Pair<SingleMode,SingleMode> pair, List<SingleMode> items);
}
