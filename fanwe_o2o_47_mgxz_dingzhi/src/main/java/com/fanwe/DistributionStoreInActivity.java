package com.fanwe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.fanwe.adapter.StoreInAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.StoreIn_Fx;
import com.fanwe.model.StoreIn_list;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getCroupBuyByMerchant.ModelCroupBuyByMerchant;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class DistributionStoreInActivity extends BaseActivity implements CallbackView {
    @ViewInject(R.id.iv_user_avatar)
    private CircularImageView mIv_avatar;

    @ViewInject(R.id.tv_name)
    private TextView mTv_name;

    @ViewInject(R.id.tv_number)
    private TextView mTv_number;

    @ViewInject(R.id.pull_scroll)
    private PullToRefreshGridView store_grid;

    private List<StoreIn_list> listModel = new ArrayList<>();

    private StoreInAdapter mAdapter;
    private PageModel page = new PageModel();
    private String mId;
    protected StoreIn_Fx mActModel;
    private SellerHttpHelper sellerHttpHelper;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_in_store);
        init();
    }

    private void getData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
        mId = "160160c0-262c-11e6-98e2-6c92bf2c1775";
        sellerHttpHelper.getCroupBuyByMerchant(pageNum, pageSize, mId);
    }

    private void init() {
        initDefaultAdapter();
        initTitle();
        initPullToRefreshScrollView();
    }

    private void initPullToRefreshScrollView() {
        store_grid.setMode(Mode.BOTH);
        store_grid.setOnRefreshListener(new OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(items)) {
                    pageNum++;
                }
                getData();
            }
        });
        store_grid.setRefreshing();
    }

    private void requestData(final boolean isLoadMore) {
        RequestModel model = new RequestModel();
        model.putCtl("uc_fx");
        model.putAct("deal_fx_list");
        model.put("id", mId);
        model.putPage(page.getPage());
        SDRequestCallBack<StoreIn_Fx> handler = new SDRequestCallBack<StoreIn_Fx>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    mActModel = actModel;
                    page.update(actModel.getPage());
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), mAdapter, isLoadMore);
                }
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
                store_grid.onRefreshComplete();
            }
        };
        InterfaceServer.getInstance().requestInterface(HttpMethod.POST, model, null, false, handler);
    }

    private void initDefaultAdapter() {
        mAdapter = new StoreInAdapter(listModel, this);
        store_grid.setAdapter(mAdapter);
    }

    private void initTitle() {
        Bundle bundle = getIntent().getExtras();
        mId = bundle.getString("id");
        if (TextUtils.isEmpty(mId)) {
            finish();
        }
        mTitle.setMiddleTextTop(bundle.getString("name"));
        mTitle.initRightItem(0);
        SDViewBinder.setImageView(bundle.getString("img"), mIv_avatar);
        SDViewBinder.setTextView(mTv_name, bundle.getString("name"));
        SDViewBinder.setTextView(mTv_number, String.valueOf(bundle.getInt("count")));
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelCroupBuyByMerchant> items;

    @Override
    public void onSuccess(String method, List datas) {
        if (SellerConstants.GROUP_BUY_BY_MERCHANT.equals(method)) {
            items = datas;
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        listModel.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        for (ModelCroupBuyByMerchant bean : items) {
                            StoreIn_list storeIn_list = new StoreIn_list();
                            storeIn_list.setId(bean.getId());
                            storeIn_list.setName(bean.getName());
                            storeIn_list.setImg(bean.getImg());
                            storeIn_list.setCurrent_price(Float.valueOf(bean.getTuan_price()));
                            storeIn_list.setOrigin_price(Float.valueOf(bean.getOrigin_price()));
                            storeIn_list.setSalary(Float.valueOf(bean.getTuan_price()) - Float.valueOf(bean.getBalance_price()));
                            listModel.add(storeIn_list);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    store_grid.onRefreshComplete();
                    break;
            }
        }
    };
}
