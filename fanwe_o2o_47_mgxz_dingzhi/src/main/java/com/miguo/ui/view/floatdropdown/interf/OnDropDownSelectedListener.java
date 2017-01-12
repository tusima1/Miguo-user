package com.miguo.ui.view.floatdropdown.interf;

/**
 * Created by didik 
 * Created time 2017/1/9
 * Description: 
 */

public interface OnDropDownSelectedListener<T> {
//    void onDropDownSelected(int index, T levelOne, List<T> levelTwo);
    void onDropDownSelected(T levelOne, T levelTwo);
}
