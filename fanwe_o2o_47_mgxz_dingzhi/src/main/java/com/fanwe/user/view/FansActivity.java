package com.fanwe.user.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.adapters.FansAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FansActivity extends Activity {

    private PullToRefreshListView mPtrlv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);

        initPullToRefreshListView();
    }

    private void initPullToRefreshListView() {
        mPtrlv_content= (PullToRefreshListView) findViewById(R.id.ptrlv_content);
        FansAdapter mAdapter=new FansAdapter();
        mPtrlv_content.setAdapter(mAdapter);
        mPtrlv_content.setMode(PullToRefreshBase.Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPtrlv_content.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPtrlv_content.onRefreshComplete();
            }
        });

    }
}
