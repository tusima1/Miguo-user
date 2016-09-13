package com.fanwe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.DistributionWithdrawActivity;
import com.fanwe.MemberRankActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.Constant;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.user.view.customviews.RedDotView;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.miguo.live.views.view.UserSendGiftPopHelper;
import com.miguo.utils.MGLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/9/13.
 */
public class MyFragment2 extends BaseFragment implements RedDotView
        .OnRedDotViewClickListener, View.OnClickListener,CallbackView2 {

    private RedDotView mRDV_orderNotPay;//待付款订单
    private RedDotView mRDV_orderNotUse;//待使用
    private RedDotView mRDV_orderNotComment;//待评价
    private RedDotView mRDV_orderNotRefund;//退款
    private View mShoppingCart;
    private View mShop;
    private View mWallet;
    private View mFriends;
    private View mQuan;
    private View mSuggestion;
    private View mStar;
    private View mCollect;
    private View mFans;
    private View mMine;
    private CircleImageView mIvUserFace;
    private TextView mUserName;
    private TextView mUserName2;
    private TextView mTvStarNum;
    private TextView mTvCollectNum;
    private TextView mTvFansNum;
    private PullToRefreshScrollView mPtrsvAll;
    private String mUserFaceString = "";
    private UserHttpHelper httpHelper;
    private ModelPersonalHome modelPersonalHome;
    private View mAllOrder;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(Constant.TitleType.TITLE_NONE);
        return setContentView(R.layout.activity_test_my_fragment);
    }


    @Override
    protected void init() {
        super.init();
        initView();
        httpHelper = new UserHttpHelper(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        httpHelper.getPersonalHome();
    }

    private void initView() {
        initTopView();
        initMyOrders();
        initGridLayout();
        initPullToRefreshScrollView();
        setUserData();
    }

    private void setUserData() {
        mUserFaceString = App.getInstance().getUserIcon();
        SDViewBinder.setTextView(mUserName, App.getInstance().getUserNickName(), "");
        SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIvUserFace,
                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
    }

    private void initTopView() {
        mMine = findViewById(R.id.fl_mine);

        mIvUserFace = ((CircleImageView) findViewById(R.id.iv_user_face));
        mUserName = ((TextView) findViewById(R.id.tv_username));
        mUserName2 = ((TextView) findViewById(R.id.tv_username2));

        mTvStarNum = ((TextView) findViewById(R.id.tv_star_num));
        mTvCollectNum = ((TextView) findViewById(R.id.tv_collect_number));
        mTvFansNum = ((TextView) findViewById(R.id.tv_fans_num));

        mStar = findViewById(R.id.ll_star);
        mCollect = findViewById(R.id.ll_collect);
        mFans = findViewById(R.id.ll_fans);

        mStar.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mFans.setOnClickListener(this);
        mMine.setOnClickListener(this);
    }


    private void initGridLayout() {
        mShoppingCart = findViewById(R.id.ll_shopping_cart);
        mShop = findViewById(R.id.ll_shop);
        mWallet = findViewById(R.id.ll_wallet);
        mFriends = findViewById(R.id.ll_friends);
        mQuan = findViewById(R.id.ll_quan);
        mSuggestion = findViewById(R.id.ll_suggestion);

        mShoppingCart.setOnClickListener(this);
        mShop.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mFriends.setOnClickListener(this);
        mQuan.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
    }

    private void initMyOrders() {
        mAllOrder = findViewById(R.id.fl_all_order);
        mRDV_orderNotPay = ((RedDotView) findViewById(R.id.rdv_order_not_paid));
        mRDV_orderNotUse = ((RedDotView) findViewById(R.id.rdv_order_not_used));
        mRDV_orderNotComment = ((RedDotView) findViewById(R.id.rdv_order_not_commented));
        mRDV_orderNotRefund = ((RedDotView) findViewById(R.id.rdv_order_to_refund));

        int color = Color.parseColor("#999999");

        mRDV_orderNotPay.setAllParams("待付款", R.drawable.ic_pay, 0, color);
        mRDV_orderNotUse.setAllParams("待使用", R.drawable.ic_use, 0, color);
        mRDV_orderNotComment.setAllParams("待评价", R.drawable.ic_talk, 0, color);
        mRDV_orderNotRefund.setAllParams("退款", R.drawable.ic_refund, 0, color);

        mRDV_orderNotPay.setOnRedDotViewClickListener(this);
        mRDV_orderNotUse.setOnRedDotViewClickListener(this);
        mRDV_orderNotComment.setOnRedDotViewClickListener(this);
        mRDV_orderNotRefund.setOnRedDotViewClickListener(this);
        mAllOrder.setOnClickListener(this);
    }
    private void initPullToRefreshScrollView() {
        mPtrsvAll= (PullToRefreshScrollView) findViewById(R.id.frag_my_account_ptrsv_all);
        mPtrsvAll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPtrsvAll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestMyAccount();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }

        });
        mPtrsvAll.setRefreshing();
    }
    /**
     * 请求我的账户接口
     */
    public void requestMyAccount() {
        httpHelper.getPersonalHome();
    }

    @Override
    public void onRedDotViewClick(View v) {
        if (v == mRDV_orderNotPay) {
            clickMyOrderView("pay_wait");
        } else if (v == mRDV_orderNotUse) {
            clickMyOrderView("use_wait");
        } else if (v == mRDV_orderNotComment) {
            clickMyOrderView("comment_wait");
        } else if (v == mRDV_orderNotRefund) {
            clickMyOrderView("refund");
        }
    }

    /*点击订单*/
    private void clickMyOrderView(String key) {
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, key);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mShoppingCart) {
            /*购物车*/
            startActivity(ShopCartActivity.class);
        } else if (v == mShop) {
            /*我的小店*/
            startActivity(DistributionStoreWapActivity.class);
        } else if (v == mFriends) {
            /*分销战队*/
            startActivity(DistributionMyXiaoMiActivity.class);
        } else if (v == mQuan) {
            /*消费券*/
            startActivity(MyCouponListActivity.class);
        } else if (v == mWallet) {
            //TODO 测试部分 佣金提现
            String fx_level = modelPersonalHome.getFx_level();
            int level = MGStringFormatter.getInt(fx_level);
            if (level < 2) {
                Intent intent = new Intent(getActivity(), MemberRankActivity.class);
                startActivity(intent);
                SDToast.showToast("您还没有提现权限");
            } else {
                Intent intent = new Intent(getActivity(), DistributionWithdrawActivity.class);
                intent.putExtra("money", modelPersonalHome.getWithdrawals());
                intent.putExtra("money_type",2);
                startActivity(intent);
            }
        } else if (v == mSuggestion) {
//            startActivity(RedPacketListActivity.class);
            UserSendGiftPopHelper helper=new UserSendGiftPopHelper(getActivity());
            helper.show();
        }else if (v==mStar){
            /*关注*/
        }else if (v==mCollect){
            /*收藏*/
        }else if (v==mFans){
            /*粉丝*/
        }else if (v==mMine){
            /*点击了我的区域*/
        }else if (v==mAllOrder){
            /*全部订单*/
            clickMyOrderView("all");
        }
    }

    /**
     * goto 新Activity
     *
     * @param clazz 类
     */
    public void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void bindData() {
        if (modelPersonalHome == null) {
            MGLog.e("personalHome 为null");
            return;
        }
        String fx_level = modelPersonalHome.getFx_level();
        Drawable rankDrawable=null;
        if ("1".equals(fx_level)) {
            rankDrawable=getResources().getDrawable(R.drawable.ic_rank_3);
        } else if ("2".equals(fx_level)) {
            rankDrawable=getResources().getDrawable(R.drawable.ic_rank_2);
        } else if ("3".equals(fx_level)) {
            rankDrawable=getResources().getDrawable(R.drawable.ic_rank_1);
        }
        if (rankDrawable!=null){
            rankDrawable.setBounds(0, 0, rankDrawable.getMinimumWidth(), rankDrawable.getMinimumHeight());
            mUserName.setCompoundDrawables(null,null,rankDrawable,null);
        }
        //粉丝
        mTvFansNum.setText(modelPersonalHome.getFans_count());
        //TODO 收藏 & 关注

        // 待付款订单数量
        String notPaidCountStr = modelPersonalHome.getPending_pay();
        Integer notPaidCount = Integer.parseInt(notPaidCountStr);
        mRDV_orderNotPay.setRedNum(notPaidCount);

        //待使用
        String readyForUseCountStr = modelPersonalHome.getPending_use();
        Integer readyForUseCount = Integer.parseInt(readyForUseCountStr);
        mRDV_orderNotUse.setRedNum(readyForUseCount);
        //待评价
        String notCommentedCountStr = modelPersonalHome.getPending_evaluation();
        Integer notCommentedCount = Integer.parseInt(notCommentedCountStr);
        mRDV_orderNotComment.setRedNum(notCommentedCount);

        //退款
        String refundCountStr = modelPersonalHome.getRefunt();
        Integer refundCount = Integer.parseInt(refundCountStr);
        mRDV_orderNotRefund.setRedNum(refundCount);
    }

    //------------http start---------------
    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case UserConstants.PERSONALHOME:
                modelPersonalHome = (ModelPersonalHome) datas.get(0);
                bindData();
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        switch (method) {
            case UserConstants.PERSONALHOME:
                mPtrsvAll.onRefreshComplete();
                break;
        }
    }

    //--------------http end----------------
    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    public void refreshFragment() {
        httpHelper.getPersonalHome();
    }
}
