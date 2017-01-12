package com.miguo.ui.view.floatdropdown.interf;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/9
 * Description: 
 */

public interface OnDropDownSelectedListener2<T> {
//    void onDropDownSelected(int index, T levelOne, List<T> levelTwo);
    void onDropDownSelected(T levelOne, List<T> levelTwo);
}
