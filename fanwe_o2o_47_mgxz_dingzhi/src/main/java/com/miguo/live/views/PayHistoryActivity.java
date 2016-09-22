/*
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miguo.live.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fanwe.BaseActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.miguo.live.adapters.PayHistoryAdapter;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.payHistory.ModelPayHistory;
import com.miguo.live.presenters.LiveHttpHelper;

import java.util.ArrayList;
import java.util.List;

public class PayHistoryActivity extends BaseActivity implements CallbackView2 {
    private Context mContext = PayHistoryActivity.this;
    private PullToRefreshPinnedSectionListView mPTR;
    private List<ModelPayHistory> datas = new ArrayList<>();
    private PayHistoryAdapter mPayHistoryAdapter;
    private LiveHttpHelper liveHttpHelper;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_pay_history);
        preWidget();
        preData();
        mTitle.setMiddleTextTop("充值记录");
    }

    private void getData() {
        if (liveHttpHelper == null) {
            liveHttpHelper = new LiveHttpHelper(this, this, "");
        }
        liveHttpHelper.getRechargeDiamondList(pageNum, pageSize);
    }

    private void preData() {
        datas.clear();
        mPayHistoryAdapter = new PayHistoryAdapter(mContext, getLayoutInflater(), datas);
        mPTR.setAdapter(mPayHistoryAdapter);
    }

    private void preWidget() {
        mPTR = (PullToRefreshPinnedSectionListView) findViewById(R.id.list);
        mPTR.setMode(PullToRefreshBase.Mode.BOTH);
        mPTR.setOnRefreshListener(mOnRefresherListener2);
        mPTR.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long positionL) {
                position--;
                ModelPayHistory item = datas.get(position);
//                MGToast.showToast("Item " + position + ": " + item.getDate());
            }
        });
        mPTR.setRefreshing();
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

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelPayHistory> items;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (LiveConstants.RECHARGE_DIAMOND_LIST.equals(method)) {
            items = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        datas.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        for (ModelPayHistory bean : items) {
                            if (!containDate(bean.getDate())) {
                                ModelPayHistory temp = new ModelPayHistory();
                                temp.setType(1);
                                temp.setDate(bean.getDate());
                                datas.add(temp);
                                datas.add(bean);
                            } else {
                                datas.add(bean);
                            }
                        }
                    }
                    mPayHistoryAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mPTR.onRefreshComplete();
                    break;
            }
        }
    };

    /**
     * 是否包含当天的title
     *
     * @param date
     * @return
     */
    private boolean containDate(String date) {
        if (!SDCollectionUtil.isEmpty(datas)) {
            for (ModelPayHistory bean : datas) {
                if (1 == bean.getType()) {
                    if (date.equals(bean.getDate())) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
}