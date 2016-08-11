package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.base.Root;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.UserRedPacketInfo;
import com.miguo.live.presenters.LiveHttpHelper;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by didik on 2016/7/29.
 */
public class PagerRedPacketView extends LinearLayout implements CallbackView{

    private Context mContext;
    private TextView mTv_redNum;
    private RecyclerView mRecyclerRed;
    private LiveHttpHelper mLiveHttpHelper;

    private PagerRedPacketAdapter mAdapter;


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
        mLiveHttpHelper = new LiveHttpHelper(mContext,this);
        mLiveHttpHelper.getUserRedPacketList(CurLiveInfo.getRoomNum()+"");
        LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_red_packet,this);
        mTv_redNum = ((TextView) this.findViewById(R.id.tv_red_num));
        mRecyclerRed = ((RecyclerView) findViewById(R.id.recycler_red));

        /*init RecyclerView*/
        mAdapter=new PagerRedPacketAdapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerRed.setLayoutManager(layoutManager);
        mRecyclerRed.setHasFixedSize(true);
        mRecyclerRed.setAdapter(mAdapter);

    }

    @Override
    public void onSuccess(String responseBody) {


    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case LiveConstants.AUDIENCE_LIST:
                if (mAdapter != null) {
                    mAdapter = new PagerRedPacketAdapter();
                }

                int size = datas==null?0:datas.size();
                if (datas == null) {
                    mAdapter.setMdatas(null);
                } else {
                     mAdapter.setMdatas(datas);
                }
                mTv_redNum.setText("已经抢到"+size+"个红包");
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }
}
