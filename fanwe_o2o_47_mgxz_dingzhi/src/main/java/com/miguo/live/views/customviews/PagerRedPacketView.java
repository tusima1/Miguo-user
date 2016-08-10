package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.PagerRedPacketAdapter;


/**
 * Created by didik on 2016/7/29.
 */
public class PagerRedPacketView extends LinearLayout {

    private Context mContext;
    private TextView mTv_redNum;
    private RecyclerView mRecyclerRed;
  



    public PagerRedPacketView(Context context) {
        super(context);
        init(context);
    }

    public PagerRedPacketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagerRedPacketView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_red_packet,this);
        mTv_redNum = ((TextView) this.findViewById(R.id.tv_red_num));
        mRecyclerRed = ((RecyclerView) findViewById(R.id.recycler_red));

        /*init RecyclerView*/
        PagerRedPacketAdapter mAdapter=new PagerRedPacketAdapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerRed.setLayoutManager(layoutManager);
        mRecyclerRed.setHasFixedSize(true);
        mRecyclerRed.setAdapter(mAdapter);

    }
}
