package com.fanwe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.DistributionMyQRCodeActivity;
import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.MyAccountActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.common.MGDict;
import com.fanwe.common.model.getMGDict.DictModel;
import com.fanwe.constant.Constant;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.user.view.AdviceActivity;
import com.fanwe.user.view.AttentionListActivity;
import com.fanwe.user.view.CollectListActivity;
import com.fanwe.user.view.FansActivity;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.user.view.UserHomeActivity;
import com.fanwe.user.view.WalletActivity;
import com.fanwe.user.view.customviews.RedDotView;
import com.fanwe.utils.MGStringFormatter;
import com.fanwe.utils.SDFormatUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by didik on 2016/9/13.
 */
public class MyFragment extends BaseFragment implements RedDotView.OnRedDotViewClickListener,
        View.OnClickListener, CallbackView {
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
    private View mErWeiMa;
    private View mKefu;
    private View mStar;
    private View mCollect;
    /**
     * 设置。
     */
    private View mSet;
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
    //    private ImageView mIvMsg;//消息
    private View mUpgrade;
    private TextView mTvRedShopCart;
    private TextView mTvRedFriends;
    private TextView mTvRedQuan;

    private String mKefuNum = "";


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        setmTitleType(Constant.TitleType.TITLE_NONE);
        return setContentView(R.layout.frag_mine);
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
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            modelPersonalHome = null;
            bindData();
        } else {
            httpHelper.getPersonalHome();
        }
    }

    private void initView() {
        initTopView();
        initMyOrders();
        initGridLayout();
        initPullToRefreshScrollView();
//        setUserData();
    }

    private void setUserData() {
        mUserFaceString = App.getInstance().getUserIcon();
        SDViewBinder.setTextView(mUserName, App.getInstance().getUserNickName(), "");
        SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIvUserFace,
                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
    }

    private void initTopView() {
        mMine = findViewById(R.id.fl_mine);
//        mIvMsg = ((ImageView) findViewById(R.id.iv_msg));
        mUpgrade = findViewById(R.id.ll_my_upgrade);

        mIvUserFace = ((CircleImageView) findViewById(R.id.iv_user_face));
        mUserName = ((TextView) findViewById(R.id.tv_username));
        mUserName2 = ((TextView) findViewById(R.id.tv_username2));

        mTvStarNum = ((TextView) findViewById(R.id.tv_star_num));
        mTvCollectNum = ((TextView) findViewById(R.id.tv_collect_number));
        mTvFansNum = ((TextView) findViewById(R.id.tv_fans_num));

        mStar = findViewById(R.id.ll_star);
        mCollect = findViewById(R.id.ll_collect);
        mFans = findViewById(R.id.ll_fans);

        mSet = findViewById(R.id.ll_set);

        mStar.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mSet.setOnClickListener(this);
        mFans.setOnClickListener(this);
        mMine.setOnClickListener(this);
//        mIvMsg.setOnClickListener(this);
        mIvUserFace.setOnClickListener(this);
        mUserName.setOnClickListener(this);
    }


    private void initGridLayout() {
        mShoppingCart = findViewById(R.id.ll_shopping_cart);
        mShop = findViewById(R.id.ll_shop);
        mWallet = findViewById(R.id.ll_wallet);
        mFriends = findViewById(R.id.ll_friends);
        mQuan = findViewById(R.id.ll_quan);
        mSuggestion = findViewById(R.id.ll_suggestion);
        mErWeiMa = findViewById(R.id.ll_erweima);
        mKefu = findViewById(R.id.ll_kefu);

        mTvRedShopCart = ((TextView) findViewById(R.id.tv_red_shopcart));
        mTvRedFriends = ((TextView) findViewById(R.id.tv_red_friends));
        mTvRedQuan = ((TextView) findViewById(R.id.tv_red_quan));

        mShoppingCart.setOnClickListener(this);
        mShop.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mFriends.setOnClickListener(this);
        mQuan.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mErWeiMa.setOnClickListener(this);
        mKefu.setOnClickListener(this);

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
        mPtrsvAll = (PullToRefreshScrollView) findViewById(R.id.frag_my_account_ptrsv_all);
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
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            httpHelper.getPersonalHome();
        }
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
            Intent intent = new Intent(getActivity(), DistributionStoreWapActivity.class);
            String id="";
            if(App.getInstance().getmUserCurrentInfo()!=null&&App.getInstance().getmUserCurrentInfo().getUserInfoNew()!=null) {
                id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
            }
            intent.putExtra("id", id);
            startActivity(intent);
        } else if (v == mFriends) {
            /*分销战队*/
            startActivity(DistributionMyXiaoMiActivity.class);

        } else if (v == mQuan) {
            /*消费券*/
            startActivity(MyCouponListActivity.class);
        } else if (v == mWallet) {
            //TODO 测试部分 佣金提现
            startActivity(WalletActivity.class);

        } else if (v == mSuggestion) {
            //建议
            startActivity(AdviceActivity.class);
        } else if (v == mStar) {
            /*关注*/
            startActivity(AttentionListActivity.class);
        } else if (v == mCollect) {
            /*收藏*/
            startActivity(CollectListActivity.class);
        } else if (v == mFans) {
            /*粉丝*/
            startActivity(FansActivity.class);
        } else if (v == mMine) {
            startActivity(UserHomeActivity.class);
        } else if (v == mAllOrder) {
            /*全部订单*/
            clickMyOrderView("all");
        } else if (v == mErWeiMa) {
            /*二维码名片*/
            startActivity(DistributionMyQRCodeActivity.class);
        } else if (v == mIvUserFace) {
            startActivity(UserHomeActivity.class);
        } else if (v == mUserName) {
            //个人主页。
//            startActivity(UserHomeActivity.class);
            //跳转至网红主页
            startActivity(UserHomeActivity.class);
        } else if (v == mKefu) {
            //客服电话
            clickKfPhone();
        } else if (v == mSet) {
            //设置页。
             /*点击了我的区域*/
            /*设置页面*/
            Intent intent = new Intent(getActivity(), MyAccountActivity.class);
            Bundle userData = new Bundle();
            userData.putString("user_face", mUserFaceString);
            intent.putExtras(userData);
            startActivity(intent);
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
            mUserFaceString = App.getInstance().getUserIcon();
            SDViewBinder.setTextView(mUserName, App.getInstance().getUserNickName(), "");
            SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIvUserFace,
                    ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
            MGLog.e("personalHome 为null");
            return;
        }
        //设置用户信息
        SDViewBinder.setTextView(mUserName, MGStringFormatter.getLimitedString(modelPersonalHome.getNick(), 10));
        String sign = "一生很难，一辈子很长，所以不能停止探寻有趣的生活。一个爱吃爱玩爱享受的人。";
        if (!TextUtils.isEmpty(modelPersonalHome.getPersonality())) {
            sign = modelPersonalHome.getPersonality();
        }
        SDViewBinder.setTextView(mUserName2, MGStringFormatter.getLimitedString(sign, 10));
        if (!TextUtils.isEmpty(modelPersonalHome.getPersonality())) {
            App.getInstance().getmUserCurrentInfo().getUserInfoNew().setRemark(modelPersonalHome.getPersonality());
        }
        mUserFaceString = modelPersonalHome.getIcon();
        SDViewBinder.setImageView(mUserFaceString, mIvUserFace);

        String fx_level = modelPersonalHome.getFx_level();
        App.getInstance().getmUserCurrentInfo().getUserInfoNew().setFx_level(fx_level);
        //粉丝
        String value = modelPersonalHome.getFans_count();

        mTvFansNum.setText(SDFormatUtil.formatTenThouthand(value));
        //关注
        mTvStarNum.setText(SDFormatUtil.formatTenThouthand(modelPersonalHome.getFocus_count()));
        //收藏
        mTvCollectNum.setText(SDFormatUtil.formatTenThouthand(modelPersonalHome.getCollect()));

        // 待付款订单数量
        setCustomRedText(mRDV_orderNotPay, modelPersonalHome.getPending_pay());
        //待使用
        setCustomRedText(mRDV_orderNotUse, modelPersonalHome.getPending_use());
        //待评价
        setCustomRedText(mRDV_orderNotComment, modelPersonalHome.getPending_evaluation());
        //退款
        setCustomRedText(mRDV_orderNotRefund, modelPersonalHome.getRefunt());

        //--------------红点数量
