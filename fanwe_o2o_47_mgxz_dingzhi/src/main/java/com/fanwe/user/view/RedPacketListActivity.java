package com.fanwe.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.RedpacketListAdapter;
import com.fanwe.user.model.getUserRedpackets.ModelUserRedPacket;
import com.fanwe.user.model.getUserRedpackets.ResultUserRedPacket;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

public class RedPacketListActivity extends MGBaseActivity implements CallbackView2 {

    private PullToRefreshListView mPull2Refresh;
    private LinearLayout mLL_empty;

    private boolean isCheckMode=false;//是否可以选择

    List<ModelUserRedPacket> mData=new ArrayList<ModelUserRedPacket>();
    private RedpacketListAdapter mAdapter;
    private UserHttpHelper httpHelper;
    private OutSideShoppingCartHelper outSideShoppingCartHelper;
    /**
     * 商品IDS.
     */
    private  String mListDeal_id="";
    protected int request_CODE = 100;
    /**
     * 选 中的红包集。
     */
    private ArrayList<String> mRed_id;

    @Override
    protected void init() {
        outSideShoppingCartHelper = new OutSideShoppingCartHelper(this);
        mPull2Refresh = ((PullToRefreshListView) findViewById(R.id.ptrlv_content));

        mLL_empty = ((LinearLayout) findViewById(R.id.ll_empty));

        mAdapter = new RedpacketListAdapter(mData,isCheckMode);
        mPull2Refresh.setAdapter(mAdapter);

        mPull2Refresh.setMode(PullToRefreshBase.Mode.BOTH);
        mPull2Refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(TextUtils.isEmpty(mListDeal_id)) {
                    httpHelper.getUserRedPackets();
                }else{
                    outSideShoppingCartHelper.getUsableRedPacketList(mListDeal_id);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPull2Refresh.onRefreshComplete();
            }
        });

        httpHelper = new UserHttpHelper(this,this);
    }

    @Override
    protected void initTitle(LinearLayout container) {
        setTitle("红包列表");
        intentValue();
        super.initTitle(container);
    }

    @Override
    protected int addContentView() {
        return R.layout.activity_red_packet_list;
    }

    @Override
    protected int setRightImageSrcId() {
        int resId;
        if (isCheckMode){
            resId=R.drawable.app_icon;
        }else {
            resId=0;
        }
        return resId;
    }

    @Override
    protected void onRightImageClick(View v) {
        if(mAdapter!=null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("selectedIDs",  mAdapter.getSelectedItemIds());
            intent.putExtras(bundle);
            setResult(request_CODE, intent);
            finish();
        }
    }

    private void intentValue(){
        Intent intent = getIntent();
        if(intent.hasExtra(ShoppingCartconstants.LIST_DEAL_IDS)){
             mListDeal_id = intent.getStringExtra(ShoppingCartconstants.LIST_DEAL_IDS);
             isCheckMode = true;
            if(intent.hasExtra(ShoppingCartconstants.RED_IDS)){
                mRed_id = intent.getStringArrayListExtra(ShoppingCartconstants.RED_IDS);
            }

        }

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
    protected CharSequence setTitleText() {
        return "红包列表";
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(final String method, List datas) {
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
            case ShoppingCartconstants.GET_USERING_REDPACKETS:
                mData = datas;
                if(datas!=null){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mRed_id!=null&&mRed_id.size()>0) {
                                for (int i = 0; i < mData.size(); i++) {
                                    ModelUserRedPacket modelUserRedPacket = mData
                                            .get(i);
                                    if(mRed_id.contains(modelUserRedPacket.getRed_packet_id())){
                                        modelUserRedPacket.setChecked(true);
                                    }else{
                                        modelUserRedPacket.setChecked(false);
                                    }
                                }
                            }
                            mAdapter.update(mData);
                        }
                    });
                }
                break;
            default:
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
