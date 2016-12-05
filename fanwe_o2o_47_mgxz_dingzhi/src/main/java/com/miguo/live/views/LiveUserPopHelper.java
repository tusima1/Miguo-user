package com.miguo.live.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.base.CallbackView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerDetailInfo;
import com.miguo.live.adapters.LiveViewPagerItemAdapter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.PagerMainHostView;
import com.miguo.live.views.customviews.PagerRedPacketView;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.MGUIUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

/**
 * Created by didik on 2016/8/4.
 * 商品等viewpager界面
 */
public class LiveUserPopHelper implements IHelper, View.OnClickListener {
    private View rootView;
    private ViewPager.OnPageChangeListener listener;
    private Activity mActivity;
    private int prePosition = 0;//之前一个位置
    private PopupWindow popupWindow;
    LiveViewPagerItemAdapter adapter1;
    private ImageView mShopCart;
    /**
     * 用户取得的红包列表。
     */
    private PagerRedPacketAdapter mRedPacketAdapter;
    /**
     * 门店详情。
     */
    private SellerDetailInfo mSellerDetailInfo;
    /**
     * 个人所取得的红包结果列表。
     */
    private CallbackView mCallbackView;

    private int currentPosition=0;

    private PagerBaoBaoAdapter mBaobaoAdapter;
    private ViewPager viewPager;


    public LiveUserPopHelper(Activity activity, View rootView, CallbackView mCallbackView, PagerRedPacketAdapter mRedPacketAdapter, PagerBaoBaoAdapter mBaobaoAdapter, int currentPosition) {
        this.mRedPacketAdapter = mRedPacketAdapter;
        this.mActivity=activity;
        this.rootView=rootView;
        this.mCallbackView = mCallbackView;
        this.mBaobaoAdapter = mBaobaoAdapter;
        this.currentPosition = currentPosition;
        createPopWindow();

    }


    private void createPopWindow() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.act_live_pop_viewpager, null);
        mShopCart = ((ImageView) contentView.findViewById(R.id.iv_shop_cart));
        mShopCart.setOnClickListener(this);
        initContentView(contentView);
        //设置窗体的宽高属性
        int height = DisplayUtil.dp2px(mActivity, 350);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .MATCH_PARENT, height);
        //设置可以点击
        popupWindow.setTouchable(true);
        //设置背景
        popupWindow.setAnimationStyle(R.style.pop_translate);
//        ColorDrawable background=new ColorDrawable(0x4F000000);
        BitmapDrawable background=new BitmapDrawable();
        //设置背景+
        popupWindow.setBackgroundDrawable(background);
//
        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

    }
    /*显示*/
    public void show(){
        /**
         * 进去的时候选择哪个界面,tab与viewpager 需要保持一致
         */
        listener.onPageSelected(currentPosition);
        viewPager.setCurrentItem(currentPosition,true);
        if (popupWindow!=null){
            popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        }
    }

    public void dismissLiveUserPop(){
        popupWindow.dismiss();
    }



    private void initContentView(final View contentView) {
        ViewPagerItems pagerItems = new ViewPagerItems(mActivity);

        ViewPagerItem item1 = ViewPagerItem.of("title1", R.layout.item_pager_baobao);
        ViewPagerItem item2 = ViewPagerItem.of("title1", R.layout.item_pager_baobao);
        ViewPagerItem item3 = ViewPagerItem.of("title1", R.layout.item_pager_baobao);
//        ViewPagerItem item4 = ViewPagerItem.of("title1", R.layout.item_pager_baobao);

        pagerItems.add(item1);
        pagerItems.add(item2);
        pagerItems.add(item3);
//        pagerItems.add(item4);

        adapter1 = new LiveViewPagerItemAdapter(pagerItems,mCallbackView,mRedPacketAdapter,mBaobaoAdapter);
        if(mSellerDetailInfo!=null){
            adapter1.setmSellerDetailInfo(mSellerDetailInfo);
        }


        viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter1);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) contentView.findViewById(R.id.viewpagertab);



        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View inflate = LayoutInflater.from(mActivity).inflate(R.layout
                        .item_viewpager, container, false);
                TextView textView = (TextView) inflate.findViewById(R.id.tv_show);
                switch (position) {
                    case 0:
                        textView.setText("镇店之宝");
                        break;
                    case 1:
                        textView.setText("主场");
                        break;
                    case 2:
                        textView.setText("红包");
                        break;
//                    case 3:
//                        textView.setText("服务");
//                        break;
                }
                return inflate;
            }
        });
        viewPagerTab.setViewPager(viewPager);
        listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position != prePosition) {
                    //不可见
                    TextView tv = (TextView) viewPagerTab.getTabAt(prePosition).findViewById(R.id
                            .tv_show);
                    ImageView iv = (ImageView) viewPagerTab.getTabAt(prePosition).findViewById(R
                            .id.iv_show);
                    tv.setTextColor(Color.parseColor("#989898"));
                    iv.setVisibility(View.GONE);
                }
                //可见
                TextView tv = (TextView) viewPagerTab.getTabAt(position).findViewById(R.id.tv_show);
                ImageView iv = (ImageView) viewPagerTab.getTabAt(position).findViewById(R.id
                        .iv_show);
                tv.setTextColor(Color.WHITE);
                iv.setVisibility(View.VISIBLE);
                prePosition = position;

                //------------------------处理adapter里面的内容-----------------
                View currentView=  adapter1.getPage(position);
                if(contentView==null){
                    return;
                }
                if(currentView instanceof PagerMainHostView){
                    if(mSellerDetailInfo!=null){
                        ((PagerMainHostView)currentView).setmSellerDetailInfo(mSellerDetailInfo);
                        ((PagerMainHostView)currentView).onEntityChange();
                    }
                }else if(currentView instanceof PagerRedPacketView){
                    ((PagerRedPacketView)currentView).setmCallbackView(mCallbackView);
                    ((PagerRedPacketView)currentView).refreshData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPagerTab.setOnPageChangeListener(listener);
    }


    @Override
    public void onDestroy() {

    }
   public void setmSellerDetailInfo(SellerDetailInfo mSellerDetailInfo) {
        this.mSellerDetailInfo = mSellerDetailInfo;
        adapter1.setmSellerDetailInfo(mSellerDetailInfo);
    }
    public void refreshSellerDetailInfo(){
        if(adapter1!=null) {
            adapter1.setmSellerDetailInfo(mSellerDetailInfo);
            adapter1.refreshSellerDetailInfo();
        }
    }


    @Override
    public void onClick(View v) {
        if (v==mShopCart){
            clickShopCart(v);
        }
    }

    /**
     * 点击了购物车
     * @param v
     */
    private void clickShopCart(View v) {
        v.setClickable(false);
        startAnimation(v);
    }
    public void startAnimation(final View v){
        ObjectAnimator anim=ObjectAnimator.ofFloat(v,"dd",1.0f,1.3f,0.8f,1.0f).setDuration(500);
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                v.setScaleX(animatedValue);
                v.setScaleY(animatedValue);
            }
        });
        MGUIUtil.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.startActivity(new Intent(mActivity, ShopCartActivity.class));
                v.setClickable(true);
            }
        },500);

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public PagerBaoBaoAdapter getmBaobaoAdapter() {
        return mBaobaoAdapter;
    }

    public void setmBaobaoAdapter(PagerBaoBaoAdapter mBaobaoAdapter) {
        this.mBaobaoAdapter = mBaobaoAdapter;
    }
}
