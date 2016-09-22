package com.fanwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.adapter.CategoryHoltelLeftAdapter;
import com.fanwe.adapter.CategoryOrderAdapter;
import com.fanwe.adapter.HoltelListAdapter;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.Constant.SearchTypeMap;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.SDLvCategoryView;
import com.fanwe.library.customview.SDLvCategoryView.SDLvCategoryViewListener;
import com.fanwe.library.customview.SDLvCategoryViewHelper;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDHandlerUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.HoltelModel;
import com.fanwe.model.HoltelModel_list;
import com.fanwe.model.Holtel_indexActmode;
import com.fanwe.model.Holtel_rangeAct;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getShopList.ModelShopListNavs;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒店列表
 *
 * @author Administrator
 */
public class HoltelSearchListActivity extends BaseActivity {
    private static int REQUESTCODE = 854;
    private static int REQUEST_CODE = 234;
    protected int value;
    protected int remmod;
    protected int buy;
    protected HoltelModel mActModel;
    @ViewInject(R.id.lcv_left)
    private SDLvCategoryView mCvLeft = null;
    @ViewInject(R.id.lcv_middle)
    private SDLvCategoryView mCvMiddle = null;
    @ViewInject(R.id.lcv_right1)
    private SDLvCategoryView mCvRight1 = null;
    @ViewInject(R.id.lcv_right2)
    private SDLvCategoryView mCvRight2 = null;
    @ViewInject(R.id.ll_empty)
    private LinearLayout mLlEmpty = null;
    @ViewInject(R.id.ll_current_location)
    private LinearLayout mLlCurrentLocation = null;
    @ViewInject(R.id.tv_current_address)
    private TextView mTvAddress = null;
    @ViewInject(R.id.iv_location)
    private ImageView mIvLocation = null;
    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlvContent = null;
    private List<HoltelModel_list> mListModel = new ArrayList<HoltelModel_list>();
    private HoltelListAdapter mAdapter;
    private TextView time;
    private TextView search;
    private boolean mIsFirstBindCategoryViewData = true;
    private ArrayList<ModelShopListNavs> price;
    private ArrayList<ModelShopListNavs> recommend;
    private ArrayList<ModelShopListNavs> sale;
    private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();
    private SDLvCategoryViewHelper mHelper;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatTime = new SimpleDateFormat("MM-dd");
    private int qid;
    private int range;
    private int rate;
    private int page = 1;
    private String keyword;
    private String city_name;
    private String begin_time;
    private String end_time;
    private String inday;
    private String outday;
    private String begin;
    private String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_holtel_lv);
        init();
    }

    private void init() {
        getIntentData();
        initTitle();
        bindDefaultLvData();
        bindLocationData();
        initCategoryView();
        initCategoryViewNavigatorManager();
        registeClick();
        initPullRefreshLv();
    }

    private void initTitle() {
        mTitle.setCustomViewMiddle(R.layout.view_holtel_tab);
        mTitle.mTitleMiddle.setGravity(Gravity.CENTER);
        LinearLayout ll_container = (LinearLayout) mTitle.findViewById(R.id.ll_container);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int maxMiddleWidth = width - mTitle.mLlLeft.getWidth();
        ll_container.setMinimumWidth(maxMiddleWidth);
        time = (TextView) mTitle.findViewById(R.id.tab_0);
        begin = formatTime.format(new Date(Long.parseLong(begin_time) * 1000));
        end = formatTime.format(new Date(Long.parseLong(end_time) * 1000));
        SDViewBinder.setTextView(time, "住" + begin
                + "\n" + "离" + end);
        search = (TextView) mTitle.findViewById(R.id.tab_1);
        time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarListActivity.class);
                intent.putExtra("id", 2);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("city", getIntent().getExtras().getString("city_name"));
                bundle.putInt("id", 33);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_location_home_top);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        Intent intent = new Intent(this, MapSearchActivity.class);
        intent.putExtra(MapSearchActivity.EXTRA_SEARCH_TYPE, SearchTypeMap.STORE);
        intent.putExtra(MapSearchActivity.EXTRA_CT_ID, 15);
        startActivity(intent);
    }

    private void initPullRefreshLv() {
        mPtrlvContent.setMode(Mode.BOTH);
        mPtrlvContent.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                requestData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mActModel.getList() != null) {
                    if (mActModel.getList().size() < mActModel.getPage_size()) {
                        MGToast.showToast("没有更多数据了");
                        mPtrlvContent.onRefreshComplete();
                    } else {
                        page = mActModel.getPage_now() + 1;
                        requestData(true);
                    }
                }
            }
        });
        mPtrlvContent.setRefreshing();
    }

    protected void fillRequestParams(RequestModel model) {

        model.putCtl("hotel");
        model.putAct("index");
        model.put("city", AppRuntimeWorker.getCityIdByCityName(city_name));
        model.put("time_begin", begin_time); //入住时间
        model.put("time_end", end_time);//离店时间
        model.put("keyword", keyword);// 搜索
        model.put("qid", qid);//商圈
        model.put("range", range);//距离
        model.put("price", value);//价格
        model.put("recommend", remmod);//推荐
        model.put("avg_point", rate);//评价
        model.put("buy_count", buy);//销量
        model.put("m_longitude", BaiduMapManager.getInstance().getLongitude());
        model.put("m_latitude", BaiduMapManager.getInstance().getLatitude());
        model.put("page", page);
    }

    private void requestData(final boolean isLoadMore) {
        RequestModel model = new RequestModel();
        fillRequestParams(model);
        SDRequestCallBack<HoltelModel> handler = new SDRequestCallBack<HoltelModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    mActModel = actModel;
                    SDViewUtil.updateAdapterByList(mListModel, mActModel.getList(), mAdapter, isLoadMore);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onFinish() {
                dealFinishRequest();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    private void bindLeftCategoryViewData(Holtel_indexActmode actModel) {
        if (!SDCollectionUtil.isEmpty(actModel.getRange())) {
            CategoryHoltelLeftAdapter adapter = new CategoryHoltelLeftAdapter(actModel.getRange(), this);
            mCvLeft.setAdapter(adapter);
        }

    }

    private void bindRight2CategoryViewData(
            List<ModelShopListNavs> navs) {

        if (!SDCollectionUtil.isEmpty(navs)) {
            CategoryOrderAdapter adapter = new CategoryOrderAdapter(navs, this);
            mCvRight2.setAdapter(adapter);
        }
    }

    private void bindRight1CategoryViewData(
            List<ModelShopListNavs> navs1) {
        if (!SDCollectionUtil.isEmpty(navs1)) {
            CategoryOrderAdapter adapter = new CategoryOrderAdapter(navs1, this);
            mCvRight1.setAdapter(adapter);
        }
    }


    private void bindMiddleCategoryViewData(
            List<ModelShopListNavs> price) {
        if (!SDCollectionUtil.isEmpty(price)) {
            CategoryOrderAdapter adapter = new CategoryOrderAdapter(price, this);
            mCvMiddle.setAdapter(adapter);
        }

    }


    protected void dealFinishRequest() {
        SDDialogManager.dismissProgressDialog();
        mPtrlvContent.onRefreshComplete();
        SDViewUtil.toggleEmptyMsgByList(mListModel, mLlEmpty);
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

    private void initCategoryViewNavigatorManager() {

        SDViewBase[] items = new SDViewBase[]{mCvLeft, mCvMiddle, mCvRight1, mCvRight2};
        mViewManager.setItems(items);
        mViewManager.setmMode(SDViewNavigatorManager.Mode.CAN_NONE_SELECT);
    }

    private void initCategoryView() {
        mCvLeft.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_holtel_press_down);
        mCvLeft.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_holtel_press_up);
        mCvLeft.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvLeft.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvLeft.mTvTitle.setText("距离");
        mCvLeft.setmListener(new SDLvCategoryViewListener() {
            @Override
            public void onItemSelect(int index, Object model) {
                if (model instanceof Holtel_rangeAct) {
                    Holtel_rangeAct orderModel = (Holtel_rangeAct) model;
                    range = orderModel.getValue();
                    mPtrlvContent.setRefreshing();
                    mCvLeft.mTvTitle.setTextColor(Color.parseColor("#FB6F08"));
                }
            }
        });

        mCvMiddle.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_holtel_press_down);
        mCvMiddle.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_holtel_press_up);
        mCvMiddle.mIvLeft.setImageDrawable(null);
        mCvMiddle.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvMiddle.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvMiddle.mTvTitle.setText("价格");
        mCvMiddle.setmListener(new SDLvCategoryViewListener() {
            @Override
            public void onItemSelect(int index, Object model) {
                if (model instanceof ModelShopListNavs) {
                    ModelShopListNavs orderModel = (ModelShopListNavs) model;
                    if (orderModel.getName().equals("低价优先")) {
                        if (remmod == 2 || rate == 2 || buy == 2) {
                            mCvRight1.mTvTitle.setText("推荐");
                            mCvRight2.mTvTitle.setText("销量");
                            remmod = 0;
                            rate = 0;
                            buy = 0;
                        }
                        value = 1;

                    } else if (orderModel.getName().equals("高价优先")) {

                        if (remmod == 2 || rate == 2 || buy == 2) {
                            mCvRight1.mTvTitle.setText("推荐");
                            mCvRight2.mTvTitle.setText("销量");
                            remmod = 0;
                            rate = 0;
                            buy = 0;
                        }
                        value = 2;
                    }
                    mCvMiddle.mTvTitle.setTextColor(Color.parseColor("#FB6F08"));
                    mPtrlvContent.setRefreshing();
                }
            }
        });
        mCvRight1.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_holtel_press_down);
        mCvRight1.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_holtel_press_up);
        mCvRight1.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvRight1.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvRight1.mTvTitle.setText("推荐");
        mCvRight1.setmListener(new SDLvCategoryViewListener() {
            @Override
            public void onItemSelect(int index, Object model) {
                if (model instanceof ModelShopListNavs) {
                    ModelShopListNavs orderModel = (ModelShopListNavs) model;
                    if (orderModel.getName().equals("推荐排序")) {
                        if (value == 1 || value == 2 || buy == 2) {
                            mCvMiddle.mTvTitle.setText("价格");
                            mCvRight2.mTvTitle.setText("销量");
                            value = 0;
                            buy = 0;
                        }
                        remmod = 2;
                    } else if (orderModel.getName().equals("好评优先")) {
                        if (value == 1 || value == 2 || buy == 2) {
                            mCvMiddle.mTvTitle.setText("价格");
                            mCvRight2.mTvTitle.setText("销量");
                            value = 0;
                            buy = 0;
                        }
                        rate = 2;
                    }
                    mCvRight1.mTvTitle.setTextColor(Color.parseColor("#FB6F08"));
                    mPtrlvContent.setRefreshing();
                }
            }
        });
        mCvRight2.getmAttr().setmBackgroundDrawableNormalResId(R.drawable.bg_holtel_press_down);
        mCvRight2.getmAttr().setmBackgroundDrawableSelectedResId(R.drawable.bg_holtel_press_up);

        mCvRight2.getmAttr().setmTextColorNormalResId(R.color.text_item_content);
        mCvRight2.getmAttr().setmTextColorSelectedResId(R.color.main_color);
        mCvRight2.mTvTitle.setText("销量");
        mCvRight2.setmListener(new SDLvCategoryViewListener() {
            @Override
            public void onItemSelect(int index, Object model) {
                if (model instanceof ModelShopListNavs) {
                    ModelShopListNavs orderModel = (ModelShopListNavs) model;
                    if (orderModel.getName().equals("销量优先")) {
                        if (value == 1 || value == 2 || remmod == 2 || rate == 2) {
                            mCvRight1.mTvTitle.setText("推荐");
                            mCvMiddle.mTvTitle.setText("价格");
                            value = 0;
                            remmod = 0;
                            rate = 0;
                        }
                        buy = 2;
                    }
                    mCvRight2.mTvTitle.setTextColor(Color.parseColor("#FB6F08"));
                    mPtrlvContent.setRefreshing();
                }
            }
        });
    }

    private void bindLocationData() {
        locationAddress();
    }

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

    private void bindDefaultLvData() {
        mAdapter = new HoltelListAdapter(mListModel, this, begin_time, end_time);
        mPtrlvContent.setAdapter(mAdapter);

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        city_name = bundle.getString("city_name");
        begin_time = bundle.getString("time_begin");
        end_time = bundle.getString("time_end");
        keyword = bundle.getString("keyword");
        qid = bundle.getInt("qid");
        price = new ArrayList<ModelShopListNavs>();
        ModelShopListNavs categoryOrderModel_price1 = new ModelShopListNavs();
        categoryOrderModel_price1.setName("低价优先");
        price.add(categoryOrderModel_price1);
        ModelShopListNavs categoryOrderModel_price2 = new ModelShopListNavs();
        categoryOrderModel_price2.setName("高价优先");
        price.add(categoryOrderModel_price2);

        recommend = new ArrayList<ModelShopListNavs>();
        ModelShopListNavs categoryOrderModel_recommend1 = new ModelShopListNavs();
        categoryOrderModel_recommend1.setName("推荐排序");
        recommend.add(categoryOrderModel_recommend1);
        ModelShopListNavs categoryOrderModel_recommend2 = new ModelShopListNavs();
        categoryOrderModel_recommend2.setName("好评优先");
        recommend.add(categoryOrderModel_recommend2);

        sale = new ArrayList<ModelShopListNavs>();
        ModelShopListNavs categoryOrderModel_sale1 = new ModelShopListNavs();
        categoryOrderModel_sale1.setName("销量优先");
        sale.add(categoryOrderModel_sale1);
        RequestModel model = new RequestModel();
        model.putCtl("hotel");
        model.putAct("get_conf");
        SDRequestCallBack<Holtel_indexActmode> handler = new SDRequestCallBack<Holtel_indexActmode>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    bindLeftCategoryViewData(actModel);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onFinish() {

            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
        if (mIsFirstBindCategoryViewData) {
            bindMiddleCategoryViewData(price);
            bindRight1CategoryViewData(recommend);
            bindRight2CategoryViewData(sale);
            mIsFirstBindCategoryViewData = false;
            SDHandlerUtil.runOnUiThreadDelayed(new Runnable() {

                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            }, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == CalendarListActivity.RESULT_CANCELED) {
                if (data.getIntExtra("success", -1) == 1) {
                    SharedPreferences sp = getSharedPreferences("date", Context.MODE_PRIVATE);
                    inday = sp.getString("dateIn", "");
                    outday = sp.getString("dateOut", "");
                    SimpleDateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date1 = inputFormatter1.parse(inday);
                        Date date2 = inputFormatter1.parse(outday);
                        if (!"".equals(inday) && !"".equals(outday)) {
                            begin = formatTime.format(date1);
                            end = formatTime.format(date2);
                            SDViewBinder.setTextView(time, "住" + begin + "\n" + "离" + end);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == REQUESTCODE) {
            if (resultCode == SearchListActivity.CODE) {
                Bundle bundle = data.getExtras();
                int id = bundle.getInt("id");
                int mQid = bundle.getInt("qid");
                String mSearch = bundle.getString("search");
                search.setText(mSearch);
                if (id == 0) {
                    qid = mQid;
                } else {
                    keyword = mSearch;
                }
                mPtrlvContent.setRefreshing();
            }
        }
    }

}
