package com.miguo.live.views.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.IHelper;
import com.miguo.live.adapters.GiftViewPagerAdapter;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.DisplayUtil;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by didik on 2016/9/11.
 */
public class UserSendGiftPopHelper implements IHelper, View.OnClickListener {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private TextView mTvMoney;
    private TextView mTvSend;
    private ViewPager mGiftViewPager;
    private CircleIndicator mIndicator;

    public UserSendGiftPopHelper(Activity mActivity) {
        this.mActivity = mActivity;
        createPopWindow();
    }

    @Override
    public void onDestroy() {

    }

    private void createPopWindow() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.act_live_pop_send_gift, null);
        initContentView(contentView);
        //设置窗体的宽高属性
        int height = DisplayUtil.dp2px(mActivity, 300);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        //设置可以点击
        popupWindow.setTouchable(true);
        //设置背景
        popupWindow.setAnimationStyle(R.style.pop_translate);
//        ColorDrawable background=new ColorDrawable(0x4F000000);
        BitmapDrawable background = new BitmapDrawable();
        //设置背景+
        popupWindow.setBackgroundDrawable(background);
//
        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

    }

    private void initContentView(View contentView) {
        mTvMoney = ((TextView) contentView.findViewById(R.id.tv_money));
        mTvSend = ((TextView) contentView.findViewById(R.id.tv_send));

        mGiftViewPager = ((ViewPager) contentView.findViewById(R.id.viewpager_gift));
        mIndicator = ((CircleIndicator) contentView.findViewById(R.id.indicator_circle));

        /*init viewpager*/
        GiftViewPagerAdapter mGiftAdapter=new GiftViewPagerAdapter(mActivity,null,mTvSend);
        mGiftViewPager.setAdapter(mGiftAdapter);
        mIndicator.setViewPager(mGiftViewPager);

        mGiftViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GiftViewPagerAdapter adapter = (GiftViewPagerAdapter) mGiftViewPager.getAdapter();
                boolean selected = adapter.getSelected(position);
                Object selectedItemInfo = adapter.getSelectedItemInfo();
                MGToast.showToast(selectedItemInfo.toString());
                if (selected){
                    mTvSend.setEnabled(true);
                }else {
                    mTvSend.setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTvMoney.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
    }

    /*显示*/
    public void show(){
        /**
         * 进去的时候选择哪个界面,tab与viewpager 需要保持一致
         */
        if (popupWindow!=null){
            ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM,0,0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==mTvMoney){
            clickMoney();
        }else if (v==mTvSend){
            clickSend();
        }
    }

    private void clickSend() {
        MGToast.showToast("clickSend");
    }

    private void clickMoney() {
        MGToast.showToast("clickMoney");
    }
}