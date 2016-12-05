package com.fanwe.user.view;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.InviteAdapter;
import com.fanwe.user.model.wallet.InviteModel;
import com.fanwe.user.presents.WalletHttpHelper;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.miguo.BaseNewActivity;
import com.miguo.live.model.PageModel;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请收益列表。
 * Created by zhouhy on 2016/11/16.
 */

public class InviteActivity extends BaseNewActivity implements CallbackView {

    private PullToRefreshPinnedSectionListView mPTR;
    private List<String> titleList;
    private PageModel pageModel;

    private InviteAdapter mAdapter;
    private List<InviteModel> mDatas;

    private WalletHttpHelper walletHttpHelper = null;
    private int pageNum = 1;
    private int pageSize = 10;
    private int maxPage = 1;
    private LinearLayout ll_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_invite);
        initTitleView("邀请奖金");
        setLeftDrawable(R.drawable.ic_left_arrow_dark);
        initView();
    }

    public void initView() {
        mPTR = (PullToRefreshPinnedSectionListView) findViewById(R.id.list);
        mPTR.setMode(PullToRefreshBase.Mode.BOTH);
        mPTR.setOnRefreshListener(mOnRefresherListener2);
        mPTR.setRefreshing();
        mAdapter = new InviteAdapter(this, this, mDatas);
        mPTR.setAdapter(mAdapter);

        ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
    }

    public void requestData() {
        if (walletHttpHelper == null) {
            walletHttpHelper = new WalletHttpHelper(this);
        }
        walletHttpHelper.getInviteList(pageNum + "", pageSize + "");
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mOnRefresherListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(
                PullToRefreshBase<ListView> refreshView) {
            pageNum = 1;
            requestData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (maxPage >= pageNum) {
                requestData();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        pageNum = 1;
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    public void showLLEmpty() {
        if (mAdapter.getCount() <= 0) {
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_empty.setVisibility(View.GONE);
        }
    }

    /**
     * 是否包含当月的title
     *
     * @param inTimeInMillis
     * @return
     */
    private boolean containTitle(String inTimeInMillis) {

        if (titleList == null) {
            titleList = new ArrayList<>();
        }
        String mm = DateFormat.format("yyyy年MM月", DataFormat.toLong(inTimeInMillis)).toString();
        mm = yearStr(mm);
        boolean result = false;
        if (!SDCollectionUtil.isEmpty(titleList)) {
            for (String value : titleList) {
                if (mm.equals(value)) {
                    result = true;
                    break;
                }
            }
        }
        if (!result) {
            titleList.add(mm);
        }
        return result;
    }


    public void parseData(List<InviteModel> datas) {
        //清空原来的数据。
        if (mDatas != null) {
            mDatas = null;
        }
        mDatas = new ArrayList<>();
        if (pageNum == 1) {
            titleList = null;
        }
        if (datas != null && datas.size() > 0) {
            for (InviteModel bean : datas) {
                String time = DateFormat.format("HH:mm:ss", DataFormat.toLong(bean.getInsert_time())).toString();
                String month_date = DateFormat.format("MM-dd", DataFormat.toLong(bean.getInsert_time())).toString();

                if (!containTitle(bean.getInsert_time())) {
                    //添加TITLE实体。
                    InviteModel temp = new InviteModel();
                    temp.setType(1);
                    String tempTime = DateFormat.format("yyyy年MM月", DataFormat.toLong(bean.getInsert_time())).toString();
                    tempTime = yearStr(tempTime);
                    temp.setYear_month(tempTime);
                    mDatas.add(temp);
                }
                bean.setMonth_date(month_date);
                bean.setTime_str(time);
                mDatas.add(bean);
            }
        }

        if (pageNum == 1) {
            mAdapter.setmDatas(mDatas);
        } else {
            mAdapter.addMoreMDatas(mDatas);
        }
        mAdapter.notifyDataSetChanged();
        showLLEmpty();
        pageNum++;

    }

    public void bindDataToView(List datas) {
        if (datas == null || datas.size() < 1) {
            return;
        }
        pageModel = (com.miguo.live.model.PageModel) datas.get(0);
        pageNum = Integer.valueOf(pageModel.getPage());
        maxPage = Integer.valueOf(pageModel.getPage_total());
        datas.remove(0);
        parseData(datas);
    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case UserConstants.POST_WALLET_BALANCE:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindDataToView(datas);
                    }
                });
                break;
            default:
                break;

        }

    }

    @Override
    public void onFailue(String responseBody) {
        onFinish(responseBody);
    }

    @Override
    public void onFinish(String method) {
        mPTR.onRefreshComplete();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        walletHttpHelper = null;
        mAdapter = null;
    }


}
