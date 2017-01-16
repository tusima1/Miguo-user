package com.fanwe.seller.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.base.CallbackView;
import com.fanwe.base.PageBean;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.MyPagerAdapter;
import com.fanwe.seller.adapters.SellerListAdapter;
import com.fanwe.seller.adapters.TypeHorizontalScrollViewAdapter;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.seller.model.getBusinessListings.ResultBusinessListings;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.seller.util.CollectionUtils;
import com.fanwe.seller.views.customize.DPViewPager;
import com.fanwe.seller.views.customize.FullyLinearLayoutManager;
import com.fanwe.seller.views.customize.MultiScrollView;
import com.fanwe.seller.views.customize.TypeHorizontalScrollView;
import com.fanwe.seller.views.fragment.FirstFragment;
import com.fanwe.seller.views.fragment.SecondTypeFragment;
import com.miguo.dao.impl.GetSearchCateConditionDaoImpl;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.SingleMode;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.ui.view.floatdropdown.SearchGuideActivity;
import com.miguo.ui.view.floatdropdown.helper.DropDownPopHelper;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;
import com.miguo.utils.BaseUtils;
import com.miguo.view.GetSearchCateConditionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 代言人二级页面。
 * Created by zhouhy on 2017/1/11.
 */

public class DaiyanSendTypeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, CallbackView, OnDropDownListener {


    public static final String COLLECT = "collect";
    public static final String REPRESENT = "represent";
    private TypeHorizontalScrollView mHorizontalScrollView;

    private DPViewPager mViewPager;

    List<SecondTypeFragment> mFragmentList;

    private TypeHorizontalScrollViewAdapter mAdapter;
    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> mDatas;
    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> secondDatas;
    private SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean currentFirstType;
    private SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean currentSecondType;
    private HashMap<Integer, SecondTypeFragment> secondFragmentList;
    private int currentFirstTypePosition = -1;
    private int currentSecondTypePosition = -1;
    private String currentFirstTypeStr;
    private String currentSecondTypeStr;
    private HashMap<String, List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>> allTypes;
    private LinearLayout topView;
    private FakeDropDownMenu mFlowView;
    private FakeDropDownMenu fakeFlowLine;
    private MultiScrollView mScrollView;
    SellerListAdapter mRecycleViewAdapter;
    FullyLinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    /**
     * 用于填充屏幕的空白 view
     */
    LinearLayout whitebg;

    RelativeLayout title_line;
    /**
     * 分类数据。
     */
    SearchCateConditionBean.ResultBean.BodyBean bodyBean;

    /**
     * 商家类型：1，优惠商家。0，全部商家
     */
    private String merchant_type = "1";
    public String area_one;
    public String area_two;
    public String category_one;
    public String category_two;
    public String filter;
    public String sort_type;
    private SellerHttpHelper sellerHttpHelper;
    private List<ModelBusinessListings> items;
    List<ResultBusinessListings> results;
    private int pageNum = 1;
    private int pageSize = 10;
    private PageBean pageBean;
    /**
     * 是否加载更多。
     */
    private boolean isLoadmore = false;

    private Context mContext;
    /**
     * 无数据显示。
     */
    LinearLayout empty_line;
    DropDownPopHelper helper;


    boolean isNeedPopviewUpdate = true;

    int displayHeight = 0;

