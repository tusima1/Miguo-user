package com.fanwe.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.AttentionListAdapter;
import com.fanwe.user.model.getAttentionFocus.ModelAttentionFocus;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注列表
 * Created by Administrator on 2016/9/12.
 */
public class AttentionListActivity extends Activity implements CallbackView {
    private Context mContext = AttentionListActivity.this;
    private UserHttpHelper userHttpHelper;
    private PullToRefreshListView ptrl;
    private List<ModelAttentionFocus> datas = new ArrayList<>();
    private AttentionListAdapter mAttentionListAdapter;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private RelativeLayout ll_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_attention_list);
        initTitle();
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
        preData();
        ll_empty = (RelativeLayout)findViewById(R.id.ll_empty);
    }

    public void showEmpty(boolean show){
        if(show) {
            ll_empty.setVisibility(View.VISIBLE);
        }else{
            ll_empty.setVisibility(View.GONE);
        }
    }

    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("我的关注");
    }

    private void getData() {
        userHttpHelper.getAttentionFocus(pageNum, pageSize);
    }

    private void preData() {
        mAttentionListAdapter = new AttentionListAdapter(mContext, getLayoutInflater(), datas);
        ptrl.setAdapter(mAttentionListAdapter);
    }

    private void setListener() {
    }

    private void preWidget() {
        ptrl = (PullToRefreshListView) findViewById(R.id.ptrl_act_attention);
        ptrl.setMode(PullToRefreshBase.Mode.BOTH);
        ptrl.setOnRefreshListener(mOnRefresherListener2);
        ptrl.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long positionL) {
                position--;
                ModelAttentionFocus item = datas.get(position);
                Intent intent = new Intent(AttentionListActivity.this, UserHomeActivity.class);
                intent.putExtra("id", item.getFocus_user_id());
                startActivity(intent);
            }
        });
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
    protected void onResume() {
        super.onResume();
        ptrl.setRefreshing();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelAttentionFocus> items;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.ATTENTION_FOCUS.equals(method)) {
            items = datas;
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
                    if (isRefresh) {
                        datas.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        datas.addAll(items);

                    }
                    if(datas==null||datas.size()<1){
                        showEmpty(true);
                    }else{
                        showEmpty(false);
                    }
                    mAttentionListAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    ptrl.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }
}
