package com.fanwe.mine.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.PageBean;
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
import com.miguo.BaseNewActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代言收益
 * Created by qiang.chen on 2016/10/20.
 */
public class RepresentIncomeActivity extends BaseNewActivity implements CallbackView {
    private Context mContext = RepresentIncomeActivity.this;
    private PullToRefreshPinnedSectionListView mPTR;
    private TextView tvMoney;
    private ImageView ivRank;

    private int pageNum = 1;
    private int maxPage = 1;
    private int pageSize = 10;
    private List<ModelCommissionLog> mDatas = new ArrayList<ModelCommissionLog>();
    private int rank = -1;
    private LogHttpHelper httpHelper;
    private ResultCommissionLog resultCommissionLog;
    private RepresentIncomeAdapter mRepresentIncomeAdapter;
    private LinearLayout ll_empty;
    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_represent_income);
        //头部初始化。
        initTitleView("代言人佣金");
        setLeftDrawable(R.drawable.ic_left_arrow_dark);

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
                startActivity(new Intent(mContext, ClassNameFactory.getClass(ClassPath.REPRESENT_INTRODUCE_ACTIVITY)));
            }
        });

        ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mOnRefresherListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(
                PullToRefreshBase<ListView> refreshView) {
            pageNum = 1;
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (maxPage >= pageNum) {
                getData();
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
        String fxLevel = App.getInstance().getCurrentUser().getFx_level();
        if ("2".equals(fxLevel)) {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_2);
        } else if ("1".equals(fxLevel)) {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_1);
        }else {
            ivRank.setImageResource(R.drawable.ic_represent_incom_rank_1);
        }
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    public void bindData() {
        if (resultCommissionLog == null) {
            return;
        }
        PageBean pageModel = (PageBean) resultCommissionLog.getPage();
        if (pageModel != null) {
            pageNum = Integer.valueOf(pageModel.getPage());
            maxPage = Integer.valueOf(pageModel.getPage_total());
        }
        if (pageNum == 1) {
            titleList = null;
            mDatas.clear();
        }
        items = resultCommissionLog.getList();
        if (!SDCollectionUtil.isEmpty(items)) {
            for (ModelCommissionLog bean : items) {
                String time = DateFormat.format("HH:mm:ss", DataFormat.toLong(bean.getInsert_time())).toString();
                String month_date = DateFormat.format("MM-dd", DataFormat.toLong(bean.getInsert_time())).toString();
                if (!containTitle(bean.getInsert_time())) {
                    //添加TITLE实体。
                    ModelCommissionLog temp = new ModelCommissionLog();
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
            mRepresentIncomeAdapter.setmDatas(mDatas);
        } else {
            mRepresentIncomeAdapter.addMoreMDatas(mDatas);
        }
        showLLEmpty();
        mRepresentIncomeAdapter.notifyDataSetChanged();
        pageNum++;
    }

    List<ModelCommissionLog> items;

    @Override
    public void onSuccess(String method, List datas) {
        if (CommissionConstance.USER_COMMISSION_LOG.equals(method)) {
            if (SDCollectionUtil.isEmpty(datas)) {
                return;
            }
            resultCommissionLog = (ResultCommissionLog) datas.get(0);
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SDViewBinder.setTextView(tvMoney, resultCommissionLog.getSalary_total(), "0");
                    bindData();
                }
            });
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
