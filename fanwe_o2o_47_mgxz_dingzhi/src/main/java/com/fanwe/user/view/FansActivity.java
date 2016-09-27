package com.fanwe.user.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.adapters.FansAdapter;
import com.fanwe.user.model.getAttentionFans.ModelFans;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class FansActivity extends Activity implements CallbackView2 {

    private PullToRefreshListView mPtrlv_content;

    private List<ModelFans> mFansList=new ArrayList();

    private int mPage=1;//当前页面
    private FansAdapter mAdapter;
    private UserHttpHelper httpHelper;
    private RelativeLayout ll_empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        initTitle();
        initPullToRefreshListView();

        httpHelper = new UserHttpHelper(null,this);
        httpHelper.getAttentionFans(1,10);
        ll_empty = (RelativeLayout)findViewById(R.id.ll_empty);

    }
    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("我的粉丝");
    }
    public void showEmpty(boolean show){
        if(show) {
            ll_empty.setVisibility(View.VISIBLE);
        }else{
            ll_empty.setVisibility(View.GONE);
        }
    }

    private void initPullToRefreshListView() {
        mPtrlv_content= (PullToRefreshListView) findViewById(R.id.ptrlv_content);
        mAdapter = new FansAdapter();
        mPtrlv_content.setAdapter(mAdapter);
        mPtrlv_content.setMode(PullToRefreshBase.Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage=1;
                mFansList.clear();
                httpHelper.getAttentionFans(mPage,10);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                httpHelper.getAttentionFans(mPage,10);
            }
        });

    }



    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (datas!=null ){
            mFansList.addAll(datas);
            mAdapter.setData(mFansList);
                mPage++;
            if(mFansList==null||mFansList.size()<1){
                showEmpty(true);
            }else{
                showEmpty(false);
            }
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        mPtrlv_content.onRefreshComplete();
    }
}
