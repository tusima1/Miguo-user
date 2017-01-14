package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.miguo.entity.SingleMode;
import com.miguo.ui.view.floatdropdown.adapter.SingleListAdapter;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener;
import com.miguo.ui.view.floatdropdown.interf.OnSingleModeRVItemClickListener;
import com.miguo.ui.view.floatdropdown.interf.SetDropDownListener;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/6
 * Description: 
 */

public class SingleSideListView extends FrameLayout implements SetDropDownListener<SingleMode> {

    private RecyclerView singleRv;
    private SingleListAdapter singleAdapter;
    private OnDropDownSelectedListener<SingleMode> onDropDownSelectedListener;

    public SingleSideListView(Context context) {
        this(context,null);
    }

    public SingleSideListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SingleSideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(Color.WHITE);
        init();
    }

    private void init() {
        addRv();
    }

    private void initRv() {
        singleRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        singleAdapter.setItemClickListener(new OnSingleModeRVItemClickListener() {
            @Override
            public void onItemClick(View v, int position, SingleMode singleMode) {
                if (onDropDownSelectedListener!=null){
                    onDropDownSelectedListener.onDropDownSelected(singleMode,null);
                }
            }
        });
        singleRv.setAdapter(singleAdapter);
    }

    private void addRv(){
        singleRv = new RecyclerView(getContext());
        addView(singleRv);
    }

    public void setData(List<SingleMode> singleModes){
        singleAdapter = new SingleListAdapter(singleModes);
        initRv();
    }
    /**
     * 代替 performClick()方法,在adapter初始化的时候加载.
     * 如果想用performCLick() 参见 {@link TwoSideListView#handlePerformClick(int, int)}
     */
    public void performPosition(int clickPosition){
        if (clickPosition<0){
            return;
        }
        singleAdapter.performPosition(clickPosition);
    }
    @Override
    public void setOnDropDownSelectedListener(OnDropDownSelectedListener<SingleMode>
                                                          onDropDownSelectedListener) {
        this.onDropDownSelectedListener=onDropDownSelectedListener;
    }
}
