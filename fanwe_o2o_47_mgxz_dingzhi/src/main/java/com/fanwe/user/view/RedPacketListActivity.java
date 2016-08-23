package com.fanwe.user.view;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.RedpacketListAdapter;
import com.fanwe.user.model.getUserRedpackets.ModelUserRedPacket;
import com.fanwe.user.model.getUserRedpackets.ResultUserRedPacket;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class RedPacketListActivity extends MGBaseActivity implements CallbackView2 {

    private PullToRefreshListView mPull2Refresh;
    private LinearLayout mLL_empty;

    private boolean isCheckMode=false;//是否可以选择

    List<ModelUserRedPacket> mData=new ArrayList<ModelUserRedPacket>();
    private RedpacketListAdapter mAdapter;
    private UserHttpHelper httpHelper;

    @Override
    protected void init() {
        mPull2Refresh = ((PullToRefreshListView) findViewById(R.id.ptrlv_content));

        mLL_empty = ((LinearLayout) findViewById(R.id.ll_empty));

        mAdapter = new RedpacketListAdapter(mData,isCheckMode);
        mPull2Refresh.setAdapter(mAdapter);

        mPull2Refresh.setMode(PullToRefreshBase.Mode.BOTH);
        mPull2Refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                httpHelper.getUserRedPackets();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPull2Refresh.onRefreshComplete();
            }
        });

        httpHelper = new UserHttpHelper(this,this);
    }

    @Override
    protected int addContentView() {
        return R.layout.activity_red_packet_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPull2Refresh.setRefreshing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        httpHelper.onDestroy();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method){
            case UserConstants.USER_RED_PACKET_LIST:
                ResultUserRedPacket resultUserRedPacket = (ResultUserRedPacket) datas.get(0);
                if (resultUserRedPacket==null){
                    return;
                }
                List<ModelUserRedPacket> body = resultUserRedPacket.getBody();
                if (body!=null && body.size()>0){
                    String page = resultUserRedPacket.getPage();
                    if (Integer.valueOf(page)==1){
                        String page_size = resultUserRedPacket.getPage_size();
                    }
                    mAdapter.update(body);
                }
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        mPull2Refresh.onRefreshComplete();
    }
}
