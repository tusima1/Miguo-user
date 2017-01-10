package com.fanwe.search.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2017/1/5.
 */

public class SearchResultActivity extends FragmentActivity {
    private ImageView ivLeft;
    private TextView tvMiddle, tvGoods, tvShop;
    private String pageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        preWidget();
        preView();
        setListener();
        getData();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_goods_frag_groupon_list:
                clickGoods();
                break;
            case R.id.tv_shop_frag_groupon_list:
                clickShop();
                break;
        }
    }

    private void clickShop() {
        if ("shop".equals(pageType)) {
            return;
        }
        setTabSelected("shop");
    }

    private void clickGoods() {
        if ("goods".equals(pageType)) {
            return;
        }
        setTabSelected("goods");
    }

    /**
     * 设置选中的标签
     *
     * @param pageType
     */
    private void setTabSelected(String pageType) {
        this.pageType = pageType;
        setTextColor();
        switchFragment();
    }

    /**
     * 设置标题颜色
     */
    private void setTextColor() {
        if ("shop".equals(pageType)) {
            tvShop.setTextColor(getResources().getColor(R.color.black_2e));
            tvGoods.setTextColor(getResources().getColor(R.color.c_CCCCCC));
        } else {
            tvGoods.setTextColor(getResources().getColor(R.color.black_2e));
            tvShop.setTextColor(getResources().getColor(R.color.c_CCCCCC));
        }
    }

    /**
     * 切换fragment
     */
    private void switchFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if ("shop".equals(pageType)) {
            //商家
            if (fragmentGoods != null && !fragmentGoods.isHidden()) {
                ft.hide(fragmentGoods);
            }
            if (fragmentShop == null) {
                fragmentShop = new FragmentSearchShop();
                ft.add(R.id.content_act_search, fragmentShop);
            } else {
                ft.show(fragmentShop);
            }
            ft.commit();
        } else {
            //商品
            if (fragmentShop != null && !fragmentShop.isHidden()) {
                ft.hide(fragmentShop);
            }
            if (fragmentGoods == null) {
                fragmentGoods = new FragmentSearchGoods();
                ft.add(R.id.content_act_search, fragmentGoods);
            } else {
                ft.show(fragmentGoods);
            }
            ft.commit();
        }
    }

    private FragmentSearchShop fragmentShop;
    private FragmentSearchGoods fragmentGoods;

    android.support.v4.app.FragmentManager fm;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        fm = getSupportFragmentManager();
        setTabSelected("shop");
    }

    private void preView() {
        initFragment();
        tvMiddle.setText("搜索");
    }

    private void getData() {

    }

    private void setListener() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void preWidget() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        tvGoods = (TextView) findViewById(R.id.tv_goods_frag_groupon_list);
        tvShop = (TextView) findViewById(R.id.tv_shop_frag_groupon_list);
    }
}
