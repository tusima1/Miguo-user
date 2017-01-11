package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.miguo.ui.view.floatdropdown.adapter.SingleListAdapter;
import com.miguo.entity.SingleMode;
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
                    onDropDownSelectedListener.onDropDownSelected(singleMode);
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

    @Override
    public void setOnDropDownSelectedListener(OnDropDownSelectedListener<SingleMode>
                                                          onDropDownSelectedListener) {
        this.onDropDownSelectedListener=onDropDownSelectedListener;
    }
}
