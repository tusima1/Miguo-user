package com.fanwe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.adapter.CategoryCateLeftAdapter;
import com.fanwe.adapter.CategoryCateRightAdapter;
import com.fanwe.adapter.CategoryOrderAdapter;
import com.fanwe.adapter.CategoryQuanLeftAdapter;
import com.fanwe.adapter.CategoryQuanRightAdapter;
import com.fanwe.adapter.MerchantListAdapter;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.customview.SD2LvCategoryView;
import com.fanwe.library.customview.SDLvCategoryView;
import com.fanwe.library.customview.SDLvCategoryView.SDLvCategoryViewListener;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getBusinessCircleList.ModelBusinessCircleList;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.seller.model.getClassifyList.ModelClassifyList;
import com.fanwe.seller.model.getShopList.ModelShopListNavs;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

public class StoreListFragment extends BaseFragment implements CallbackView {
    //widget
    private SD2LvCategoryView mCvLeft;
    private SD2LvCategoryView mCvMiddle;
    private SDLvCategoryView mCvRight;
    private LinearLayout mLlEmpty;
    private LinearLayout mLlCurrentLocation;
    private LinearLayout mLlCurrentSearch;
    private TextView mTvAddress;
    private ImageView mIvLocation;
    private TextView mTvCurrentKeyword;
    private PullToRefreshListView mPtrlvContent;
    private ImageView ivRepresent;
    // 大分类id(int)
    public static final String EXTRA_CATE_ID = "extra_cate_id";
    //小分类id(int)
    public static final String EXTRA_TID = "extra_tid";
    // 商圈id(int)
    public static final String EXTRA_QID = "extra_qid";
    //关键字(String)
    public static final String EXTRA_KEY_WORD = "extra_key_word";
    //门店类型(int) 1:优惠，0:全部
    public static final String EXTRA_STORE_TYPE = "extra_store_type";
    // =======================提交到服务器参数
    //大分类id
    private String cate_id;
    //小分类id
    private String tid;
    // 关键词
    private String keyword;
    //商圈id
    private String qid;
    //排序类型
    private String order_type = "default";
    // 米果定制
    private String store_type;
    // 大区id
    private String pid;
    private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();
    //可代言flag
    int iRepresent = 0;
    private int pageNum = 1;
    private int pageSize = 10;
    private SellerHttpHelper sellerHttpHelper;
    private MerchantListAdapter mAdapter = null;
    private List<ModelBusinessListings> mListModel = new ArrayList<>();

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_seller_list);
    }

    @Override
    protected void init() {
        super.init();
        preWidget();
        setListener();
        getIntentData();
        bindDefaultLvData();
        bindLocationData();
        initCategoryView();
        initCategoryViewNavigatorManager();
        initPullRefreshLv();
        initData();
    }

    private void setListener() {
        mIvLocation.setOnClickListener(this);
        ivRepresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRepresent == 1) {
                    iRepresent = 0;
                } else {
                    iRepresent = 1;
                }
                isRefresh = true;
                pageNum = 1;
                setRepresent();
                getShopList();
            }
        });
    }

    private void preWidget() {
        mCvLeft = (SD2LvCategoryView) findViewById(R.id.lcv_left);
        mCvMiddle = (SD2LvCategoryView) findViewById(R.id.lcv_middle);
        mCvRight = (SDLvCategoryView) findViewById(R.id.lcv_right);
        ivRepresent = (ImageView) findViewById(R.id.iv_represent);
        mLlEmpty = (LinearLayout) findViewById(R.id.ll_empty);
        mLlCurrentLocation = (LinearLayout) findViewById(R.id.ll_current_location);
        mLlCurrentSearch = (LinearLayout) findViewById(R.id.ll_current_search);
        mTvAddress = (TextView) findViewById(R.id.tv_current_address);
        mTvCurrentKeyword = (TextView) findViewById(R.id.tv_current_keyword);
        mIvLocation = (ImageView) findViewById(R.id.iv_location);
        mPtrlvContent = (PullToRefreshListView) findViewById(R.id.ptrlv_content);
    }

    private void setRepresent() {
        if (iRepresent == 1) {
            ivRepresent.setImageResource(R.drawable.ic_represent_selected);
        } else {
            ivRepresent.setImageResource(R.drawable.ic_represent_normal);
        }
    }

    private void getShopList() {
        sellerHttpHelper.getBusinessListings(tid, cate_id, AppRuntimeWorker.getCity_id(), order_type, pid, store_type, qid, keyword, pageNum, pageSize, String.valueOf(iRepresent));
    }

    private void initData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(getActivity(), this);
        }
        //商圈
        if (SDCollectionUtil.isEmpty(App.getInstance().modelBusinessCircleLists)) {
            sellerHttpHelper.getBusinessCircleList(AppRuntimeWorker.getCity_id());
        } else {
            modelBusinessCircleLists = App.getInstance().modelBusinessCircleLists;
            bindMiddleCategoryViewData(modelBusinessCircleLists);
        }
        //类别
        if (SDCollectionUtil.isEmpty(App.getInstance().modelClassifyLists)) {
            sellerHttpHelper.getClassifyList();
        } else {
            modelClassifyLists = App.getInstance().modelClassifyLists;
            bindLeftCategoryViewData(modelClassifyLists);
        }
        //排序
        if (SDCollectionUtil.isEmpty(App.getInstance().navs)) {
            sellerHttpHelper.getOrderByList();
        } else {
            navs = App.getInstance().navs;
            bindRightCategoryViewData(navs);
        }
    }

    private void bindLocationData() {
        String addrShort = BaiduMapManager.getInstance().getCurAddressShort();
        if (TextUtils.isEmpty(addrShort)) {
            locationAddress();
        }
    }

    public void scrollToTop() {
        if (isResumed() && mPtrlvContent != null && mAdapter != null && mAdapter.getCount() > 0) {
            mPtrlvContent.getRefreshableView().setSelection(0);
        }
    }

    private void getIntentData() {
        if (getArguments() != null) {
            cate_id = getArguments().getString(EXTRA_CATE_ID);
            tid = getArguments().getString(EXTRA_TID);
            qid = getArguments().getString(EXTRA_QID);
            keyword = getArguments().getString(EXTRA_KEY_WORD);
            store_type = getArguments().getString(EXTRA_STORE_TYPE);
        }
        if (TextUtils.isEmpty(keyword)) {
            mLlCurrentLocation.setVisibility(View.VISIBLE);
            mLlCurrentSearch.setVisibility(View.GONE);
            if (BaiduMapManager.getInstance().getCurAddress() != null) {
                mTvAddress.setText(BaiduMapManager.getInstance().getCurAddressShort());
            }
        } else {
            mLlCurrentLocation.setVisibility(View.GONE);
            mLlCurrentSearch.setVisibility(View.VISIBLE);
            mTvCurrentKeyword.setText(keyword);
        }
    }

    private boolean isRefresh;

    private void initPullRefreshLv() {
        mPtrlvContent.setMode(Mode.BOTH);
        mPtrlvContent.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNum = 1;
                isRefresh = true;
                getShopList();
                if (SDCollectionUtil.isEmpty(modelBusinessCircleLists) || SDCollectionUtil.isEmpty(modelClassifyLists) || SDCollectionUtil.isEmpty(navs)) {
                    //获取类别等信息
                    initData();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(itemsModelBusinessListings)) {
                    pageNum++;
                }
                getShopList();
            }
        });
        mPtrlvContent.setRefreshing();
    }

    private void bindDefaultLvData() {
        mAdapter = new MerchantListAdapter(mListModel, getActivity());
        mPtrlvContent.setAdapter(mAdapter);
    }

    private void initCategoryViewNavigatorManager() {
        SDViewBase[] items = new SDViewBase[]{mCvLeft, mCvMiddle, mCvRight};
        mViewManager.setItems(items);
        mViewManager.setmMode(SDViewNavigatorManager.Mode.CAN_NONE_SELECT);
    }

    private void initCategoryView() {
        mCvLeft.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_down);
        mCvLeft.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_up);

        mCvLeft.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvLeft.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvLeft.setmListener(new SD2LvCategoryView.SD2LvCategoryViewListener() {

            @Override
            public void onRightItemSelect(int leftIndex, int rightIndex, Object leftModel, Object rightModel) {
                ModelClassifyList left = (ModelClassifyList) leftModel;
                ModelClassifyList right = (ModelClassifyList) rightModel;
                cate_id = left.getId();
                tid = right.getId();
                mPtrlvContent.setRefreshing();
            }

            @Override
            public void onLeftItemSelect(int leftIndex, Object leftModel, boolean isNotifyDirect) {
                if (isNotifyDirect) {
                    ModelClassifyList left = (ModelClassifyList) leftModel;
                    ModelClassifyList right = SDCollectionUtil.get(left.getBcate_type(), 0);
                    cate_id = left.getId();
                    if (right != null) {
                        tid = right.getId();
                    } else {
                        tid = "";
                    }
                    mPtrlvContent.setRefreshing();
                }
            }
        });

        mCvMiddle.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_down);
        mCvMiddle.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_up);

        mCvMiddle.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvMiddle.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvMiddle.setmListener(new SD2LvCategoryView.SD2LvCategoryViewListener() {

            @Override
            public void onRightItemSelect(int leftIndex, int rightIndex, Object leftModel, Object rightModel) {
                ModelBusinessCircleList right = (ModelBusinessCircleList) rightModel;
                qid = right.getId();
                mPtrlvContent.setRefreshing();
            }

            @Override
            public void onLeftItemSelect(int leftIndex, Object leftModel, boolean isNotifyDirect) {
                if (isNotifyDirect) {
                    ModelBusinessCircleList left = (ModelBusinessCircleList) leftModel;
                    ModelBusinessCircleList right = SDCollectionUtil.get(left.getQuan_sub(), 0);
                    if (right != null) {
                        qid = right.getId();
                    }
                    if (TextUtils.isEmpty(qid)) {
                        qid = left.getId();
                    }
                    mPtrlvContent.setRefreshing();
                }
            }
        });

        mCvRight.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_down);
        mCvRight.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_up);

        mCvRight.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvRight.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvRight.setmListener(new SDLvCategoryViewListener() {
            @Override
            public void onItemSelect(int index, Object model) {
                if (model instanceof ModelShopListNavs) {
                    ModelShopListNavs orderModel = (ModelShopListNavs) model;
                    order_type = orderModel.getCode();
                    mPtrlvContent.setRefreshing();
                }
            }
        });
    }

    private void bindLeftCategoryViewData(List<ModelClassifyList> listModel) {
        if (!SDCollectionUtil.isEmpty(listModel)) {
            int[] arrIndex = ModelClassifyList.findIndex(cate_id, tid, listModel);
            int leftIndex = arrIndex[0];
            int rightIndex = arrIndex[1];

            ModelClassifyList leftModel = listModel.get(leftIndex);
            List<ModelClassifyList> listRight = leftModel.getBcate_type();

            CategoryCateLeftAdapter adapterLeft = new CategoryCateLeftAdapter(listModel, getActivity());
            adapterLeft.setmDefaultIndex(leftIndex);

            CategoryCateRightAdapter adapterRight = new CategoryCateRightAdapter(listRight, getActivity());
            adapterRight.setmDefaultIndex(rightIndex);

            mCvLeft.setLeftAdapter(adapterLeft);
            mCvLeft.setRightAdapter(adapterRight);
            mCvLeft.setAdapterFinish();
        }
    }

    private void bindMiddleCategoryViewData(List<ModelBusinessCircleList> listModel) {
        if (!SDCollectionUtil.isEmpty(listModel)) {
            int[] arrIndex = ModelBusinessCircleList.findIndex(qid, listModel);
            int leftIndex = arrIndex[0];
            int rightIndex = arrIndex[1];

            ModelBusinessCircleList leftModel = listModel.get(leftIndex);
            List<ModelBusinessCircleList> listRight = leftModel.getQuan_sub();

            CategoryQuanLeftAdapter adapterLeft = new CategoryQuanLeftAdapter(listModel, getActivity());
            adapterLeft.setmDefaultIndex(leftIndex);

            CategoryQuanRightAdapter adapterRight = new CategoryQuanRightAdapter(listRight, getActivity());
            adapterRight.setmDefaultIndex(rightIndex);

            mCvMiddle.setLeftAdapter(adapterLeft);
            mCvMiddle.setRightAdapter(adapterRight);
            mCvMiddle.setAdapterFinish();
        }
    }

    private void bindRightCategoryViewData(List<ModelShopListNavs> listOrderModel) {
        if (!SDCollectionUtil.isEmpty(listOrderModel)) {
            CategoryOrderAdapter adapter = new CategoryOrderAdapter(listOrderModel, getActivity());
            mCvRight.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
                clickTv_locaiton();
                break;
            default:
                break;
        }
    }

    private void clickTv_locaiton() {
        locationAddress();
    }

    /**
     * 定位地址
     */
    private void locationAddress() {
        // 开始定位
        setCurrentLocation("定位中", false);
        BaiduMapManager.getInstance().startLocation(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    setCurrentLocation(BaiduMapManager.getInstance().getCurAddressShort(), true);
                }
                BaiduMapManager.getInstance().stopLocation();
            }
        });
    }

    private void setCurrentLocation(String string, boolean isLocationSuccess) {
        if (!TextUtils.isEmpty(string)) {
            if (mTvAddress != null) {
                mTvAddress.setText(string);
                if (isLocationSuccess) {
                    mPtrlvContent.setRefreshing();
                }
            }
        }
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                initData();
                mPtrlvContent.setRefreshing();
                break;

            default:
                break;
        }
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelBusinessCircleList> modelBusinessCircleLists;
    List<ModelClassifyList> modelClassifyLists;
    List<ModelShopListNavs> navs;
    List<ModelBusinessListings> itemsModelBusinessListings;

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (SellerConstants.BUSINESS_CIRCLE_LIST.equals(method)) {
            //商圈
            modelBusinessCircleLists = datas;
            message.what = 0;
            App.getInstance().modelBusinessCircleLists = datas;
        } else if (SellerConstants.CLASSIFY_LIST.equals(method)) {
            //类别
            modelClassifyLists = datas;
            message.what = 1;
            App.getInstance().modelClassifyLists = datas;
        } else if (SellerConstants.ORDER_BY_LIST.equals(method)) {
            //排序
            navs = datas;
            message.what = 2;
            App.getInstance().navs = datas;
        } else if (SellerConstants.BUSINESS_LIST.equals(method)) {
            //店铺
            itemsModelBusinessListings = datas;
            message.what = 3;
        }
        mHandler.sendMessage(message);
    }

    @Override
    public void onFailue(String responseBody) {
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPtrlvContent.onRefreshComplete();
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //商圈
                    bindMiddleCategoryViewData(modelBusinessCircleLists);
                    break;
                case 1:
                    //类别
                    bindLeftCategoryViewData(modelClassifyLists);
                    break;
                case 2:
                    //排序
                    bindRightCategoryViewData(navs);
                    break;
                case 3:
                    //店铺
                    if (isRefresh) {
                        mListModel.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(itemsModelBusinessListings)) {
                        mListModel.addAll(itemsModelBusinessListings);
                    }
                    mAdapter.notifyDataSetChanged();
                    mPtrlvContent.onRefreshComplete();
                    break;
            }
        }
    };
}