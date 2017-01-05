package com.miguo.groupon.views;

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

public class SearchActivity extends FragmentActivity {
    private ImageView ivLeft;
    private TextView tvMiddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        preWidget();
        preView();
        setListener();
        getData();
    }


    //    FragmentFeaturedGroupon fragment;
//    FragmentSellerList fragment;
    FragmentGrouponList fragment;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        fragment = new FragmentFeaturedGroupon();
//        fragment = new FragmentSellerList();
        fragment = new FragmentGrouponList();
        fragment.refresh();
        ft.replace(R.id.content_act_search, fragment);
        ft.commit();
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
    }
}
