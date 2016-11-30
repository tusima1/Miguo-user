package com.fanwe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.adapter.MyXiaoMiDetailAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.Member;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getMyDistributionCorps.ModelMyDistributionCorps;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级下线
 *
 * @author cxk
 */
public class MyXiaomiDetailActivity extends BaseActivity implements CallbackView {

    @ViewInject(R.id.frag_my_detail)
    private PullToRefreshScrollView mPtrsvAll;

    @ViewInject(R.id.listView_detail)
    private SDListViewInScroll mListView;

    @ViewInject(R.id.ll_empty)
    private LinearLayout mLl_empty;

    @ViewInject(R.id.tv_userName)
    private TextView mTv_user;

    @ViewInject(R.id.tv_userNumber)
    private TextView mTv_number;

    private List<Member> listMember = new ArrayList<Member>();
    private String user_id;
    private String user_name;

    private MyXiaoMiDetailAdapter mAdapter;

    private int mType = 1;
    private String mRank;
    private int pageNum = 1;
    private int pageSize = 10;
    private UserHttpHelper userHttpHelper;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_xiaomi_detail);
        init();
    }

    private void init() {
        initIntent();
        initTitle();
        initDefaultData();
        initPullToScrollView();
    }

    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(this, this);
        }
        userHttpHelper.getMyDistributionCorps(mType + "", mRank, pageNum, pageSize, user_id);
    }

    private ResultMyDistributionCorps currResultMyDistributionCorps;

    private void convertList() {
        if (isRefresh)
            listMember.clear();
        if (!SDCollectionUtil.isEmpty(currResultMyDistributionCorps.getList())) {
            for (ModelMyDistributionCorps bean : currResultMyDistributionCorps.getList()) {
                Member member = new Member();
                member.setId(bean.getUser_id());
                member.setUid(bean.getUser_id());
                member.setAvatar(bean.getIcon());
                member.setCreate_time(bean.getFx_time());
                member.setMobile(bean.getMobile());
                member.setRank(DataFormat.toInt(bean.getFx_level()));
                member.setSalary(bean.getFx_total_commission());
                member.setUser_name(bean.getNick());
                member.setUser_num(DataFormat.toInt(bean.getUser_num()));

                listMember.add(member);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initDefaultData() {
        mAdapter = new MyXiaoMiDetailAdapter(listMember, this);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mLl_empty);
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("id");
        if (TextUtils.isEmpty(user_id)) {
            finish();
        }
        user_name = bundle.getString("user");
        mTv_user.setText(user_name + "（");
        int user_num = bundle.getInt("number");
        mTv_number.setText(String.valueOf(user_num));
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("我的小米");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextTop("关闭");
    }

    private void initPullToScrollView() {
        mPtrsvAll.setMode(Mode.BOTH);
        mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(results)) {
                    pageNum++;
                }
                getData();
            }
        });
        mPtrsvAll.setRefreshing();
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        SDActivityManager.getInstance().finishActivity(
                DistributionMyXiaoMiActivity.class);
        finish();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ResultMyDistributionCorps> results;
    ResultMyDistributionCorps currResult;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.MY_DISTRIBUTION_CROPS.equals(method)) {
            results = datas;
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
                    if (!SDCollectionUtil.isEmpty(results)) {
                        currResult = results.get(0);
                        currResultMyDistributionCorps = currResult;
                        convertList();
                        mPtrsvAll.onRefreshComplete();
                    }
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}
