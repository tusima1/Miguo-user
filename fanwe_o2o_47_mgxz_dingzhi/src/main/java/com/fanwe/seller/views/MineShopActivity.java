package com.fanwe.seller.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.MineShopListPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;


/**
 * Created by Administrator on 2016/7/29.
 */
public class MineShopActivity extends FragmentActivity {

    private MineShopListPagerAdapter mMineShopListPagerAdapter;
    private ViewPager mViewPager;
    private SmartTabLayout tab;
    private int selectedTabPostion = 0;
    ViewPager.OnPageChangeListener listener;
    private int prePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_shop);
        preWidget();
        initView();
        setListener();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_arrow_left_bar_mine_shop:
                finish();
                break;
        }
    }

    private void setListener() {
        tab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View inflate = LayoutInflater.from(MineShopActivity.this).inflate(R.layout.tv_tab_mine_shop, container, false);
                TextView textView = (TextView) inflate.findViewById(R.id.tv_name_tab_mine_shop);
                switch (position) {
                    case 0:
                        textView.setText("我的代言");
                        break;
                    case 1:
                        textView.setText("好友推荐");
                        break;
                    case 2:
                        textView.setText("精选推荐");
                        break;
                }
                return inflate;
            }
        });
        tab.setViewPager(mViewPager);
        listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != prePosition) {
                    TextView textView = (TextView) tab.getTabAt(prePosition).findViewById(R.id.tv_name_tab_mine_shop);
                    textView.setTextColor(getResources().getColor(R.color.colorTextG2));
                }
                TextView textView = (TextView) tab.getTabAt(position).findViewById(R.id.tv_name_tab_mine_shop);
                textView.setTextColor(getResources().getColor(R.color.main_color));
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        tab.setOnPageChangeListener(listener);
        listener.onPageSelected(0);

    }

    private void preWidget() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mine_shop);
        tab = (SmartTabLayout) findViewById(R.id.tab_mine_shop);
    }


    public void initView() {
        initViewPager();
    }


    public void initViewPager() {
        mMineShopListPagerAdapter = new MineShopListPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mMineShopListPagerAdapter);
        mViewPager.setCurrentItem(selectedTabPostion);
        tab.setViewPager(mViewPager);
    }

}