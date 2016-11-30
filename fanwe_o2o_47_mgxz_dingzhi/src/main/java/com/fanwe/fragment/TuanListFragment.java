package com.fanwe.fragment;

import android.content.Intent;
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
import com.fanwe.HomeSearchActivity;
import com.fanwe.MapSearchActivity;
import com.fanwe.adapter.CategoryCateLeftAdapter;
import com.fanwe.adapter.CategoryCateRightAdapter;
import com.fanwe.adapter.CategoryOrderAdapter;
import com.fanwe.adapter.CategoryQuanLeftAdapter;
import com.fanwe.adapter.CategoryQuanRightAdapter;
import com.fanwe.adapter.TuanGruopListAdapter;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.SearchTypeMap;
import com.fanwe.constant.Constant.SearchTypeNormal;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.customview.SD2LvCategoryView;
import com.fanwe.library.customview.SDLvCategoryView;
import com.fanwe.library.customview.SDLvCategoryView.SDLvCategoryViewListener;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsGroupModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getBusinessCircleList.ModelBusinessCircleList;
import com.fanwe.seller.model.getClassifyList.ModelClassifyList;
import com.fanwe.seller.model.getGroupList.ModelDealData;
import com.fanwe.seller.model.getGroupList.ModelGroupList;
import com.fanwe.seller.model.getShopList.ModelShopListNavs;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiHomeActivity;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购列表界面
 *
 * @author js02
 */
public class TuanListFragment extends BaseFragment implements CallbackView {

    /**
     * 大分类id(int)
     */
    public static final String EXTRA_CATE_ID = "extra_cate_id";

    /**
     * 小分类id(int)
     */
    public static final String EXTRA_TID = "extra_tid";

    /**
     * 商圈id(int)
     */
    public static final String EXTRA_QID = "extra_qid";

    /**
     * 关键字(String)
     */
    public static final String EXTRA_KEY_WORD = "extra_key_word";

    /**
     * 优惠预告(int) 1显示
     */
    public static final String EXTRA_NOTICE = "extra_notice";

    @ViewInject(R.id.lcv_left)
    private SD2LvCategoryView mCvLeft = null;

    @ViewInject(R.id.lcv_middle)
    private SD2LvCategoryView mCvMiddle = null;

    @ViewInject(R.id.lcv_right)
    private SDLvCategoryView mCvRight = null;

    @ViewInject(R.id.ll_empty)
    private LinearLayout mLlEmpty = null;

    @ViewInject(R.id.ll_current_location)
    private LinearLayout mLlCurrentLocation = null;

    @ViewInject(R.id.tv_current_address)
    private TextView mTvAddress = null;

    @ViewInject(R.id.iv_location)
    private ImageView mIvLocation = null;

    @ViewInject(R.id.ll_current_search)
    private LinearLayout mLlCurrentSearch = null;

    @ViewInject(R.id.tv_current_keyword)
    private TextView mTvCurrentKeyword = null;

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlvContent = null;

    private TuanGruopListAdapter mAdapter = null;
    private List<GoodsGroupModel> mListModel = new ArrayList<GoodsGroupModel>();
    private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

    private boolean mIsFirstBindCategoryViewData = true;

    // ====================提交服务端参数
    /**
     * 大分类id
     */
    private String cate_id;
    /**
     * 小分类id
     */
    private String tid;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 商圈id
     */
    private String qid;
    /**
     * 排序类型
     */
    private String order_type;

    private PageModel mPage = new PageModel();

