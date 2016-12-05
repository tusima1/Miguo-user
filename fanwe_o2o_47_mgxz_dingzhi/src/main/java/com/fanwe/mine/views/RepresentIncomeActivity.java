package com.fanwe.mine.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.commission.model.getCommissionLog.ResultCommissionLog;
import com.fanwe.commission.presenter.LogHttpHelper;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.mine.adapters.RepresentIncomeAdapter;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代言收益
 * Created by qiang.chen on 2016/10/20.
 */
public class RepresentIncomeActivity extends Activity implements CallbackView {
    private Context mContext = RepresentIncomeActivity.this;
    private PullToRefreshPinnedSectionListView mPTR;
    private TextView tvMoney;
    private ImageView ivRank;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private List<ModelCommissionLog> mDatas = new ArrayList<ModelCommissionLog>();
    private int rank = -1;
    private LogHttpHelper httpHelper;
    private ResultCommissionLog resultCommissionLog;
    private RepresentIncomeAdapter mRepresentIncomeAdapter;
    private LinearLayout ll_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_represent_income);
        initTitle();
        preWidget();
        preData();
    }

    private void preWidget() {
        tvMoney = (TextView) findViewById(R.id.tv_money_act_represent_income);
        ivRank = (ImageView) findViewById(R.id.iv_rank_act_represent_income);
        mPTR = (PullToRefreshPinnedSectionListView) findViewById(R.id.list);
        mPTR.setMode(PullToRefreshBase.Mode.BOTH);
        mPTR.setOnRefreshListener(mOnRefresherListener2);
        mPTR.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long positionL) {
                position--;
                ModelCommissionLog bean = mDatas.get(position);
            }
        });
        mPTR.setRefreshing();
        ivRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RepresentIntroduceActivity.class));
            }
        });

        ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mOnRefresherListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(
                PullToRefreshBase<ListView> refreshView) {
            isRefresh = true;
            pageNum = 1;
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            isRefresh = false;
            if (!SDCollectionUtil.isEmpty(items)) {
                pageNum++;
            }
            getData();
        }
    };


    public void showLLEmpty() {
        if (mRepresentIncomeAdapter.getCount() <= 0) {
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_empty.setVisibility(View.GONE);
        }
    }

    private void getData() {
        httpHelper.getUserCommissionLog(String.valueOf(pageNum), String.valueOf(pageSize));
    }


    private void preData() {
        httpHelper = new LogHttpHelper(this);
        mRepresentIncomeAdapter = new RepresentIncomeAdapter(mContext, getLayoutInflater(), mDatas);
        mPTR.setAdapter(mRepresentIncomeAdapter);
        String fxLevel = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getFx_level();
        if ("2".equals(fxLevel)) {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_2);
        } else if ("3".equals(fxLevel)) {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_3);
        } else {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_1);
        }
    }

    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("代言收益");
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    public void bindData() {
        if (isRefresh) {
            mDatas.clear();
        }
        items = resultCommissionLog.getList();
        if (!SDCollectionUtil.isEmpty(items)) {
            for (ModelCommissionLog bean : items) {
                String time = DateFormat.format("HH:mm:ss", DataFormat.toLong(bean.getInsert_time())).toString();
                if (!containDate(bean.getInsert_time())) {
                    ModelCommissionLog temp = new ModelCommissionLog();
                    temp.setType(1);
                    String tempTime = DateFormat.format("yyyy年MM月", DataFormat.toLong(bean.getInsert_time())).toString();
                    tempTime = yearStr(tempTime);
                    temp.setInsert_time(tempTime);
                    mDatas.add(temp);
                    bean.setInsert_time(time);
                    mDatas.add(bean);
                } else {
                    bean.setInsert_time(time);
                    mDatas.add(bean);
                }
            }
        }
    }

    List<ModelCommissionLog> items;

    @Override
    public void onSuccess(String method, List datas) {
        if (CommissionConstance.USER_COMMISSION_LOG.endsWith(method)) {
            if (SDCollectionUtil.isEmpty(datas)) {
                return;
            }
            resultCommissionLog = (ResultCommissionLog) datas.get(0);
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SDViewBinder.setTextView(tvMoney, resultCommissionLog.getSalary_total(), "0");
                }
            });
            if (isRefresh) {
                mDatas.clear();
            }
            items = resultCommissionLog.getList();
            if (!SDCollectionUtil.isEmpty(items)) {
                for (ModelCommissionLog bean : items) {
                    String time = DateFormat.format("HH:mm:ss", DataFormat.toLong(bean.getInsert_time())).toString();
                    if (!containDate(bean.getInsert_time())) {
                        ModelCommissionLog temp = new ModelCommissionLog();
                        temp.setType(1);
                        String tempTime = DateFormat.format("yyyy-MM-dd", DataFormat.toLong(bean.getInsert_time())).toString();
                        temp.setInsert_time(tempTime);
                        mDatas.add(temp);
                        bean.setInsert_time(time);
                        mDatas.add(bean);
                    } else {
                        bean.setInsert_time(time);
                        mDatas.add(bean);
                    }
                }
            }
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRepresentIncomeAdapter.notifyDataSetChanged();
                    showLLEmpty();

                }
            });


        }
    }

    /**
     * 是否包含当天的title
     *
     * @param inTimeInMillis
     * @return
     */
    private boolean containDate(String inTimeInMillis) {
        String mm = DateFormat.format("yyyy年MM月", DataFormat.toLong(inTimeInMillis)).toString();
        mm=yearStr(mm);

        if (!SDCollectionUtil.isEmpty(mDatas)) {
            for (ModelCommissionLog bean : mDatas) {
                if (1 == bean.getType()) {
                    if (mm.equals(bean.getInsert_time())) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private String yearStr(String value) {
        if (TextUtils.isEmpty(value) || value.length() < 4) {
            return "";
        }
        String  valueYear = value.substring(0,4);
        String sysYear = DateFormat.format("yyyy", App.getInstance().getSysTime()).toString();
        if (sysYear.equals(valueYear)&&value.length()>5) {
             return value.substring(5);
        }else{
            return value;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        if (CommissionConstance.USER_COMMISSION_LOG.equals(method)) {
            mPTR.onRefreshComplete();
        }
    }
}
