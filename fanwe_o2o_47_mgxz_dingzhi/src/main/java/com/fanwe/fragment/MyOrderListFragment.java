package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.adapters.OrderOutAdapter;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemOut;
import com.fanwe.user.model.getOrderInfo.ResultOrderInfo;
import com.fanwe.user.presents.OrderHttpHelper;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单fragment
 *
 * @author js02
 *
 */
public class MyOrderListFragment extends BaseFragment implements CallbackView {

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlv_content;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    private OrderOutAdapter mAdapter;
    private List<ModelOrderItemOut> mListModel = new ArrayList<ModelOrderItemOut>();
    private String mPayStatus;
    private int mOrderMode;
//    protected User_Order mActModel;
    private OrderHttpHelper httpHelper;
    private int page=1;

    private boolean isLoadMore=false;
    private boolean hasMore=true;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        return setContentView(R.layout.frag_my_order_list);
    }

    @Override
    protected void init() {
        super.init();
        httpHelper = new OrderHttpHelper(this);
        getIntentData();
        initTitle();
        initPullToRefreshListView();
        bindDefaultData();
    }

    private void initTitle() {
        String title = "";
        if (mPayStatus.equals("pay_wait")) {
            title = "待付款订单";
            mOrderMode = 1;
        } else if (mPayStatus.equals("refund")) {
            title = "退款订单";
            mOrderMode = 4;
        } else if (mPayStatus.equals("use_wait")) {
            title = "待消费订单";
            mOrderMode = 2;
        } else if (mPayStatus.equals("comment_wait")) {
            title = "待评价订单";
            mOrderMode = 3;
        } else if (mPayStatus.equals("all")) {
            title = "全部订单";
            mOrderMode = 0;
        }

        mTitle.setMiddleTextTop(title);
    }

    private void getIntentData() {
        mPayStatus = getActivity().getIntent().getStringExtra(MyOrderListActivity
				.EXTRA_ORDER_STATUS);
    }

    private void bindDefaultData() {
        mAdapter = new OrderOutAdapter(mListModel, getActivity(), false, mOrderMode);
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page=1;
                hasMore=true;
                requestData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (hasMore){
                    page++;
                    requestData(true);
                }else {
                    MGToast.showToast("没有更多数据了");
                    mPtrlv_content.onRefreshComplete();
                }
            }
        });

    }

    protected void requestData(boolean isLoadMore) {
        this.isLoadMore=isLoadMore;
        httpHelper.getOrderInfo(mPayStatus,page);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case COMMENT_SUCCESS:
                refreshData();
                break;
            case PAY_ORDER_SUCCESS:
                refreshData();
                break;
            case REFRESH_ORDER_LIST:
                refreshData();
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPtrlv_content.setRefreshing();
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    //-----http start
    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        ResultOrderInfo resultOrderInfo = (ResultOrderInfo) datas.get(0);
        if (resultOrderInfo == null || resultOrderInfo.getItems()==null){
            ll_empty.setVisibility(View.VISIBLE);
            return;
        }
        int anInt = MGStringFormatter.getInt(resultOrderInfo.getPage());
        List<ModelOrderItemOut> items = resultOrderInfo.getItems();
        if (anInt <=1 && items.size()<1){
            ll_empty.setVisibility(View.VISIBLE);
        }else {
            ll_empty.setVisibility(View.GONE);
        }
        int pageItemNum = MGStringFormatter.getInt(resultOrderInfo.getData_num());
        if (pageItemNum!=10){
            hasMore=false;
        }

        SDViewUtil.updateAdapterByList(mListModel,items , mAdapter,
							isLoadMore);
    }

    @Override
    public void onFailue(String responseBody) {
        ll_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinish(String method) {
        mPtrlv_content.onRefreshComplete();
    }
    //----http end
}