//        private TextView mTvRedShopCart;
//        private TextView mTvRedFriends;
//        private TextView mTvRedQuan;

        //购物车数量
        setRedText(mTvRedShopCart, modelPersonalHome.getCart_count());
        //战队数量
        setRedText(mTvRedFriends, modelPersonalHome.getFx_count());
        //券数量
        setRedText(mTvRedQuan, modelPersonalHome.getCoupon_count());

    }

    /**
     * 设置自定义的小红点数字显示
     *
     * @param redDotView
     * @param count
     */
    private void setCustomRedText(RedDotView redDotView, String count) {
        int intCount = Integer.parseInt(count);
        redDotView.setRedNum(intCount);
    }

    /**
     * 设置红点数量
     *
     * @param tvRed textView
     * @param count count
     */
    private void setRedText(TextView tvRed, String count) {
        int anInt = MGStringFormatter.getInt(count);
        if (anInt > 0) {
            tvRed.setText(anInt + "");
            tvRed.setVisibility(View.VISIBLE);
        } else {
            tvRed.setVisibility(View.GONE);
        }
    }

    /**
     * 客服电话
     */
    private void clickKfPhone() {
        if (!TextUtils.isEmpty(mKefuNum)) {
            callKeFu(mKefuNum);
            return;
        }

        List<DictModel> dict = MGDict.getDict();
        if (dict!=null){
            for (DictModel data : dict) {
                String dic_value = data.getDic_value();
                if ("support_phone".equals(dic_value)) {
                    mKefuNum = data.getDic_mean();
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(mKefuNum)) {
            MGToast.showToast("获取数据失败,请重试");
        } else {
            callKeFu(mKefuNum);
        }
    }

    private void callKeFu(String tel) {
        if(getActivity()!=null) {
            Intent intent = SDIntentUtil.getIntentCallPhone(tel);
            SDActivityUtil.startActivity(getActivity(), intent);
        }
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
                mPtrsvAll.onRefreshComplete();
                bindData();
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {
        switch (responseBody) {
            case UserConstants.PERSONALHOME:
                mPtrsvAll.onRefreshComplete();
                break;
        }
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
        if (httpHelper != null) {
            httpHelper.getPersonalHome();
        }
    }

    @Override
    public void onDestroy() {
        httpHelper = null;
        super.onDestroy();
    }
}