    private int mNotice;

    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isRefresh;
    private SellerHttpHelper sellerHttpHelper;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        return setContentView(R.layout.frag_tuan_list);
    }

    @Override
    protected void init() {
        super.init();
        initTitle();
        getIntentData();
        bindDefaultLvData();
        bindLocationData();
        initCategoryView();
        initCategoryViewNavigatorManager();
        registeClick();
        initPullRefreshLv();

        initData();
    }

    private void initData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(getActivity(), this);
        }
        //商圈
        if (SDCollectionUtil.isEmpty(App.modelBusinessCircleLists)) {
            sellerHttpHelper.getBusinessCircleList(AppRuntimeWorker.getCity_id());
        } else {
            modelBusinessCircleLists = App.modelBusinessCircleLists;
            bindMiddleCategoryViewData(modelBusinessCircleLists);
        }
        //类别
        if (SDCollectionUtil.isEmpty(App.modelClassifyLists)) {
            sellerHttpHelper.getClassifyList();
        } else {
            modelClassifyLists = App.modelClassifyLists;
            bindLeftCategoryViewData(modelClassifyLists);
        }
        //排序
        if (SDCollectionUtil.isEmpty(App.navs)) {
            sellerHttpHelper.getOrderByList();
        } else {
            navs = App.navs;
            bindRightCategoryViewData(navs);
        }
    }

    private void getGroupList() {
        sellerHttpHelper.getGroupList(tid, cate_id, AppRuntimeWorker.getCity_id(), order_type, qid, keyword, pageNum, pageSize);
    }

    private void bindLocationData() {
        String addrShort = BaiduMapManager.getInstance().getCurAddressShort();
        if (TextUtils.isEmpty(addrShort)) {
            locationAddress();
        }
    }

    private void initTitle() {
        String title = SDResourcesUtil.getString(R.string.tuan_gou);
        String cityName = AppRuntimeWorker.getCity_name();
        if (!TextUtils.isEmpty(cityName)) {
            title = title + " - " + cityName;
        }
        mTitle.setMiddleTextTop(title);

        if (getActivity() instanceof HiHomeActivity) {
            mTitle.setLeftImageLeft(0);
        } else {
            mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
        }

        mTitle.initRightItem(2);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_location_home_top);
        mTitle.getItemRight(1).setImageLeft(R.drawable.ic_search_home_top);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        Intent intent;
        switch (index) {
            case 0:
                intent = new Intent(getActivity(), MapSearchActivity.class);
                intent.putExtra(MapSearchActivity.EXTRA_SEARCH_TYPE, SearchTypeMap.TUAN);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getActivity(), HomeSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(HomeSearchActivity.EXTRA_SEARCH_TYPE, SearchTypeNormal.TUAN);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 从intent获取数据
     */
    private void getIntentData() {
        Intent intent = getActivity().getIntent();
        cate_id = intent.getStringExtra(EXTRA_CATE_ID);
        tid = intent.getStringExtra(EXTRA_TID);
        qid = intent.getStringExtra(EXTRA_QID);
        keyword = intent.getStringExtra(EXTRA_KEY_WORD);

        mNotice = intent.getIntExtra(EXTRA_NOTICE, -1);

        if (TextUtils.isEmpty(keyword)) {
            SDViewUtil.show(mLlCurrentLocation);
            SDViewUtil.hide(mLlCurrentSearch);
            if (BaiduMapManager.getInstance().getCurAddress() != null) {
                mTvAddress.setText(BaiduMapManager.getInstance().getCurAddressShort());
            }
        } else {
            SDViewUtil.hide(mLlCurrentLocation);
            SDViewUtil.show(mLlCurrentSearch);
            mTvCurrentKeyword.setText(keyword);
        }

    }

    /**
     * 初始化下拉刷新控件
     */
    private void initPullRefreshLv() {
        mPtrlvContent.setMode(Mode.BOTH);
        mPtrlvContent.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                getGroupList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!SDCollectionUtil.isEmpty(groupItems)) {
                    pageNum++;
                    isRefresh = false;
                    getGroupList();
                }
            }
        });

        mPtrlvContent.setRefreshing();
    }

    private void bindDefaultLvData() {
        mAdapter = new TuanGruopListAdapter(mListModel, getActivity());
        mPtrlvContent.setAdapter(mAdapter);
    }

    private void initCategoryViewNavigatorManager() {
        SDViewBase[] items = new SDViewBase[]{mCvLeft, mCvMiddle, mCvRight};
        mViewManager.setItems(items);
        mViewManager.setmMode(SDViewNavigatorManager.Mode.CAN_NONE_SELECT);
    }

    private void initCategoryView() {

        mCvLeft.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_press_down);
        mCvLeft.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_press_up);

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

        mCvMiddle.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_press_down_2);
        mCvMiddle.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_press_up_2);

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

        mCvRight.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_choosebar_press_down_3);
        mCvRight.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_choosebar_press_up_3);

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

    private void registeClick() {
        mIvLocation.setOnClickListener(this);

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
                initTitle();
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
    List<ModelGroupList> groupItems;

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (SellerConstants.BUSINESS_CIRCLE_LIST.equals(method)) {
            //商圈
            modelBusinessCircleLists = datas;
            message.what = 0;
            App.modelBusinessCircleLists = datas;
        } else if (SellerConstants.CLASSIFY_LIST.equals(method)) {
            //类别
            modelClassifyLists = datas;
            message.what = 1;
            App.modelClassifyLists = datas;
        } else if (SellerConstants.ORDER_BY_LIST.equals(method)) {
            //排序
            navs = datas;
            message.what = 2;
            App.navs = datas;
        } else if (SellerConstants.GROUP_BUY.equals(method)) {
            //团购
            groupItems = datas;
            message.what = 3;
        }
        mHandler.sendMessage(message);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

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
                    if (isRefresh) {
                        mListModel.clear();
                    }
                    //团购
                    if (!SDCollectionUtil.isEmpty(groupItems)) {
                        for (ModelGroupList bean : groupItems) {
                            GoodsGroupModel goodsGroupModel = new GoodsGroupModel();
                            goodsGroupModel.setId(bean.getId());
                            goodsGroupModel.setName(bean.getName());
                            goodsGroupModel.setAddress(bean.getAddress());
                            goodsGroupModel.setAvg_point(DataFormat.toFloat(bean.getAvg_grade()));
                            goodsGroupModel.setDiscount_pay(DataFormat.toInt(bean.getDiscount_pay()));
                            goodsGroupModel.setDistance(DataFormat.toDouble(bean.getDistance()));
                            goodsGroupModel.setPreview(bean.getPreview());
                            goodsGroupModel.setIs_verify(DataFormat.toInt(bean.getIs_verify()));
                            goodsGroupModel.setIs_youhui(DataFormat.toInt(bean.getIs_youhui()));
                            goodsGroupModel.setTel(bean.getTel());
                            goodsGroupModel.setXpoint(DataFormat.toDouble(bean.getXpoint()));
                            goodsGroupModel.setYpoint(DataFormat.toDouble(bean.getYpoint()));

                            List<GoodsModel> deal_data = new ArrayList<>();
                            if (!SDCollectionUtil.isEmpty(bean.getDeal_data())) {
                                for (ModelDealData temp : bean.getDeal_data()) {
                                    GoodsModel goodsModel = new GoodsModel();
                                    goodsModel.setId(temp.getId());
                                    goodsModel.setName(temp.getName());
                                    goodsModel.setSub_name(temp.getSub_name());
                                    goodsModel.setIcon(temp.getIcon());
                                    goodsModel.setBuy_count(DataFormat.toInt(temp.getBuy_count()));
                                    goodsModel.setCurrent_price(DataFormat.toDouble(temp.getCurrent_price()));
                                    goodsModel.setOrigin_price(DataFormat.toDouble(temp.getOrigin_price()));
                                    goodsModel.setBrief(temp.getBrief());
                                    goodsModel.setDistance(temp.getDistance());

                                    deal_data.add(goodsModel);
                                }
                            }
                            goodsGroupModel.setDeal_data(deal_data);

                            mListModel.add(goodsGroupModel);
                        }
                    }
                    mAdapter.notifyDataSetChanged();

                    SDDialogManager.dismissProgressDialog();
                    mPtrlvContent.onRefreshComplete();
                    break;
            }
        }
    };

}