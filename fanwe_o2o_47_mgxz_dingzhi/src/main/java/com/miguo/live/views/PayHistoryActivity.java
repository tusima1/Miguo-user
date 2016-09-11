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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.miguo.live.adapters.PayHistoryAdapter;
import com.miguo.live.model.payHistory.ModelPayHistory;
import com.miguo.live.presenters.LiveHttpHelper;

import java.util.ArrayList;
import java.util.List;

public class PayHistoryActivity extends Activity implements CallbackView2 {
    private Context mContext = PayHistoryActivity.this;
    private PullToRefreshPinnedSectionListView mPTR;
    private List<ModelPayHistory> datas = new ArrayList<>();
    private PayHistoryAdapter mPayHistoryAdapter;
    private LiveHttpHelper liveHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_history);
        preWidget();
        preData();
        getData();
    }

    private void getData() {
        liveHttpHelper = new LiveHttpHelper(this, this,"");
        liveHttpHelper.getRechargeDiamondList();
    }

    private void preData() {
        datas.clear();
        ModelPayHistory model;

        model = new ModelPayHistory();
        model.setType(1);
        model.setDate("2016.9.11");
        datas.add(model);
        for (int i = 1; i < 9; i++) {
            model = new ModelPayHistory();
            model.setPrice(i + ".00");
            model.setDiamond_count(i * 5 + "");
            model.setOrder_id("21216546465" + i);
            model.setStatus("1");
            model.setDate("2016.9.11");
            model.setTime("12:24:56");
            datas.add(model);
        }

        model = new ModelPayHistory();
        model.setType(1);
        model.setDate("2016.10.26");
        datas.add(model);
        for (int i = 3; i < 12; i++) {
            model = new ModelPayHistory();
            model.setPrice(i + ".00");
            model.setDiamond_count(i * 5 + "");
            model.setOrder_id("21216546489" + i);
            model.setStatus("1");
            model.setDate("2016.10.26");
            model.setTime("12:24:56");
            datas.add(model);
        }

        model = new ModelPayHistory();
        model.setType(1);
        model.setDate("2016.11.21");
        datas.add(model);
        for (int i = 5; i < 16; i++) {
            model = new ModelPayHistory();
            model.setPrice(i + ".00");
            model.setDiamond_count(i * 5 + "");
            model.setOrder_id("21216546426" + i);
            model.setStatus("0");
            model.setDate("2016.11.21");
            model.setTime("12:24:56");
            datas.add(model);
        }

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
                SDToast.showToast("Item " + position + ": " + item.getDate());
            }
        });
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mOnRefresherListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(
                PullToRefreshBase<ListView> refreshView) {
            String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

            //TODO Do work to refresh the list here.
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PayHistoryActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // Stop refreshing
                            mPTR.onRefreshComplete();
                        }
                    });

                }
            }).start();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PayHistoryActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // Stop refreshing
                            mPTR.onRefreshComplete();
                        }
                    });

                }
            }).start();
        }
    };

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}