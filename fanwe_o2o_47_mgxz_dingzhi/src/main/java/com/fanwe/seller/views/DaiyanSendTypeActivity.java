package com.fanwe.seller.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.MyPagerAdapter;
import com.fanwe.seller.adapters.TypeHorizontalScrollViewAdapter;
import com.fanwe.seller.adapters.TypeListViewAdapter;
import com.fanwe.seller.model.TypeEntity;
import com.fanwe.seller.model.TypeModel;
import com.fanwe.seller.views.customize.DPViewPager;
import com.fanwe.seller.views.customize.ListViewForScrollView;
import com.fanwe.seller.views.customize.MultiScrollView;
import com.fanwe.seller.views.customize.PointView;
import com.fanwe.seller.views.customize.TypeHorizontalScrollView;
import com.fanwe.seller.views.fragment.FirstFragment;
import com.fanwe.seller.views.fragment.SecondTypeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zhouhy on 2017/1/11.
 */

public class DaiyanSendTypeActivity  extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private int[] ICON_MAP_COMMON = {R.drawable.ic_r8, R.drawable.ic_r9};
    private int ICON_SIZE = ICON_MAP_COMMON.length;
    private TypeHorizontalScrollView mHorizontalScrollView;
    private final int PAGESIZE = 8;
    private DPViewPager mViewPager;
    private View mView;
    List<SecondTypeFragment> mFragmentList;

    private CircleIndicator mIndicator;
    private TypeHorizontalScrollViewAdapter mAdapter;
    private List<TypeEntity> mDatas;
    private List<TypeModel> secondDatas;
    private TypeEntity currentFirstType;
    private HashMap<String, List<TypeModel>> allTypes;
    private HashMap<String, Integer> allTypesPageSize;
    private LinearLayout topView;
    private LinearLayout mFlowView;
    private MultiScrollView mScrollView;
    ListViewForScrollView listViewForScrollView;
    TypeListViewAdapter listViewAdapter;
    ArrayList<String> data;
    RelativeLayout title_line;


    public DaiyanSendTypeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daiyan_sendtype_act);
        title_line = (RelativeLayout)findViewById(R.id.title_line);

        mHorizontalScrollView = (TypeHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        initHorizontalScrollView();
        createSecondViewPager();
        initScrollView();
    }


    private void initView() {
        mViewPager = (DPViewPager) findViewById(R.id.viewpager_meituan);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setNeedScroll(false);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));

    }


    public void createSecondViewPager() {
        mFragmentList = null;
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<SecondTypeFragment>();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            TypeEntity entity = mDatas.get(i);
            secondDatas = allTypes.get(entity.getId());
            if (secondDatas != null && secondDatas.size() > 0) {
                int pageSize = 0;
                int count = secondDatas.size();
                if (count > PAGESIZE) {
                    pageSize = count / PAGESIZE;
                    if (count % PAGESIZE > 0) {
                        pageSize++;
                    }
                } else {
                    pageSize = 1;
                }
                SecondTypeFragment secondTypeFragment = new SecondTypeFragment();
                List<FirstFragment> firstFragments = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    FirstFragment firstFragment = new FirstFragment();
                    int lastIndex = j * PAGESIZE + (PAGESIZE - 1);
                    if (lastIndex > secondDatas.size()) {
                        lastIndex = secondDatas.size();
                    }
                    firstFragment.setmDataList(secondDatas.subList(j * PAGESIZE, lastIndex));
                    firstFragments.add(firstFragment);
                }
                secondTypeFragment.setFragments(firstFragments);
                mFragmentList.add(secondTypeFragment);
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
                    public void onCurrentTypeChanged( View oldView,int position,
                                                     View view) {
                        updateCurrentItem(mDatas.get(position), position);
                        updateHorizontalScrollViewItem(oldView,false);
                        updateHorizontalScrollViewItem(view,true);
                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new TypeHorizontalScrollView.OnItemClickListener() {

            @Override
            public void onClick(View oldView,View view, int position) {

                updateCurrentItem(mDatas.get(position), position);
                updateHorizontalScrollViewItem(oldView,false);
                updateHorizontalScrollViewItem(view,true);
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter);
    }

    public void getFirstType() {
        if (mDatas == null) {
            mDatas = new ArrayList<TypeEntity>();
            allTypes = new HashMap<>();
            allTypesPageSize = new HashMap<>();
        }
        for (int i = 0; i < 10; i++) {
            TypeEntity entity = new TypeEntity();
            entity.setId(i + "");
            entity.setTypeName("大类" + i + "类");
            entity.setTypeLevel("1");
            mDatas.add(entity);
            updateSecondType(entity);
            allTypes.put(i + "", secondDatas);
        }

    }

    public void updateSecondType(TypeEntity firstType) {
        secondDatas = null;
        if (secondDatas == null) {
            secondDatas = new ArrayList<>();
        }
        for (int i = 0; i < 10; i++) {
            TypeModel model = new TypeModel();
            model.setContent(firstType.getTypeName() + i);
            secondDatas.add(model);
        }
    }

    /**
     * 修改horizontalscrollview item的颜色
     * @param view
     * @param selected
     */
    public  void updateHorizontalScrollViewItem(View view,boolean selected)
    {
        if(view==null){
            return;
        }
        if(view instanceof  RelativeLayout){
            RelativeLayout relativeLayout = (RelativeLayout)view;
            TextView  textView =(TextView) relativeLayout.findViewById(R.id.textview1);
            View  view1 = (View)relativeLayout.findViewById(R.id.divline);
            if(selected) {
                textView.setTextColor(Color.parseColor("#FF2E2E2E"));
                view1.setVisibility(View.VISIBLE);
            }else{
                textView.setTextColor(Color.parseColor("#FFCCCCCC"));
                view1.setVisibility(View.GONE);
            }
        }
    }
    public void updateCurrentItem(TypeEntity firstType, int position) {
        if (firstType == null || TextUtils.isEmpty(firstType.getId())) {
            return;
        }
        if (allTypesPageSize == null) {
            return;
        }
        if (mViewPager != null) {

            mViewPager.setCurrentItem(position);
        }
    }

    private void initScrollView() {
        mScrollView = (MultiScrollView) findViewById(R.id.scroll_view);
        topView = (LinearLayout) findViewById(R.id.topview);
        mFlowView = (LinearLayout) findViewById(R.id.flow_llay);
        listViewForScrollView = (ListViewForScrollView) findViewById(R.id.list_view);

        listViewAdapter = new TypeListViewAdapter(getData(), this);
        listViewForScrollView.setAdapter(listViewAdapter);
        listViewForScrollView.setFocusable(false);
        listViewForScrollView.setOnItemClickListener(onItemClickListener);

        /************************关键代码**************
         * 暂时想到的这个方案,当listview滑动到最顶部的时候,拦截scrollview事件,listview可以刷新
         * 反之,取消拦截scrollview事件,listview不能刷新
         * ******************************/
        listViewForScrollView.setOnTouchListener(new View.OnTouchListener() {

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
        });

        setListViewHeightBasedOnChildren(listViewForScrollView);
        //监听浮动view的滚动状态
        mScrollView.listenerFlowViewScrollState(topView, mFlowView);
        //将ScrollView滚动到起始位置
        mScrollView.scrollTo(0, 0);
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Toast.makeText(DaiyanSendTypeActivity.this, "菜单" + (arg2 + 1), Toast.LENGTH_SHORT).show();
        }
    };

    private void onLoadMore() {
        int oldSize = data.size();
        for (int i = oldSize; i < oldSize + 30; i++) {
            data.add("更多的菜单" + i);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewAdapter.notifyDataSetChanged();
            }
        });

    }

    private ArrayList<String> getData() {
        data = new ArrayList<String>();
        for (int i = 1; i < 30; i++) {
            data.add("菜单" + i);
        }
        return data;
    }

    /**
     * 用于解决ScrollView嵌套listview时，出现listview只能显示一行的问题
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Toast.makeText(getActivity(), "index" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
