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

import com.fanwe.adapter.MyXiaoMiAdapter;
import com.fanwe.base.CallbackView2;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.Member;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getMyDistributionCorps.ModelMyDistributionCorps;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MyDistFragment extends BaseFragment implements View.OnClickListener, CallbackView2 {
    @ViewInject(R.id.frag_my_xiaomi)
    private PullToRefreshScrollView mPtr_ScrollView;

    @ViewInject(R.id.listView_xiaomi)
    private SDListViewInScroll mListView;

    @ViewInject(R.id.ll_empty)
    private LinearLayout mLl_empty;

    @ViewInject(R.id.ll_vip1)
    private LinearLayout mLl_vip1;

    @ViewInject(R.id.tv_textVip1)
    private TextView mTv_textVip1;

    @ViewInject(R.id.tv_vip1Number)
    private TextView mTv_vip1Number;

    @ViewInject(R.id.ll_vip2)
    private LinearLayout mLl_vip2;

    @ViewInject(R.id.tv_textVip2)
    private TextView mTv_textVip2;

    @ViewInject(R.id.tv_vip2Number)
    private TextView mTv_vip2Number;

    @ViewInject(R.id.ll_vip3)
    private LinearLayout mLl_vip3;

    @ViewInject(R.id.tv_textVip3)
    private TextView mTv_textVip3;

    @ViewInject(R.id.tv_vip3Number)
    private TextView mTv_vip3Number;

    private MyXiaoMiAdapter mAdapter;
    private PageModel mPage = new PageModel();
    private List<Member> listModel = new ArrayList<Member>();
    protected OnDialogData mListener;

    private int mType;
    private String mRank;
    private int pageNum = 1;
    private int pageSize = 10;
    private UserHttpHelper userHttpHelper;
    private boolean isRefresh = true;

    public void setmListener(OnDialogData listener) {
        this.mListener = listener;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.flag_dist);
    }

    @Override
    protected void init() {
        super.init();
        getIntentData();
        bindData();
        initClick();
        initPullToScrollView();
    }

    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(getActivity(), this);
        }
        userHttpHelper.getMyDistributionCorps(mType + "", mRank, pageNum, pageSize, "");
    }

    private ResultMyDistributionCorps currResultMyDistributionCorps;

    public void setResultMyDistributionCorps(ResultMyDistributionCorps bean) {
        currResultMyDistributionCorps = bean;
        if (currResultMyDistributionCorps != null) {
            SDViewBinder.setTextView(mTv_vip1Number, "（" + currResultMyDistributionCorps.getLevel1() + "）");
            SDViewBinder.setTextView(mTv_vip2Number, "（" + currResultMyDistributionCorps.getLevel2() + "）");
            SDViewBinder.setTextView(mTv_vip3Number, "（" + currResultMyDistributionCorps.getLevel3() + "）");
        }
    }

    private void convertList() {
        if (isRefresh)
            listModel.clear();
        if (!SDCollectionUtil.isEmpty(currResultMyDistributionCorps.getList())) {
            for (ModelMyDistributionCorps bean : currResultMyDistributionCorps.getList()) {
                Member member = new Member();
                member.setId(bean.getUser_id());
                member.setUid(bean.getUser_id());
                member.setAvatar(bean.getIcon());
                member.setCreate_time(bean.getFx_time());
                member.setMobile(bean.getMobile());
                member.setRank(DataFormat.toInt(bean.getFx_level()));
                member.setSalary(bean.getFx_total_commission());
                member.setUser_name(bean.getNick());
                member.setUser_num(DataFormat.toInt(bean.getUser_num()));

                listModel.add(member);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initClick() {
        mLl_vip1.setOnClickListener(this);
        mLl_vip2.setOnClickListener(this);
        mLl_vip3.setOnClickListener(this);
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
            case R.id.ll_vip3:
                clickVip3();
                break;
            default:
                break;
        }
    }

    private void clickVip3() {
        mRank = "3";
        isRefresh = true;
        pageNum = 1;
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(true);
        mLl_vip3.setEnabled(false);
        mTv_textVip1
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip3.setTextColor(getResources().getColor(R.color.main_color));
        getData();
    }

    private void clickVip2() {
        mRank = "2";
        isRefresh = true;
        pageNum = 1;
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(false);
        mLl_vip3.setEnabled(true);
        mTv_textVip1
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip3
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.main_color));
        getData();
    }

    private void clickVip1() {
        mRank = "1";
        isRefresh = true;
        pageNum = 1;
        mLl_vip1.setEnabled(false);
        mLl_vip2.setEnabled(true);
        mLl_vip3.setEnabled(true);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.main_color));
        mTv_textVip2
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip3
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        getData();
    }

    private void getIntentData() {
        mType = getArguments().getInt("type");
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
                        mLl_vip3.setEnabled(true);
                        mTv_textVip1.setTextColor(getResources().getColor(
                                R.color.text_fenxiao));
                        mTv_textVip2.setTextColor(getResources().getColor(
                                R.color.text_fenxiao));
                        mTv_textVip3.setTextColor(getResources().getColor(
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
        mPtr_ScrollView.setRefreshing();
    }

    private void bindData() {
        mAdapter = new MyXiaoMiAdapter(listModel, getActivity(), mType);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mLl_empty);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ResultMyDistributionCorps> results;
    ResultMyDistributionCorps currResult;

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
                    if (!SDCollectionUtil.isEmpty(results)) {
                        currResult = results.get(0);
                        currResultMyDistributionCorps = currResult;
                        convertList();
                        mPtr_ScrollView.onRefreshComplete();
                    }
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

    public interface OnDialogData {
        public void setData(int vip1, int num1, int num2, int total, String up_name, int up_id);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
