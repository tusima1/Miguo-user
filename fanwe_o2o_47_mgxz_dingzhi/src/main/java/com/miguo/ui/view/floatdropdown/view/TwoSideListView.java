package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.miguo.ui.view.floatdropdown.adapter.LeftListAdapter;
import com.miguo.ui.view.floatdropdown.adapter.RightListAdapter;
import com.miguo.entity.SingleMode;
import com.miguo.entity.TwoMode;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownSelectedListener;
import com.miguo.ui.view.floatdropdown.interf.OnLeftRVItemClickListener;
import com.miguo.ui.view.floatdropdown.interf.OnSingleModeRVItemClickListener;
import com.miguo.ui.view.floatdropdown.interf.SetDropDownListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class TwoSideListView extends LinearLayout implements SetDropDownListener<SingleMode> {
    private RecyclerView leftListView;
    private RecyclerView rightListView;
    private List<TwoMode> data =new ArrayList<>();
    private LeftListAdapter leftAdapter;
    private RightListAdapter rightAdapter;
    private int currLeftMarkPosition = -2;//左边的位置
    private OnDropDownSelectedListener<SingleMode> onDropDownSelectedListener;

    public TwoSideListView(Context context) {
        this(context,null);
    }

    public TwoSideListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TwoSideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        createListView();
    }

    private void createListView(){

        leftListView =new RecyclerView(getContext());
        rightListView=new RecyclerView(getContext());

        leftListView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        rightListView.setBackgroundColor(Color.WHITE);
        addView(leftListView,new LayoutParams(0, LayoutParams.MATCH_PARENT,31));
        addView(rightListView,new LayoutParams(0, LayoutParams.MATCH_PARENT,71));

    }

    /**
     * 代替 performClick()方法,在adapter初始化的时候加载.
     * 如果想用performCLick() 参见 {@link TwoSideListView#handlePerformClick(int, int)}
     * @param leftPosition 左边为一级类目
     * @param rightPosition 右边为二级类目
     */
    public void performPosition(int leftPosition, int rightPosition){
        if (leftPosition<0 ||rightPosition <0){
            return;
        }
        Log.e("test","TwosideListView = "+ "leftPosition: "+leftPosition + "rightPosition: "+rightPosition);
        leftAdapter.performPosition(leftPosition);
        rightAdapter.setData(data.get(leftPosition).getSingleModeList());
        rightAdapter.performPosition(rightPosition);
        currLeftMarkPosition = leftPosition;
        clearLastDataState();
    }

    public void setData(final List<TwoMode> data){
        this.data = data;
        leftAdapter = new LeftListAdapter(data);
        leftListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        leftListView.setAdapter(leftAdapter);
        setRightListView(null);

        leftAdapter.setOnItemClickListener(new OnLeftRVItemClickListener() {
            @Override
            public void onItemClick(View v, final int position, final List<SingleMode> singleModes) {
                Log.e("test","left item click: "+ position);
                currLeftMarkPosition =position;
                final List singleModeList = data.get(position).getSingleModeList();
                //当不存在二级列表时,选中二级就直接回调!(产品需求)
                if (singleModeList == null || singleModeList.size() == 0){
                    if (onDropDownSelectedListener!=null){
                        onDropDownSelectedListener.onDropDownSelected(data.get(position),null);
                    }
                }
                leftListView.post(new Runnable() {
                    @Override
                    public void run() {
                        rightAdapter.updateData(singleModeList);
                    }
                });
            }
        });

    }

    private void setRightListView(List<SingleMode> singleModes){
        rightAdapter = new RightListAdapter(singleModes);
        rightListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rightListView.setAdapter(rightAdapter);

        rightAdapter.setOnItemClickListener(new OnSingleModeRVItemClickListener() {
            @Override
            public void onItemClick(View v, int position, SingleMode singleMode) {
                int leftCurrPosition = leftAdapter.getClickPosition();
                if (onDropDownSelectedListener!=null){
                    onDropDownSelectedListener.onDropDownSelected(data.get(leftCurrPosition),singleMode);
                }
                currLeftMarkPosition = leftCurrPosition;
                Log.e("test","之前的位置: "+ currLeftMarkPosition);
                clearLastDataState();
            }
        });
    }

    private void clearLastDataState() {
        if (currLeftMarkPosition <0){
            return;
        }
        //先这样吧,后面根据position定点恢复
        int leftSize = data.size();
        for (int i = 0; i < leftSize; i++) {
            if (currLeftMarkPosition !=i ){
                TwoMode twoMode = data.get(i);
                int size = twoMode.getSingleModeList().size();
                for (int i1 = 0; i1 < size; i1++) {
                    SingleMode item = (SingleMode) twoMode.getSingleModeList().get(i1);
                    if (item.isChecked()){
                        item.setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    public void setOnDropDownSelectedListener(OnDropDownSelectedListener<SingleMode>
                                                          onDropDownSelectedListener) {
        this.onDropDownSelectedListener=onDropDownSelectedListener;
    }

    public void handlePerformClick(final int left, final int right) {
        leftListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                leftListView.findViewHolderForAdapterPosition(left).itemView.performClick();
                rightListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rightListView.findViewHolderForAdapterPosition(right).itemView.performClick();
                    }
                },300);

            }
        },300);
    }
}
