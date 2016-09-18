package com.fanwe.user.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.adapters.FansAdapter;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

public class FansActivity extends Activity implements CallbackView2 {

    private PullToRefreshListView mPtrlv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);

        initPullToRefreshListView();

        UserHttpHelper httpHelper=new UserHttpHelper(null,this);
        httpHelper.getAttentionFans(1,10);
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
