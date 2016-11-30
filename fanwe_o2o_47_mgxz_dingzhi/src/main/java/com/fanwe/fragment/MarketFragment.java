package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fanwe.ShoppingMallActivity;
import com.fanwe.adapter.DistributionMarketAdapter;
import com.fanwe.adapter.DistributionMarketCatePageAdapter;
import com.fanwe.adapter.DistributionMarketCatePageAdapter.OnClickCateItemListener;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.customview.app.DistributionMarketCateView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.Supplier_fx;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getMarketList.ModelMarketListItem;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends BaseFragment implements CallbackView {
    /**
     * 要搜索的商品的id (int)
     */
    public static final String EXTRA_ID = "extra_id";
    /**
     * 商品分类id (int)
     */
    public static final String EXTRA_CATE_ID = "extra_cate_id";
    /**
     * 要搜索的商品的关键字 (String)
     */
    public static final String EXTRA_KEY_WORD = "extra_key_word";
    public static final int CITY_RESULT = 10086;

    @ViewInject(R.id.et_search)
    private EditText mEt_search;

    @ViewInject(R.id.ptrlv_content)
    public PullToRefreshListView mPtrlv_content;

    @ViewInject(R.id.ll_tip_text)
    private LinearLayout ll_tipTextLayout;//tip

    @ViewInject(R.id.ll_empty)
    private RelativeLayout ll_emptyLayout;//无数据展示的空白页

    private DistributionMarketCateView mCateView;
    private boolean mNeedBindCate = true;

    private List<Supplier_fx> mListModel = new ArrayList<Supplier_fx>();
    private DistributionMarketAdapter mAdapter;

    private PageModel mPage = new PageModel();

    private String mId;
    private String mCate_id;

    private String city_id = "";//城市id
    private MarketTitleFragment marketTitleFragment;
    private SellerHttpHelper sellerHttpHelper;
    private CommonHttpHelper commonHttpHelper;
    private int pageNum = 1;
    private int pageSize = 10;
    private String keyword;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_market);
    }

    @Override
    protected void init() {
        super.init();
        getHomeClassify();
        initView();
        initFragment();
        getIntentData();
        registerClick();
        bindDefaultData();
        addHeaderView();
        initPullToRefreshListView();

        getMarketList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            if (mPtrlv_content != null) {
                mPtrlv_content.setRefreshing();
            }
        }
    }

    private void initView() {

    }

    private void getHomeClassify() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(getActivity(), this);
        }
        commonHttpHelper.getHomeClassifyList();
    }

    private void initFragment() {
        marketTitleFragment = new MarketTitleFragment();
        getSDFragmentManager().replace(R.id.frag_market_titleBar, marketTitleFragment);

    }

    private void addHeaderView() {
        mCateView = new DistributionMarketCateView(getActivity());
        SDViewUtil.hide(mCateView);
        mPtrlv_content.getRefreshableView().addHeaderView(mCateView);
    }

    private void getIntentData() {
        mId = getArguments().getString(EXTRA_ID);
        mCate_id = getArguments().getString(EXTRA_CATE_ID);
        keyword = getArguments().getString(EXTRA_KEY_WORD);
        if (!isEmpty(keyword)) {
            mEt_search.setText(keyword);
        } else {
            keyword = "";
        }
    }


    private void registerClick() {
        mEt_search.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SDViewUtil.hideInputMethod(mEt_search, getActivity());
                    keyword = mEt_search.getText().toString();

                    isRefresh = true;
                    pageNum = 1;
                    getMarketList();
                    return true;
                }
                return false;
            }
        });
        mEt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString().trim();
                if (keyword.length() > 0) {
                    ll_tipTextLayout.setVisibility(View.GONE);
                } else {
                    ll_tipTextLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void bindDefaultData() {
        city_id = AppRuntimeWorker.getCity_id();
        mAdapter = new DistributionMarketAdapter(mListModel, getActivity());
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageNum = 1;

                mListModel.clear();
                mAdapter.notifyDataSetChanged();

                getMarketList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(items)) {
                    pageNum++;
                }
                getMarketList();
            }
        });
        mPtrlv_content.setRefreshing();
    }

    private void getMarketList() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(getActivity(), this, "");
        }
        sellerHttpHelper.getMarketList(pageNum, pageSize, mCate_id, keyword, AppRuntimeWorker.getCity_id());
    }


    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                isRefresh = true;
                getMarketList();
                break;
            case DELETE_DISTRIBUTION_GOODS_SUCCESS:
                isRefresh = true;
                getMarketList();
                break;
            case ADD_DISTRIBUTION_GOODS_SUCCESS:
                isRefresh = true;
                getMarketList();
                break;
            default:
                break;
        }
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    public void setCityIdTitle(String cityName, String cityId) {
        marketTitleFragment.setShowCity(cityName);
        city_id = cityId;
        getMarketList();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ModelMarketListItem> items;
    private boolean isRefresh;
    List<ModelHomeClassifyList> itemsHomeClassify = new ArrayList<>();

    @Override
    public void onSuccess(String method, List datas) {
        if (SellerConstants.MARKET_LIST.equals(method)) {
            items = datas;
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        } else if (CommonConstants.HOME_CLASSIFY_LIST.equals(method)) {
            itemsHomeClassify = datas;
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        Message message = new Message();
        message.what = 2;
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        mListModel.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        for (ModelMarketListItem bean : items) {
                            Supplier_fx supplier_fx = new Supplier_fx();
                            supplier_fx.setId(bean.getId());
                            supplier_fx.setPreview(bean.getEnt_logo());
                            supplier_fx.setBuy_count(Integer.valueOf(bean.getRq()));
                            supplier_fx.setName(bean.getBuss_name());
                            supplier_fx.setIs_delete(Integer.valueOf(bean.getIs_delete()));
                            mListModel.add(supplier_fx);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        ll_emptyLayout.setVisibility(View.VISIBLE);
                    } else {
                        ll_emptyLayout.setVisibility(View.GONE);
                    }
                    mPtrlv_content.onRefreshComplete();
                    break;
                case 1:
                    // 分类
                    initSlidingPlayView();
                    //小红点
                    if (itemsHomeClassify.size() > 5) {
                        SDViewUtil.setViewMarginBottom(mCateView.mSpv_content.mVpgContent, SDViewUtil.dp2px(20));
                    }
                    List<List<ModelHomeClassifyList>> listModel = SDCollectionUtil.splitList(itemsHomeClassify, 5);
                    if (isEmpty(listModel)) {
                        SDViewUtil.hide(mCateView);
                    } else {
                        SDViewUtil.show(mCateView);
                        DistributionMarketCatePageAdapter adapter = new DistributionMarketCatePageAdapter(listModel, getActivity());
                        adapter.setmListenerOnClickCateItem(new OnClickCateItemListener() {
                            @Override
                            public void onClickItem(int position, View view, ModelHomeClassifyList model) {
                                mCate_id = model.getId();
                                if (TextUtils.isEmpty(mCate_id)) {
                                    Intent intent = new Intent(getActivity(), ShoppingMallActivity.class);
                                    startActivity(intent);
                                } else {
                                    mCateView.mNow_Selected.setText(model.getName());
                                    mPtrlv_content.setRefreshing();
                                }
                            }
                        });
                        mCateView.mSpv_content.setAdapter(adapter);
                    }
                    break;
                case 2:
                    mPtrlv_content.onRefreshComplete();
                    break;
            }
        }

    };

    private void initSlidingPlayView() {
        mCateView.mSpv_content.setmImageNormalResId(R.drawable.ic_small_dot_normal);
        mCateView.mSpv_content.setmImageSelectedResId(R.drawable.ic_small_dot_slecet);
    }
}
