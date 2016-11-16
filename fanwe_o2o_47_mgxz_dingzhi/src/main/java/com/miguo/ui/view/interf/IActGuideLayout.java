package com.miguo.ui.view.interf;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by didik on 2016/11/16.
 */

public interface IActGuideLayout<T> {
    /**
     * 添加点,添加操作应该是一次性的
     * @param num
     * @param target
     */
    void addDot2LinearLayout(int num, LinearLayout target);

    /**
     * 隐藏点点点
     * @param dotContainer
     */
    void hideDotLayout(ViewGroup dotContainer);

    /**
     * 添加textview
     * @param tags 文字
     * @param target 目标容器
     */
    void addTextView(List<String> tags, LinearLayout target,int num);

    /**
     * 移除textview
     * @param left 剩余的个数
     */
    void removeTextView(int left);

    TextView createTextView();
    View createDotView();


    /**
     * 添加水平滚动的rv
     * @param data
     */
    void addHorizontalRecyclerView(List<T> data,ViewGroup rvContainer);

    void hideHorizontalRecyclerView(ViewGroup rvContainer);
}
