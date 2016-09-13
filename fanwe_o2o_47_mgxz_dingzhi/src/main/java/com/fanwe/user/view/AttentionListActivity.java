package com.fanwe.user.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fanwe.BaseActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.AttentionListAdapter;
import com.fanwe.user.model.getAttentionFocus.ModelAttentionFocus;
import com.fanwe.user.presents.UserHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miguo.live.views.customviews.MGToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注列表
 * Created by Administrator on 2016/9/12.
 */
public class AttentionListActivity extends BaseActivity implements CallbackView2 {
    private Context mContext = AttentionListActivity.this;
    private UserHttpHelper userHttpHelper;
    private PullToRefreshListView ptrl;
    private List<ModelAttentionFocus> datas = new ArrayList<>();
    private AttentionListAdapter mAttentionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_attention_list);
        mTitle.setMiddleTextTop("我的关注");
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
        preData();
    }

    private void preData() {
        for (int i = 0; i < 20; i++) {
            ModelAttentionFocus bean = new ModelAttentionFocus();
            bean.setNick("昵称：" + i);
            datas.add(bean);
        }
        mAttentionListAdapter = new AttentionListAdapter(mContext, getLayoutInflater(), datas);
        ptrl.setAdapter(mAttentionListAdapter);
    }

    private void setListener() {
    }

    private void preWidget() {
        ptrl = (PullToRefreshListView) findViewById(R.id.ptrl_act_attention);
        ptrl.setMode(PullToRefreshBase.Mode.BOTH);
//        ptrl.setOnRefreshListener(mOnRefresherListener2);
        ptrl.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long positionL) {
                position--;
                ModelAttentionFocus item = datas.get(position);
//                SDToast.showToast("Item " + position + ": " + item.getDate());
            }
        });
//        ptrl.setRefreshing();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.ADVICE.equals(method)) {
            MGToast.showToast("提交成功");
            finish();
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}
