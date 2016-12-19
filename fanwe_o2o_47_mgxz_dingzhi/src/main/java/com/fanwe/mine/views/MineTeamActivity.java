package com.fanwe.mine.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.base.CallbackView;
import com.fanwe.fragment.MineTeamFragment;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.presents.UserHttpHelper;

import java.util.List;

/**
 * 我的战队
 * Created by qiang.chen on 2016/12/19.
 */

public class MineTeamActivity extends FragmentActivity implements CallbackView {
    private ImageView ivLeft;
    private TextView tvMiddle, tvOne, tvTwo, tvNumber, tvUpName;
    private UserHttpHelper userHttpHelper;
    private String up_id;
    private final int PAGE_ONE = 1;
    private final int PAGE_TWO = 2;
    private int pageType = PAGE_ONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_team);
        preWidget();
        preView();
        setListener();
        getData();
    }

    MineTeamFragment fragment;

    private void initFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new MineTeamFragment();
        ft.replace(R.id.act_my_xiaomi_fl, fragment);
        ft.commit();
    }

    private void preView() {
        initFragment();
        tvMiddle.setText("我的战队");
    }

    private void setListener() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvUpName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(up_id)) {
                    cilckUp();
                }
            }
        });
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOne.setEnabled(false);
                tvOne.setTextColor(getResources().getColor(R.color.main_color));
                tvTwo.setEnabled(true);
                tvTwo.setTextColor(getResources().getColor(R.color.text_fenxiao));
                clickTab(PAGE_ONE);
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTwo.setEnabled(false);
                tvTwo.setTextColor(getResources().getColor(R.color.main_color));
                tvOne.setEnabled(true);
                tvOne.setTextColor(getResources().getColor(R.color.text_fenxiao));
                clickTab(PAGE_TWO);
            }
        });
    }

    private void preWidget() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        tvOne = (TextView) findViewById(R.id.tv_textOne);
        tvTwo = (TextView) findViewById(R.id.tv_textTwo);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvUpName = (TextView) findViewById(R.id.tv_stationName);
    }

    /**
     * 跳转到"上线"的小店
     */
    private void cilckUp() {
        Intent intent = new Intent(this, DistributionStoreWapActivity.class);
        intent.putExtra("id", up_id);
        startActivity(intent);
    }

    /**
     * 切换大掌柜、店小二
     *
     * @param pageType
     */
    private void clickTab(int pageType) {
        this.pageType = pageType;
        tvNumber.setText("");
        getData();
    }

    /**
     * 请求数据
     */
    private void getData() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(this, this);
        }
        fragment.setPageType(pageType);
        userHttpHelper.getMyDistributionCorps(pageType + "", "", 1, 1, "");
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ResultMyDistributionCorps> results;
    private ResultMyDistributionCorps currResult;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.MY_DISTRIBUTION_CROPS.equals(method)) {
            results = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(results)) {
                        currResult = results.get(0);
                        up_id = currResult.getUp_id();
                        tvNumber.setText(currResult.getTotal());
                        tvUpName.setText(currResult.getUp_name());
                    }
                    if (fragment != null) {
                        fragment.getDataWithClear();
                    }
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}
