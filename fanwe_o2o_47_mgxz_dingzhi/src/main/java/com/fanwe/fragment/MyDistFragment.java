package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.adapter.MyXiaoMiAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.Member;
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
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

public class MyDistFragment extends BaseFragment implements View.OnClickListener, CallbackView {
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

    private MyXiaoMiAdapter mAdapter;

    private List<Member> listModel = new ArrayList<Member>();
    protected OnDialogData mListener;

    private int mType;
    private String mRank;
    private int pageNum = 1;
    private  int maxPage=1;
    private int pageSize = 10;
    private UserHttpHelper userHttpHelper;

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
        bindData();
        initClick();
        initPullToScrollView();
    }
    public void setPageType(int pageType) {
        this.mType = pageType;
    }
    public void clearBtn() {
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(true);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.text_fenxiao));
    }

    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(getActivity(), this);
        }
        userHttpHelper.getMyDistributionCorps(mType + "", mRank, pageNum, pageSize, "");
    }


    public void finishRefresh(){
        mPtr_ScrollView.onRefreshComplete();
    }

    public void setResultMyDistributionCorps(ResultMyDistributionCorps  datas) {
        finishRefresh();

        parseData(datas);
    }



    private void initClick() {
        mLl_vip1.setOnClickListener(this);
        mLl_vip2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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

    private void clickVip2() {
        mRank = "2";

        pageNum = 1;
        mLl_vip1.setEnabled(true);
        mLl_vip2.setEnabled(false);
        mTv_textVip1
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        mTv_textVip2.setTextColor(getResources().getColor(R.color.main_color));
        getData();
    }

    private void clickVip1() {
        mRank = "1";

        pageNum = 1;
        mLl_vip1.setEnabled(false);
        mLl_vip2.setEnabled(true);
        mTv_textVip1.setTextColor(getResources().getColor(R.color.main_color));
        mTv_textVip2
                .setTextColor(getResources().getColor(R.color.text_fenxiao));
        getData();
    }

    private void initPullToScrollView() {
        mPtr_ScrollView.setMode(Mode.BOTH);
        mPtr_ScrollView
                .setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
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
                       if(maxPage>=pageNum) {
                           getData();
                       }
                    }
                });
//      mPtr_ScrollView.setRefreshing();
    }

    private void bindData() {
        mAdapter = new MyXiaoMiAdapter(listModel, getActivity(), mType);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mLl_empty);
    }

    @Override
    public void onSuccess(String responseBody) {

    }


    @Override
    public void onSuccess(String method, final List datas) {
        finishRefresh();
        switch (method){
            case UserConstants.MY_DISTRIBUTION_CROPS:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(datas==null){
                            return;
                        }
                        parseData((ResultMyDistributionCorps)datas.get(0));
                    }
                });
                break;
            default:
                break;

        }

    }

    /**
     * 解析数据。
     * @param datas
     */
    public void parseData(ResultMyDistributionCorps datas){
        if(datas==null){
            return;
        }
        com.miguo.live.model.PageModel mPage =  datas.getPage();
        if(mPage ==null){
            return;
        }
        pageNum = Integer.valueOf(mPage.getPage());
        maxPage = Integer.valueOf(mPage.getPage_total());
        if(pageNum==1){
            listModel.clear();
        }
        pageNum ++;

        if (!SDCollectionUtil.isEmpty(datas.getList())) {
            for (ModelMyDistributionCorps bean : datas.getList()) {
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



    @Override
    public void onFailue(String responseBody) {
        finishRefresh();

    }

    @Override
    public void onFinish(String method) {
        mPtr_ScrollView.onRefreshComplete();

    }

    public interface OnDialogData {
        void setData(int vip1, int num1, int num2, int total, String up_name, String up_id);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
