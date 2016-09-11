package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miguo.live.interf.GiftListener;
import com.miguo.live.views.Manager.FalseScrollGridLayoutManager;
import com.miguo.live.views.Manager.GiftGridItemDecoration;
import com.miguo.live.adapters.GiftAdapter;

/**
 * Created by didik on 2016/9/11.
 */
public class GiftItemView extends FrameLayout{
    private Context mContext;

    private boolean isSelected=false;
    private TextView mTvSend;

    public GiftItemView(Context context) {
        this(context,null);
    }

    public GiftItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        createView();
    }

    private void createView() {
        RecyclerView item=new RecyclerView(mContext);
        FalseScrollGridLayoutManager mManager=new FalseScrollGridLayoutManager(mContext,4,
                GridLayoutManager.VERTICAL,false);
        mManager.setScrollHorizontally(false);
        mManager.setScrollVertically(false);
        item.setLayoutManager(mManager);
        GiftGridItemDecoration giftGridItemDecoration=new GiftGridItemDecoration();
        giftGridItemDecoration.setOffset(6);
        item.addItemDecoration(giftGridItemDecoration);
        GiftAdapter mAdapter=new GiftAdapter();
        mAdapter.setGiftListener(new GiftListener() {
            @Override
            public void onItemSelected(int position) {
                if (position>=0){
                    isSelected=true;
                    mTvSend.setEnabled(true);
                }else {
                    isSelected=false;
                    mTvSend.setEnabled(false);
                }
            }

            @Override
            public void onItemClickListener(View view, int position) {

            }
        });
        item.setAdapter(mAdapter);
        this.addView(item, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    public boolean getSelected(){
        return isSelected;
    }

    public void setText(TextView text) {
        this.mTvSend = text;
    }

    public Object getSelectedItemInfo(){

        return "hh";
    }
}
