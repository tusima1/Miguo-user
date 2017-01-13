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
import com.fanwe.constant.ServerUrl;
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
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.ui.view.dropdown.DropDownMenu;
import com.miguo.ui.view.floatdropdown.SearchGuideActivity;
import com.miguo.ui.view.floatdropdown.helper.DropDownPopHelper;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;
import com.miguo.view.GetSearchCateConditionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 代言人二级页面。
 * Created by zhouhy on 2017/1/11.
 */

public class DaiyanSendTypeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, CallbackView {


    private TypeHorizontalScrollView mHorizontalScrollView;

    private DPViewPager mViewPager;

    List<SecondTypeFragment> mFragmentList;

    private TypeHorizontalScrollViewAdapter mAdapter;
    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> mDatas;
    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean> secondDatas;
    private SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean currentFirstType;
    private SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean currentSecondType;
    private HashMap<Integer,SecondTypeFragment> secondFragmentList ;
    private int currentFirstTypePosition = -1;
    private int currentSecondTypePosition=-1;
    private String currentFirstTypeStr;
    private String currentSecondTypeStr;
    private HashMap<String, List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>> allTypes;
    private LinearLayout topView;
    private LinearLayout mFlowView;
    private MultiScrollView mScrollView;
    SellerListAdapter mRecycleViewAdapter;
    FullyLinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    RelativeLayout title_line;
    /**
     * 分类数据。
     */
    SearchCateConditionBean.ResultBean.BodyBean bodyBean;
    FakeDropDownMenu fakeFlowLine;
    DropDownMenu flowLine;
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

    public DaiyanSendTypeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daiyan_sendtype_act);
        mContext = this;
        title_line = (RelativeLayout) findViewById(R.id.title_line);
        fakeFlowLine = (FakeDropDownMenu) findViewById(R.id.fake_flow_llay);
        flowLine = (DropDownMenu) findViewById(R.id.flow_llay);
        mHorizontalScrollView = (TypeHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        getIntentData();
        initTitle();
        getConditionData();
        initHorizontalScrollView();
        createSecondViewPager();

        initScrollView();
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

        helper = new DropDownPopHelper(this, fakeFlowLine);
        if(mDatas==null){
            return;
        }
        //设置被选中的情形。
        if(!TextUtils.isEmpty(currentFirstTypeStr) ||currentFirstType!=null){
           for(int i = 0 ; i<mDatas.size() ; i++){
               SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean listBean =mDatas.get(i);
               if(listBean!=null){
                   if(listBean.getId().equals(currentFirstTypeStr)){
                       listBean.setChecked(true);
                       currentFirstTypePosition = i;
                       SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean bean = new  SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean();
                       bean.setId(currentFirstTypeStr);
                       bean.setName(listBean.getName());
                       bean.setImg(listBean.getImg());
                       currentFirstType = bean;
                   }
               }
           }
        }


//        helper.show();
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

        if(ServerUrl.DEBUG){
            currentFirstTypeStr ="00a089ff-34ae-4507-8690-97b6b7118a7c";
        }
        category_one = currentFirstTypeStr;
        category_two = currentSecondTypeStr;

    }

    private void initView() {
        mViewPager = (DPViewPager) findViewById(R.id.viewpager_meituan);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setNeedScroll(false);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));
        if(currentFirstTypePosition!=-1) {
            updateCurrentItem(mDatas.get(currentFirstTypePosition), currentFirstTypePosition);
        }
    }


    public void createSecondViewPager() {
        mFragmentList = null;
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<SecondTypeFragment>();
        }
        if(secondFragmentList==null){
            secondFragmentList = new HashMap<>();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean entity = mDatas.get(i);
            secondDatas = allTypes.get(entity.getId());
            if (secondDatas != null && secondDatas.size() > 0) {
                if(entity.isChecked()&&!TextUtils.isEmpty(currentSecondTypeStr)){
                    for(int k=0;k<secondDatas.size(); k++){
                        if(secondDatas.get(k).getId().equals(currentSecondTypeStr)){
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
                    firstFragments.add(firstFragment);
                }
                secondTypeFragment.setFragments(firstFragments);
                mFragmentList.add(secondTypeFragment);
            }else{
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
                        updateCurrentItem(mDatas.get(position), position);
                        updateHorizontalScrollViewItem(oldView, false);
                        updateHorizontalScrollViewItem(view, true);
                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new TypeHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View oldView, View view, int position) {
                updateCurrentItem(mDatas.get(position), position);
                updateHorizontalScrollViewItem(oldView, false);
                updateHorizontalScrollViewItem(view, true);
            }
        });
        //设置适配器
        if(currentFirstTypePosition!=-1){
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

    public void updateCurrentItem(SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean firstType, int position) {
        if (firstType == null || TextUtils.isEmpty(firstType.getId())) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
            updateSecondItem(position);
        }
    }

    public void updateSecondItem(int position){
       SecondTypeFragment secondTypeFragment = mFragmentList.get(position);
        if(secondTypeFragment!=null&&secondTypeFragment.getFragments()!=null&&secondTypeFragment.getFragments().size()>0){
            for(int i = 0 ; i < secondTypeFragment.getFragments().size();i++){
                FirstFragment firstFragment = secondTypeFragment.getFragments().get(i);
               List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean.CategoryTypeBean>  datas= firstFragment.getDataList();
                for(int j=0;j<datas.size();j++){
                    if(!TextUtils.isEmpty(currentSecondTypeStr)&&datas.get(j).getId().equals(currentSecondTypeStr)){
                        datas.get(j).setChecked(true);
                    }else{
                        datas.get(j).setChecked(false);
                    }
                }
            }
        }
    }

    private void initTitle() {
        ImageView  image_back = (ImageView)findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                requestData();
            }
        });

        commonshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchant_type = "0";
                isLoadmore = false;
                updateTextView(youhuishop, false);
                updateTextView(commonshop, true);
                requestData();
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
        mFlowView = (LinearLayout) findViewById(R.id.flow_llay);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_frag_sell_list);
        setRecycleView();
        recyclerView.setOnTouchListener(touchListener);
        //监听浮动view的滚动状态
        mScrollView.listenerFlowViewScrollState(topView, mFlowView);
        //将ScrollView滚动到起始位置
        mScrollView.scrollTo(0, 0);
        requestData();
    }


    private void requestData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(mContext, this);
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
        if (items != null&&items.size() >0) {
            ifShowEmptyListView(false);
        } else {
            ifShowEmptyListView(true);
        }

        if (mRecycleViewAdapter != null) {
            mRecycleViewAdapter.notifyDataSetChanged();
        }


    }

    private void onLoadMore() {
        if (pageBean == null || TextUtils.isEmpty(pageBean.getPage_total()) || pageNum <= Integer.valueOf(pageBean.getPage_total())) {
            isLoadmore = true;
            requestData();
            SDToast.showToast("进入加载更多了！");
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
//        SDToast.showToast("onFailue");
    }

    @Override
    public void onFinish(String method) {

    }
}
