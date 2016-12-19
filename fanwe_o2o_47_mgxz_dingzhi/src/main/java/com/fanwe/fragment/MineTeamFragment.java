package com.fanwe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.adapter.MineTeamAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getMyDistributionCorps.ModelMyDistributionCorps;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的战队fragment
 */
public class MineTeamFragment extends BaseFragment implements View.OnClickListener, CallbackView {
    private PullToRefreshScrollView mPtr_ScrollView;
    private SDListViewInScroll mListView;
    private LinearLayout mLl_empty;
    private LinearLayout mLl_vip1;
    private TextView mTv_textVip1;
    private TextView mTv_vip1Number;
    private LinearLayout mLl_vip2;
    private TextView mTv_textVip2;
    private TextView mTv_vip2Number;

    private MineTeamAdapter mAdapter;
    private PageModel mPage = new PageModel();
    private List<ModelMyDistributionCorps> listData = new ArrayList<>();

    private int mType;
    private String mRank;
    private int pageNum = 1;
    private int pageSize = 10;
    private UserHttpHelper userHttpHelper;
    private boolean isRefresh = true;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.flag_dist);
    }

    @Override
    protected void init() {
        super.init();
        preWidget();
        bindData();
        initClick();
        initPullToScrollView();
    }

    private void preWidget() {
        mPtr_ScrollView = (PullToRefreshScrollView) findViewById(R.id.frag_my_xiaomi);
        mListView = (SDListViewInScroll) findViewById(R.id.listView_xiaomi);
        mLl_empty = (LinearLayout) findViewById(R.id.ll_empty);
        mLl_vip1 = (LinearLayout) findViewById(R.id.ll_vip1);
        mLl_vip2 = (LinearLayout) findViewById(R.id.ll_vip2);
        mTv_textVip1 = (TextView) findViewById(R.id.tv_textVip1);
        mTv_vip1Number = (TextView) findViewById(R.id.tv_vip1Number);
        mTv_textVip2 = (TextView) findViewById(R.id.tv_textVip2);
        mTv_vip2Number = (TextView) findViewById(R.id.tv_vip2Number);
    }

    public void setPageType(int pageType) {
        this.mType = pageType;
    }

    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(getActivity(), this);
        }
        userHttpHelper.getMyDistributionCorps(mType + "", mRank, pageNum, pageSize, "");
    }

    /**
     * 获取数据并清空选项
     */
    public void getDataWithClear() {
        clearBtn();
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(getActivity(), this);
        }
        userHttpHelper.getMyDistributionCorps(mType + "", mRank, pageNum, pageSize, "");
    }

    private void clearBtn() {
        mRank = "";
        pageNum = 1;
        isRefresh = true;
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(true);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.text_fenxiao));
    }

    private void initClick() {
        mLl_vip1.setOnClickListener(this);
        mLl_vip2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPage.resetPage();
        switch (v.getId()) {
            case R.id.ll_vip1:
                clickVip1();
                break;
            case R.id.ll_vip2:
                clickVip2();
                break;
            default:
                break;
        }
    }

    /**
     * 高级代言人
     */
    private void clickVip2() {
        mRank = "2";
        isRefresh = true;
        pageNum = 1;
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(false);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.main_color));
        getData();
    }

    /**
     * 初级代言人
     */
    private void clickVip1() {
        mRank = "1";
        isRefresh = true;
        pageNum = 1;
        mLl_vip1.setEnabled(false);
        mLl_vip2.setEnabled(true);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.main_color));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.text_fenxiao));
        getData();
    }

    private void initPullToScrollView() {
        mPtr_ScrollView.setMode(Mode.BOTH);
        mPtr_ScrollView
                .setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        isRefresh = true;
                        pageNum = 1;
                        mLl_vip1.setEnabled(true);
                        mLl_vip2.setEnabled(true);
                        mTv_textVip1.setTextColor(getResources().getColor(
                                R.color.text_fenxiao));
                        mTv_textVip2.setTextColor(getResources().getColor(
                                R.color.text_fenxiao));
                        getData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        isRefresh = false;
                        if (!SDCollectionUtil.isEmpty(results)) {
                            pageNum++;
                        }
                        getData();
                    }
                });
    }

    private void bindData() {
        mAdapter = new MineTeamAdapter(listData, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mLl_empty);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ResultMyDistributionCorps> results = new ArrayList<>();
    private ResultMyDistributionCorps currModel;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.MY_DISTRIBUTION_CROPS.equals(method)) {
            results = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        listData.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(results)) {
                        currModel = results.get(0);
                        if (currModel != null) {
                            //人数
                            SDViewBinder.setTextView(mTv_vip1Number, "（" + currModel.getLevel1() + "）");
                            SDViewBinder.setTextView(mTv_vip2Number, "（" + currModel.getLevel2() + "）");
                            if (!SDCollectionUtil.isEmpty(currModel.getList())) {
                                listData.addAll(currModel.getList());
                            }
                        }
                    }
                    //刷新列表
                    mAdapter.notifyDataSetChanged();
                    mPtr_ScrollView.onRefreshComplete();
                    break;
            }
        }
    };


    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
