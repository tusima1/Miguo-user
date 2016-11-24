package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.user.view.MyCouponDetailActivity;
import com.fanwe.adapter.MyCouponsListAdapter;
import com.fanwe.base.CallbackView2;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getGroupBuyCoupon.ModelGroupCoupon;
import com.fanwe.user.model.getGroupBuyCoupon.ResultGroupCoupon;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MyCouponListFragment extends BaseFragment implements CallbackView2 {

    private UserHttpHelper httpHelper;
    private boolean isLoadMore;

    public static final class CouponTag {
        /** 所有 */
        public static final int ALL = 0;
        /** 快过期 */
        public static final int WILL_OVERDUE = 1;
        /** 未使用 */
        public static final int UN_USED = 2;
        /** 已失效 */
        public static final int OVERDUE = 3;
    }

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlv_content;

    @ViewInject(R.id.ll_empty)
    private LinearLayout mIv_empty;


    private List<ModelGroupCoupon> mListModel = new ArrayList<ModelGroupCoupon>();
    private MyCouponsListAdapter mAdapter;

    private PageModel mPage = new PageModel();

    private int mStatus;

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public static MyCouponListFragment newInstance(int status) {
        MyCouponListFragment fragment = new MyCouponListFragment();
        fragment.setmStatus(status);
        return fragment;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return setContentView(R.layout.frag_my_coupon_list);
    }

    @Override
    protected void init() {
        super.init();
        httpHelper = new UserHttpHelper(getContext(),this);
        bindDefaultData();
        initPullToRefreshListView();
    }

    private void bindDefaultData() {
        mAdapter = new MyCouponsListAdapter(mListModel, getActivity());
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage.resetPage();
                requestCoupons(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!mPage.increment()) {
                    MGToast.showToast("没有更多数据了");
                    mPtrlv_content.onRefreshComplete();
                } else {
                    requestCoupons(true);
                }
            }
        });
        mPtrlv_content.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter != null) {
                    ModelGroupCoupon model = mAdapter.getItem((int) id);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MyCouponDetailActivity.class);
                    intent.putExtra(MyCouponDetailActivity.EXTRA_COUPONLISTACTITEMMODEL, model);
                    getActivity().startActivity(intent);
                }

            }
        });

        mPtrlv_content.setRefreshing();
    }

    private void requestCoupons(final boolean isLoadMore) {
        this.isLoadMore=isLoadMore;
        httpHelper.getGroupBuyCouponList(mStatus+"",null,null,mPage.getPage());
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.GROUP_BUY_COUPON_LIST.endsWith(method)){
            ResultGroupCoupon resultGroupCoupon = (ResultGroupCoupon) datas.get(0);
            int page = MGStringFormatter.getInt(resultGroupCoupon.getPage());
            List<ModelGroupCoupon> body = resultGroupCoupon.getBody();
            if (page<=1){
                int pageSize = MGStringFormatter.getInt(resultGroupCoupon.getPage_count());
                mPage.setPage_total(pageSize);
                if (mAdapter !=null && mAdapter.getData()!=null && mAdapter.getData().size()>0){
                    mIv_empty.setVisibility(View.GONE);
                }else {
                    if (body==null || body.size()==0){
                        mIv_empty.setVisibility(View.VISIBLE);
                    }else {
                        mIv_empty.setVisibility(View.GONE);
                    }
                }
            }
            SDViewUtil.updateAdapterByList(mListModel, body, mAdapter, isLoadMore);
        }
    }

    @Override
    public void onFailue(String responseBody) {
        if (UserConstants.GROUP_BUY_COUPON_LIST.endsWith(responseBody)){
            mIv_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinish(String method) {
        if (UserConstants.GROUP_BUY_COUPON_LIST.endsWith(method)){
            mPtrlv_content.onRefreshComplete();
        }

    }

}