    public DaiyanSendTypeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daiyan_sendtype_act);
        mContext = this;
        displayHeight = BaseUtils.getHeight(mContext);
        title_line = (RelativeLayout) findViewById(R.id.title_line);
        fakeFlowLine = (FakeDropDownMenu) findViewById(R.id.fake_flow_llay);
        whitebg = (LinearLayout) findViewById(R.id.whitebg);
        mHorizontalScrollView = (TypeHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        getIntentData();
        initTitle();
        getConditionData();
        initHorizontalScrollView();
        createSecondViewPager();

        initScrollView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void getConditionData() {
        bodyBean = SearchCateConditionFactory.get();
        if (bodyBean == null) {
            requestTypeData();
        } else {
            putConditionDataToBody();
        }
    }

    public void putConditionDataToBody() {
        bodyBean = SearchCateConditionFactory.get();
        mDatas = bodyBean.getCategoryList();
        if (mDatas == null) {
            return;
        }
        //设置被选中的情形。
        if (!TextUtils.isEmpty(currentFirstTypeStr) || currentFirstType != null) {

        }

    }

    /**
     * 获取分类数据
     */
    private void requestTypeData() {
        GetSearchCateConditionDaoImpl getSearchCateConditionDao = new GetSearchCateConditionDaoImpl(new GetSearchCateConditionView() {
            @Override
            public void getSearchCateConditionError(String message) {
            }

            @Override
            public void getSearchCateConditionSuccess(SearchCateConditionBean.ResultBean.BodyBean body) {
                SearchCateConditionFactory.update(body);
                putConditionDataToBody();
            }
        });
        getSearchCateConditionDao.getSearchCateCondition();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        currentFirstTypeStr = intent.getStringExtra("firstType");
        currentSecondTypeStr = intent.getStringExtra("secondType");

        if (!TextUtils.isEmpty(currentFirstTypeStr)) {
            category_one = currentFirstTypeStr;
            if (!TextUtils.isEmpty(currentSecondTypeStr)) {
                category_two = currentSecondTypeStr;
            }
        }

    }

    private void initView() {
        mViewPager = (DPViewPager) findViewById(R.id.viewpager_meituan);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setNeedScroll(false);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));

        if (!TextUtils.isEmpty(currentFirstTypeStr)) {
            currentFirstTypePosition = getPositionByFirstTypeId(currentFirstTypeStr);
        }
        if (currentFirstTypePosition != -1) {
            mHorizontalScrollView.scrollToIndex(currentFirstTypePosition);
            updateCurrentItem(currentFirstTypePosition);
        }
    }

    public FirstFragment.SecondTypeClickListener secondTypeClickListener = new FirstFragment.SecondTypeClickListener() {
        @Override
        public void onItemClickListner(SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean typeBean) {

            if (helper != null && !TextUtils.isEmpty(typeBean.getId())) {
                helper.performMarkIds(category_one,typeBean.getId());
                currentSecondTypeStr = typeBean.getId();
                category_two = currentSecondTypeStr;

                requestData(true);
            }
        }
    };

    public void createSecondViewPager() {
        mFragmentList = null;
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<SecondTypeFragment>();
        }
        if (secondFragmentList == null) {
            secondFragmentList = new HashMap<>();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean entity = mDatas.get(i);
            secondDatas = allTypes.get(entity.getId());
            if (secondDatas != null && secondDatas.size() > 0) {
                if (entity.isChecked() && !TextUtils.isEmpty(currentSecondTypeStr)) {
                    for (int k = 0; k < secondDatas.size(); k++) {
                        if (secondDatas.get(k).getId().equals(currentSecondTypeStr)) {
                            secondDatas.get(k).setChecked(true);
                            currentSecondType = secondDatas.get(k);
                        }
                    }
                }
                int pageSize = 0;
                int count = secondDatas.size();
                if (count > 8) {
                    pageSize = count / 8;
                    if (count % 8 > 0) {
                        pageSize++;
                    }
                } else {
                    pageSize = 1;
                }
                SecondTypeFragment secondTypeFragment = new SecondTypeFragment();
                List<FirstFragment> firstFragments = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    FirstFragment firstFragment = new FirstFragment();
                    int lastIndex = j * 8 + (8 - 1);
                    if (lastIndex > secondDatas.size()) {
                        lastIndex = secondDatas.size();
                    }
                    firstFragment.setmDataList(secondDatas.subList(j * 8, lastIndex));
                    firstFragment.setSecondTypeClickListener(secondTypeClickListener);
                    firstFragments.add(firstFragment);
                }
                secondTypeFragment.setFragments(firstFragments);
                mFragmentList.add(secondTypeFragment);
            } else {
                mFragmentList.add(new SecondTypeFragment());
            }
        }
        initView();
    }

    public void initHorizontalScrollView() {
        getFirstType();
        mAdapter = new TypeHorizontalScrollViewAdapter(this, mDatas);
        //添加滚动回调
        mHorizontalScrollView
                .setCurrentTypeChangeListener(new TypeHorizontalScrollView.CurrentTypeChangeListener() {
                    @Override
                    public void onCurrentTypeChanged(View oldView, int position,
                                                     View view) {
                        updateCurrentItem(position);
                        if (helper != null && !TextUtils.isEmpty(mDatas.get(position).getId())) {
                            String selectedId = mDatas.get(position).getId();

                                if (isNeedPopviewUpdate) {
                                    helper.performMarkIds(category_one,selectedId);
                                } else {
                                    isNeedPopviewUpdate = true;
                                }

                            currentFirstTypeStr = selectedId;
                            category_one = selectedId;
                            requestData(true);
                        }
                        updateHorizontalScrollViewItem(oldView, false);
                        updateHorizontalScrollViewItem(view, true);

                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new TypeHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View oldView, View view, int position) {
                updateCurrentItem(position);
                if (helper != null && !TextUtils.isEmpty(mDatas.get(position).getId())) {
                    String selectedId = mDatas.get(position).getId();

                        if (isNeedPopviewUpdate) {
                            helper.performMarkIds(category_one,selectedId);
                        } else {
                            isNeedPopviewUpdate = true;
                        }

                    currentFirstTypeStr = selectedId;
                    category_one = selectedId;
                    requestData(true);
                }
                updateHorizontalScrollViewItem(oldView, false);
                updateHorizontalScrollViewItem(view, true);
            }
        });
        //设置适配器
        if (currentFirstTypePosition != -1) {
            mHorizontalScrollView.setmFristIndex(currentFirstTypePosition);
        }
        mHorizontalScrollView.initDatas(mAdapter);

    }

    public void getFirstType() {
        if (mDatas == null) {
            mDatas = new ArrayList<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean>();
        }
        if (allTypes == null) {
            allTypes = new HashMap<>();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i) == null) {
                continue;
            }
            if (mDatas.get(i).getCategory_type() == null) {
                allTypes.put(mDatas.get(i).getId(), new ArrayList<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>());
            } else {
                allTypes.put(mDatas.get(i).getId(), mDatas.get(i).getCategory_type());
            }
        }
    }

    /**
     * 修改horizontalscrollview item的颜色
     *
     * @param view
     * @param selected
     */
    public void updateHorizontalScrollViewItem(View view, boolean selected) {
        if (view == null) {
            return;
        }
        if (view instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) view;
            TextView textView = (TextView) relativeLayout.findViewById(R.id.textview1);
            View view1 = (View) relativeLayout.findViewById(R.id.divline);
            if (selected) {
                textView.setTextColor(Color.parseColor("#FF2E2E2E"));
                view1.setVisibility(View.VISIBLE);
            } else {
                textView.setTextColor(Color.parseColor("#FFCCCCCC"));
                view1.setVisibility(View.GONE);
            }
        }
    }

    public void updateCurrentItem(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
            updateSecondItem(position);
        }
    }

    public void updateSecondItem(int position) {
        SecondTypeFragment secondTypeFragment = mFragmentList.get(position);
        if (secondTypeFragment != null && secondTypeFragment.getFragments() != null && secondTypeFragment.getFragments().size() > 0) {
            for (int i = 0; i < secondTypeFragment.getFragments().size(); i++) {
                FirstFragment firstFragment = secondTypeFragment.getFragments().get(i);
                int selectedSecondType=0;
                List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> datas = firstFragment.getDataList();
                for (int j = 0; j < datas.size(); j++) {
                    if (!TextUtils.isEmpty(currentSecondTypeStr) && datas.get(j).getId().equals(currentSecondTypeStr)) {
                        firstFragment.setLastSelectedPosition(j);
                        selectedSecondType = j;
                        datas.get(j).setChecked(true);
                    } else {
                        datas.get(j).setChecked(false);
                    }
                }
                if(selectedSecondType==0){
                    currentSecondTypeStr = datas.get(0).getId();
                    category_two = currentSecondTypeStr;
                    datas.get(0).setChecked(true);
                }

                if (firstFragment.getmDPGridViewAdapter() != null && firstFragment.getmDPGridViewAdapter().getmDataList() != null) {
                    firstFragment.notifyAdapterChange();
                }
            }
        }
    }

    private void initTitle() {
        ImageView image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helper != null) {
                    helper.dismiss();
                }
                finish();
            }
        });
        final TextView youhuishop = (TextView) findViewById(R.id.youhuishop);
        final TextView commonshop = (TextView) findViewById(R.id.commonshop);
        youhuishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchant_type = "1";
                isLoadmore = false;
                updateTextView(youhuishop, true);
                updateTextView(commonshop, false);
                requestData(true);
            }
        });

        commonshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchant_type = "0";
                isLoadmore = false;
                updateTextView(youhuishop, false);
                updateTextView(commonshop, true);
                requestData(true);
            }
        });
        ImageView search_button = (ImageView) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DaiyanSendTypeActivity.this, SearchGuideActivity.class);
                startActivity(intent1);
            }
        });
    }

    public void updateTextView(TextView view, boolean isCheck) {
        if (isCheck) {
            view.setTextColor(Color.parseColor("#595959"));
        } else {
            view.setTextColor(Color.parseColor("#CCCCCC"));
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int localheight = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    localheight = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int sY = (int) event.getY();
                    int scrollY = mScrollView.getScrollY();
                    int height = mScrollView.getHeight();
                    int scrollViewMeasuredHeight = mScrollView.getChildAt(0)
                            .getMeasuredHeight();
                    //这个表示,当滑动到scrollview顶部的时候,
                    if (scrollY == 0) {
                        //检测到在listview里面手势向下滑动的手势,就下拉刷新,反之,则无法触发下拉刷新
                        if (localheight - sY > 10) {
                            // 取消拦截scrollview事件,listview不能刷新
                            mScrollView.requestDisallowInterceptTouchEvent(false);
                            break;
                        }
                        // 拦截scrollview事件,listview可以刷新
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                    //这个表示scrollview没恢复到顶部,在listview里面是无法触发下拉刷新的
                    else {
                        // 取消拦截scrollview事件,listview不能刷新
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    }
                    //滑动到底部的时候,自动去加载更多.
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        // 滑到底部触发加载更多
                        onLoadMore();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    localheight = 0;
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private void initScrollView() {
        mScrollView = (MultiScrollView) findViewById(R.id.scroll_view);
        topView = (LinearLayout) findViewById(R.id.topview);
        mFlowView = (FakeDropDownMenu) findViewById(R.id.flow_llay);
        fakeFlowLine = (FakeDropDownMenu) findViewById(R.id.fake_flow_llay);

        helper = new DropDownPopHelper(this, fakeFlowLine, mFlowView);
        helper.setOnDropDownListener(this);
        fakeFlowLine.setOnFakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMenu();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_frag_sell_list);
        setRecycleView();
        recyclerView.setOnTouchListener(touchListener);
        //监听浮动view的滚动状态
        mScrollView.listenerFlowViewScrollState(topView, mFlowView, fakeFlowLine);
        mScrollView.setScrollChangedFlowViewListener(new MultiScrollView.ScrollChangedFlowViewListener() {
            @Override
            public void ifShowFlowView(boolean show) {
                if (show) {
                    showTopMenu();
                } else {
                    showFakeMenu();
                }
            }
        });
        //将ScrollView滚动到起始位置
        mScrollView.scrollTo(0, 0);
        requestData(true);
    }


    private void requestData(boolean isFresh) {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(mContext, this);
        }
        if(isFresh){
            pageNum =1;
            isLoadmore=false;
            updateFirstTypeAndSecondType(category_one,category_two);
        }
        sellerHttpHelper.getShopSearch(area_one, area_two, category_one, category_two, filter, "", sort_type, pageNum, pageSize, merchant_type);
    }

    /**
     * 初始化recycleview 的内容。
     */
    private void setRecycleView() {
        if (items == null) {
            items = new ArrayList<>();
        }
        mRecycleViewAdapter = new SellerListAdapter(DaiyanSendTypeActivity.this, items);
        mLayoutManager = new FullyLinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mRecycleViewAdapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Toast.makeText(DaiyanSendTypeActivity.this, "菜单" + (arg2 + 1), Toast.LENGTH_SHORT).show();
        }
    };

    public void setData(List<ModelBusinessListings> models) {
        if (!isLoadmore) {
           items.clear();
        }
        items.addAll(models);
        //是否显示空的列表样式 。
        if (items != null && items.size() > 0) {
            ifShowEmptyListView(false);
            if (items.size() < 4) {
                ifShowWhitebg(true);
            } else {
                ifShowWhitebg(false);
            }
        } else {
            ifShowEmptyListView(true);
            ifShowWhitebg(true);
        }

        if (mRecycleViewAdapter != null) {
            mRecycleViewAdapter.notifyDataSetChanged();
        }


    }

    private void onLoadMore() {
        if (pageBean == null || TextUtils.isEmpty(pageBean.getPage_total()) || pageNum <= Integer.valueOf(pageBean.getPage_total())) {
            isLoadmore = true;
            requestData(false);
        } else {
            SDToast.showToast("没有更多了！");
        }
    }

    /**
     * 当查询结果为空是显示空的页。
     */
    private void ifShowEmptyListView(boolean show) {
        empty_line = (LinearLayout) findViewById(R.id.empty_line);
        if (show) {
            empty_line.setVisibility(View.VISIBLE);

        } else {
            empty_line.setVisibility(View.GONE);
        }
    }

    private void ifShowWhitebg(boolean show) {

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) whitebg.getLayoutParams();
        lp.height = (displayHeight * 3) / 4;

        if (show) {
            whitebg.setVisibility(View.VISIBLE);
        } else {
            whitebg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (SellerConstants.SHOP_SEARCH.equals(method)) {
            //店铺
            results = datas;
            if (CollectionUtils.isValid(results)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResultBusinessListings resultTemp = results.get(0);
                        pageBean = resultTemp.getPage();
                        pageNum = Integer.valueOf(pageBean.getPage());
                        pageNum++;
                        setData(resultTemp.getShop_list());
                    }
                });

            }
        }
    }

    @Override
    public void onFailue(String responseBody) {
        setData(new ArrayList<ModelBusinessListings>());
    }

    @Override
    public void onFinish(String method) {

    }

    public void clickMenu() {
        mScrollView.smoothScrollTo(0, topView.getMeasuredHeight());
        showTopMenu();
    }

    private void showTopMenu() {
        mFlowView.setVisibility(View.VISIBLE);
        fakeFlowLine.setVisibility(View.INVISIBLE);
    }

    private void showFakeMenu() {
        mFlowView.setVisibility(View.INVISIBLE);
        fakeFlowLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(int index, Pair<SingleMode, SingleMode> pair, List<SingleMode> items) {
        helper.dismiss();
        switch (index) {
            case 1:
                handleItemSelectNearBy(pair);
                requestData(true);
                break;
            case 2:
                typeChange(pair);
                requestData(true);
                break;
            case 3:
                handleItemSelectsIntel(pair);
                requestData(true);
                break;
            case 4:
                handleItemSelectFilter(items);
                requestData(true);
                break;
            default:
                break;
        }
    }

    private void handleItemSelectNearBy(Pair<SingleMode, SingleMode> pair) {
        if (pair != null) {
            if (null != pair.first) {
                area_one = pair.first.getSingleId();
            } else {
                area_one = "";
            }
            if (null != pair.second) {
                area_two = pair.second.getSingleId();
            } else {
                area_two = "";
            }
        }
    }

    private void handleItemSelectsIntel(Pair<SingleMode, SingleMode> pair) {
        if (null != pair.first) {
            sort_type = pair.first.getSingleId();
        }
    }

    private void handleItemSelectFilter(List<SingleMode> items) {
        if (SDCollectionUtil.isEmpty(items)) {
            return;
        }
        String ids = "";
        for (int i = 0; i < items.size(); i++) {
            ids = ids + (i == 0 ? "" : ",") + items.get(i).getSingleId();
        }
        filter = ids;
    }

    public void updateFirstTypeAndSecondType(String firstType,String secondType){
        category_one = "";
        category_two="";
        int position = getPositionByFirstTypeId(firstType);
        category_one = firstType;
        if(position!=-1&&!TextUtils.isEmpty(secondType)){

            List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> secondTypeBeanList =mDatas.get(position).getCategory_type();
            if(secondTypeBeanList!=null) {
                for (int i = 0;i<secondTypeBeanList.size();i++){
                    SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean bean = secondTypeBeanList.get(i);
                    if(secondType.equals(bean.getId())){
                        category_two = secondType;
                    }
                }
            }
        }
    }

    public void typeChange(Pair<SingleMode, SingleMode> pair) {
        if (pair == null) {
            return;
        }
        SingleMode firstType = pair.first;
        SingleMode secondType = pair.second;
        if (firstType != null && !TextUtils.isEmpty(firstType.getSingleId())) {
            int position = getPositionByFirstTypeId(firstType.getSingleId());
            mHorizontalScrollView.scrollToIndex(position);
            if (position != -1) {
                if (secondType != null && !TextUtils.isEmpty(secondType.getSingleId())) {
                    currentSecondTypeStr = secondType.getSingleId();
                } else {
                    currentSecondTypeStr = "";
                }
                updateCurrentItem(position);
            }
        }

    }

    /**
     * 根据传过来的值 计算当前的第一个被选中的大类
     *
     * @param id
     * @return
     */
    public int getPositionByFirstTypeId(String id) {
        //先将原来的位置清空。
        int position = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean listBean = mDatas.get(i);
            if (listBean != null) {
                if (listBean.getId().equals(id)) {
                    listBean.setChecked(true);
                    position = i;
                    SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean bean = new SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean();
                    bean.setId(id);
                    bean.setName(listBean.getName());
                    bean.setImg(listBean.getImg());
                    currentFirstType = bean;
                    return position;
                }
            }
        }
        return position;
    }


}
