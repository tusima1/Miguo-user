package com.fanwe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.AccountMoneyActivity;
import com.fanwe.DistributionMyQRCodeActivity;
import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.DistributionWithdrawActivity;
import com.fanwe.MemberRankActivity;
import com.fanwe.MyAccountActivity;
import com.fanwe.MyCollectionActivity;
import com.fanwe.MyCommentActivity;
import com.fanwe.MyCouponListActivity;
import com.fanwe.MyEventListActivity;
import com.fanwe.MyLotteryActivity;
import com.fanwe.MyOrderListActivity;
import com.fanwe.MyRedEnvelopeActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.UploadUserHeadActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.handler.PhotoHandler.PhotoHandlerListener;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.MessageCount;
import com.fanwe.model.MyDistributionUser_dataModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_center_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.view.customviews.RedDotView;
import com.fanwe.utils.MoneyFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.io.File;
import java.util.Arrays;

/**
 * 我的fragment
 *
 * @author js02
 */
public class MyFragment extends BaseFragment implements RedDotView.OnRedDotViewClickListener {
    @ViewInject(R.id.frag_my_account_ptrsv_all)
    private PullToRefreshScrollView mPtrsvAll = null;


    @ViewInject(R.id.iv_user_avatar)
    private ImageView mIv_user_avatar = null; // 用户头像

    @ViewInject(R.id.frag_my_account_tv_username)
    private TextView mTvUsername = null; // 用户名

    @ViewInject(R.id.frag_my_account_tv_balance)
    private TextView mTvBalance = null; // 账户余额

    @ViewInject(R.id.frag_my_account_tv_user_score)
    private TextView mTv_user_score = null;// 积分


    @ViewInject(R.id.frag_my_account_ll_group_coupons)
    private LinearLayout mLlGroupCoupons = null;

    @ViewInject(R.id.frag_my_account_tv_coupons)
    private TextView mTvCoupons = null; // 我的优惠券数量

    @ViewInject(R.id.hongbao_count)
    private TextView mHongbaoCount;//红包

    @ViewInject(R.id.iv_vip)
    private ImageView mIv_vip;//vip等级的图标

    /*
     * @ViewInject(R.id.ll_my_friend_circle) private LinearLayout
     * mLl_my_friend_circle; // 我的朋友圈
     */
    @ViewInject(R.id.btn_view_all_orders)
    private View viewAllOrdersButton;

//    @ViewInject(R.id.order_not_paid)
//    private View orderNotPaidView;// 待付款订单
//
//    @ViewInject(R.id.order_not_used)
//    private View orderNotUsedView;// 待使用订单
//
//    @ViewInject(R.id.order_not_commented)
//    private View orderNotCommentedView;// 待评价订单
//
//    @ViewInject(R.id.order_to_refund)
//    private View orderToRefundView;// 待退款订单


    @ViewInject(R.id.ll_look_dianpu)
    private View ll_lookDianpu;

    @ViewInject(R.id.ll_my_xiaomi)
    private View ll_myXiaomi;

    @ViewInject(R.id.ll_erweima)
    private View ll_erWeima;

    @ViewInject(R.id.ll_order_has_pay)
    private LinearLayout mLl_order_has_pay; // 已付款订单

    @ViewInject(R.id.tv_order_not_comment)
    private TextView mTv_order_not_comment;

    @ViewInject(R.id.ll_order_takeaway_reservation)
    private LinearLayout mLl_order_takeaway_reservation; // 外卖预定订单

    // 我的红包
    @ViewInject(R.id.ll_my_red_money)
    private LinearLayout mLl_my_red_money;


    @ViewInject(R.id.ll_my_collect)
    private LinearLayout mLl_my_collect; // 我的收藏

    /*
     * @ViewInject(R.id.ll_my_event) private LinearLayout mLl_my_event; // 我的活动
     *
     * @ViewInject(R.id.ll_my_lottery) private LinearLayout mLl_my_lottery; //
     * 我的抽奖
     */
    @ViewInject(R.id.ll_comments)
    private LinearLayout mLl_comments;

    @ViewInject(R.id.tv_comments_number)
    private TextView mTv_comments;

    @ViewInject(R.id.tv_collect_number)
    private TextView mTv_collect;

    @ViewInject(R.id.ll_my_asset)
    private LinearLayout mLl_my_asset;

    @ViewInject(R.id.ll_shopping_cart)
    private LinearLayout mLl_shopping_cart;// 购物车

    //	@ViewInject(R.id.iv_redDot)
//	private ImageView mRedDot;
//    @ViewInject(R.id.iv_red_big)
//    private ImageView redBig;
//
//    @ViewInject(R.id.iv_red_more)
//    private ImageView redMore;
//
//    @ViewInject(R.id.tv_red_count)
//    private TextView redCount;
//    private BadgeView redDot;

//    private BadgeView orderNotPaidBadge;
//    private BadgeView orderReadyForUseBadge;
//    private BadgeView orderNotCommentedBadge;
//    private BadgeView orderRefundBadge;
//
//    private TextView orderNotPaidCountView;
//    private TextView orderReadyForUseCountView;
//    private TextView orderNotCommentedCountView;
//    private TextView orderRefundCountView;

    private PageModel mPage = new PageModel();
//    private TextView mTv_totalMomey;// 总佣金
    private TextView mTv_tixian;// 提现现金
    private TextView mTv_used;// 已使用现金
    // private TextView mTv_place;
    /*
     * private LinearLayout mll_hongbao; private TextView mTv_hongbao;
	 */
    private TextView mTv_Predict;

    private HttpHandler<String> mHttpHandler;

    private PhotoHandler mPhotoHandler;

    protected User_center_indexActModel mActModel;


    private String mUserFaceString = "";
    private RedDotView mRDV_Comsume;//消费券
    private RedDotView mRDV_MyShop;//我的小店
    private RedDotView mRDV_MyFriend;//我的战队
    private RedDotView mRDV_MyNameCard;//我的名片
    private TextView mTv_FansNum;//粉丝数量
    private TextView mTv_Msg;//消息数量

    private RedDotView mRDV_orderNotPay;//待付款订单
    private RedDotView mRDV_orderNotUse;//待使用
    private RedDotView mRDV_orderNotComment;//待评价
    private RedDotView mRDV_orderNotRefund;//退款



    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE_NONE);
        return setContentView(R.layout.frag_my);
    }

    @Override
    protected void init() {
        super.init();
        initUserTopView();
        initPhotoHandler();
        registerClick();
        initViewState();
        initPullToRefreshScrollView();
        initMyOrders();
        addTopView();

        setView();
    }

    private void setView() {
        mUserFaceString = App.getInstance().getUserIcon();
        SDViewBinder.setTextView(mTvUsername, App.getInstance().getUserNickName(), "");
        SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIv_user_avatar,
                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
    }

    private void addTopView() {
//        redDot = (BadgeView) findViewById(R.id.badge_red_dot);
        // mTv_place=(TextView)findViewById(R.id.frag_my_tv_place);
//        mTv_totalMomey = (TextView) findViewById(R.id.frag_my_account);//总资产
        LinearLayout mLl_Predict = (LinearLayout) findViewById(R.id.ll_tixian_Predict);
        mTv_Predict = (TextView) findViewById(R.id.frag_my_account_tv_user_money);
        LinearLayout mll_tixian = (LinearLayout) findViewById(R.id.ll_tixian_Actual);
        mTv_tixian = (TextView) findViewById(R.id.my_account_tv_user_score);
        mTv_used = (TextView) findViewById(R.id.my_account_tv_user_score_2);
        /*
         * mll_hongbao=(LinearLayout)findViewById(R.id.ll_my_hongbao);
		 * mTv_hongbao=(TextView) findViewById(R.id.tv_my_hongbao);
		 */
//        LinearLayout mLl_member = (LinearLayout) findViewById(R.id.ll_member);
        /**
         * 总佣金
         */
//        mTv_totalMomey.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WithdrawLogActivity.class);
//                startActivity(intent);
//            }
//        });
        /**
         * 实际提现
         */
        mll_tixian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActModel.getLevel_id() < 2) {
                    Intent intent = new Intent(getActivity(), MemberRankActivity.class);
                    startActivity(intent);
                    SDToast.showToast("您还没有提现权限");
                } else {
                    Intent intent = new Intent(getActivity(), DistributionWithdrawActivity.class);
                    intent.putExtra("money", mActModel.getUser_data().getFx_money() + "");
                    startActivity(intent);
                }
            }
        });
        /**
         * 预计提现
         */
        mLl_Predict.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DistributionMyXiaoMiActivity.class);
                intent.putExtra("yes", true);
                intent.putExtra("money", mActModel.getYuji());
                intent.putExtra("up_name", mActModel.getUp_name());
                intent.putExtra("up_id", mActModel.getUp_id());
                startActivity(intent);
            }
        });

        /**
         * 我的红包
         *//*
             * mll_hongbao.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { startActivity(new
			 * Intent(getActivity(), MyRedEnvelopeActivity.class)); } });
			 */

        /**
         * 我的会员
         */
//        mLl_member.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MemberRankActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void initMyOrders() {
        ImageView icon;
        TextView textView;
        int colorId = getResources().getColor(R.color.white);
        //--------------------------------------------

        mRDV_Comsume = ((RedDotView) findViewById(R.id.rdv_consume));
        mRDV_MyShop = ((RedDotView) findViewById(R.id.rdv_my_shop));
        mRDV_MyFriend = ((RedDotView) findViewById(R.id.rdv_my_friend));
        mRDV_MyNameCard = ((RedDotView) findViewById(R.id.rdv_my_name_card));

        mRDV_orderNotPay = ((RedDotView) findViewById(R.id.rdv_order_not_paid));
        mRDV_orderNotUse = ((RedDotView) findViewById(R.id.rdv_order_not_used));
        mRDV_orderNotComment = ((RedDotView) findViewById(R.id.rdv_order_not_commented));
        mRDV_orderNotRefund = ((RedDotView) findViewById(R.id.rdv_order_to_refund));

        int color = Color.parseColor("#999999");
        mRDV_Comsume.setAllParams("消费券",R.drawable.bg_groupvacher,0,Color.WHITE);
        mRDV_MyShop.setAllParams("我的小店",R.drawable.bg_xiaodian,0,Color.WHITE);
        mRDV_MyFriend.setAllParams("我的战队",R.drawable.bg_xiaomi,0,Color.WHITE);
        mRDV_MyNameCard.setAllParams("我的名片",R.drawable.bg_erweima,0,Color.WHITE);

        mRDV_orderNotPay.setAllParams("代付款",R.drawable.ic_obligation,0,color);
        mRDV_orderNotUse.setAllParams("待使用",R.drawable.ic_ready_for_use,0,color);
        mRDV_orderNotComment.setAllParams("待评价",R.drawable.ic_to_rank,0,color);
        mRDV_orderNotRefund.setAllParams("退款",R.drawable.ic_refund,0,color);

        mRDV_Comsume.setOnRedDotViewClickListener(this);
        mRDV_MyShop.setOnRedDotViewClickListener(this);
        mRDV_MyFriend.setOnRedDotViewClickListener(this);
        mRDV_MyNameCard.setOnRedDotViewClickListener(this);
        mRDV_orderNotPay.setOnRedDotViewClickListener(this);
        mRDV_orderNotUse.setOnRedDotViewClickListener(this);
        mRDV_orderNotComment.setOnRedDotViewClickListener(this);
        mRDV_orderNotRefund.setOnRedDotViewClickListener(this);

        //_______________

//        int color = getResources().getColor(R.color.text_fenxiao);
//        icon = (ImageView) orderNotPaidView.findViewById(R.id.icon);
//        icon.setMinimumWidth(20);
//        icon.setMinimumHeight(20);
//        icon.setImageResource(R.drawable.ic_obligation);
//        textView = (TextView) orderNotPaidView.findViewById(R.id.text);
//        textView.setTextColor(color);
//        textView.setText("待付款");
//        orderNotPaidBadge = (BadgeView) orderNotPaidView.findViewById(R.id.badge);
//        orderNotPaidCountView = (TextView) orderNotPaidView.findViewById(R.id.count);
//
//        icon = (ImageView) orderNotUsedView.findViewById(R.id.icon);
//        icon.setMinimumWidth(20);
//        icon.setMinimumHeight(20);
//        icon.setImageResource(R.drawable.ic_ready_for_use);
//        textView = (TextView) orderNotUsedView.findViewById(R.id.text);
//        textView.setTextColor(color);
//        textView.setText("待使用");
//        orderReadyForUseBadge = (BadgeView) orderNotUsedView.findViewById(R.id.badge);
//        orderReadyForUseCountView = (TextView) orderNotUsedView.findViewById(R.id.count);
//
//        icon = (ImageView) orderNotCommentedView.findViewById(R.id.icon);
//        icon.setMinimumWidth(20);
//        icon.setMinimumHeight(20);
//        icon.setImageResource(R.drawable.ic_to_rank);
//        textView = (TextView) orderNotCommentedView.findViewById(R.id.text);
//        textView.setTextColor(color);
//        textView.setText("待评价");
//        orderNotCommentedBadge = (BadgeView) orderNotCommentedView.findViewById(R.id.badge);
//        orderNotCommentedCountView = (TextView) orderNotCommentedView.findViewById(R.id.count);
//
//        icon = (ImageView) orderToRefundView.findViewById(R.id.icon);
//        icon.setMinimumWidth(20);
//        icon.setMinimumHeight(20);
//        icon.setImageResource(R.drawable.ic_refund);
//        textView = (TextView) orderToRefundView.findViewById(R.id.text);
//        textView.setTextColor(color);
//        textView.setText("退款");
//        orderRefundBadge = (BadgeView) orderToRefundView.findViewById(R.id.badge);
//        orderRefundCountView = (TextView) orderToRefundView.findViewById(R.id.count);

    }

    private void initPhotoHandler() {
        mPhotoHandler = new PhotoHandler(this);
        mPhotoHandler.setmListener(new PhotoHandlerListener() {

            @Override
            public void onResultFromCamera(File file) {
                if (file != null && file.exists()) {
                    Intent intent = new Intent(getActivity(), UploadUserHeadActivity.class);
                    intent.putExtra(UploadUserHeadActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
                    startActivity(intent);
                }
            }

            @Override
            public void onResultFromAlbum(File file) {
                if (file != null && file.exists()) {
                    Intent intent = new Intent(getActivity(), UploadUserHeadActivity.class);
                    intent.putExtra(UploadUserHeadActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(String msg) {

                SDToast.showToast(msg);
            }
        });
    }

    private void initViewState() {
        Init_indexActModel model = AppRuntimeWorker.getInitActModel();
        if (model == null) {
            return;
        }

        if (AppRuntimeWorker.getIs_plugin_dc() == 1) {
            SDViewUtil.show(mLl_order_takeaway_reservation);
        } else {
            SDViewUtil.hide(mLl_order_takeaway_reservation);
        }
    }

    private void initPullToRefreshScrollView() {
        mPtrsvAll.setMode(Mode.PULL_FROM_START);
        mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initViewState();
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
        if (AppHelper.getLocalUser() == null) {
            return;
        }
        if (mHttpHandler != null) {
            mHttpHandler.cancel();
        }

        RequestModel model = new RequestModel();
        model.putCtl("user_center");
        model.putUser();
        SDRequestCallBack<User_center_indexActModel> handler = new SDRequestCallBack<User_center_indexActModel>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    mActModel = actModel;
                    bindData(actModel);
                }
            }

            @Override
            public void onFinish() {
                mPtrsvAll.onRefreshComplete();
            }
        };
        mHttpHandler = InterfaceServer.getInstance().requestInterface(model, handler);
    }

    protected void bindData(User_center_indexActModel actModel) {
        if (actModel == null) {
            return;
        }

        MyDistributionUser_dataModel userData = mActModel.getUser_data();
        if (userData == null) {
            return;
        }
        if (mActModel.getLevel_id() == 1) {
//            SDViewBinder.setTextView(mTv_vip, "青铜", "未找到");
            mIv_vip.setImageResource(R.drawable.ic_rank_3);
        } else if (mActModel.getLevel_id() == 2) {
            mIv_vip.setImageResource(R.drawable.ic_rank_2);
//            SDViewBinder.setTextView(mTv_vip, "白金", "未找到");
        } else if (mActModel.getLevel_id() == 3) {
//            SDViewBinder.setTextView(mTv_vip, "钻石", "未找到");
            mIv_vip.setImageResource(R.drawable.ic_rank_1);
        }

        SDViewBinder.setTextView(mTv_tixian, MoneyFormat.format(userData.getFx_money()));
//        SDViewBinder.setTextView(mTv_totalMomey, MoneyFormat.format(userData.getFx_total_balance()));

        SDViewBinder.setTextView(mTv_used, MoneyFormat.format(userData.getFx_total_money() - userData.getFx_money()), "￥ 0.00");

        SDViewBinder.setTextView(mTv_Predict, MoneyFormat.format(mActModel.getYuji()), "￥ 0.00");
//        SDViewBinder.setImageView(actModel.getUser_avatar(), mIv_user_avatar,
//                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());

//		SDViewBinder.setTextView(mTvUsername, actModel.getUser_name(), "未找到");

        SDViewBinder.setTextView(mTvBalance, MoneyFormat.format(actModel.getUser_money()), "￥ 0.00");

        SDViewBinder.setTextView(mTv_user_score, actModel.getUser_score(), "0");


        SDViewBinder.setTextView(mTvCoupons, MoneyFormat.format(userData.getFx_total_balance()), "0");
        SDViewBinder.setTextView(mHongbaoCount, actModel.getRed_packet_count(), "0");
        SDViewBinder.setTextView(mTv_comments, actModel.getHas_dp_count(), "0");
        SDViewBinder.setTextView(mTv_collect, actModel.getCollect_deal_count(), "0");
        // 待付款订单数量
        String strNotPayOrderCount = null;
        int notPayOrderCount = SDTypeParseUtil.getInt(actModel.getNot_pay_order_count());
        if (notPayOrderCount > 0) {
            strNotPayOrderCount = String.valueOf(notPayOrderCount);
        }

        // 待评价
        String strWaitComment = null;
        int waitComment = SDTypeParseUtil.getInt(actModel.getWait_dp_count());
        if (waitComment > 0) {
            strWaitComment = "待评价 " + waitComment;
        }
        SDViewBinder.setTextViewsVisibility(mTv_order_not_comment, strWaitComment);

        String notPaidCountStr = actModel.getNot_pay_order_count();
        Integer notPaidCount = Integer.parseInt(notPaidCountStr);
//        mRDV_orderNotPay.setRedNum(notPaidCount);
        mRDV_orderNotPay.setRedNum(10);
//        if (notPaidCount != null) {
//            orderNotPaidBadge.setCount(notPaidCount);
//            if (notPaidCount > 0) {
//                if (notPaidCount > 99) {
//                    notPaidCountStr = "99+";
//                }
//                orderNotPaidCountView.setVisibility(View.VISIBLE);
//                orderNotPaidCountView.setText(notPaidCountStr);
//            } else {
//                orderNotPaidCountView.setVisibility(View.INVISIBLE);
//            }
//        }
        // 消费券
        String groupVoucherCountStr = actModel.getCoupon_count();
        Integer groupVoucherCount = Integer.parseInt(groupVoucherCountStr);
        mRDV_Comsume.setRedNum(groupVoucherCount);
        // 我的战队
        String xiaomiCountStr = String.valueOf(actModel.getUser_num());
        Integer xiaomiCount = Integer.parseInt(xiaomiCountStr);
        mRDV_MyFriend.setRedNum(xiaomiCount);

        String readyForUseCountStr = actModel.getWait_use_count();
        Integer readyForUseCount = Integer.parseInt(readyForUseCountStr);
        mRDV_orderNotUse.setRedNum(readyForUseCount);
//        if (readyForUseCount != null) {
//            orderReadyForUseBadge.setCount(readyForUseCount);
//            if (readyForUseCount > 0) {
//                if (readyForUseCount > 99) {
//                    readyForUseCountStr = "99+";
//                }
//                orderReadyForUseCountView.setVisibility(View.VISIBLE);
//                orderReadyForUseCountView.setText(readyForUseCountStr);
//            } else {
//                orderReadyForUseCountView.setVisibility(View.INVISIBLE);
//            }
//        }
        String notCommentedCountStr = actModel.getWait_dp_count();
        Integer notCommentedCount = Integer.parseInt(notCommentedCountStr);
        mRDV_orderNotComment.setRedNum(notCommentedCount);
//        if (notCommentedCount != null) {
//            orderNotCommentedBadge.setCount(notCommentedCount);
//            if (notCommentedCount > 0) {
//                if (notCommentedCount > 99) {
//                    notCommentedCountStr = "99+";
//                }
//                orderNotCommentedCountView.setVisibility(View.VISIBLE);
//                orderNotCommentedCountView.setText(notCommentedCountStr);
//            } else {
//                orderNotCommentedCountView.setVisibility(View.INVISIBLE);
//            }
//        }

        String refundCountStr = actModel.getOrder_refund_count();
        Integer refundCount = Integer.parseInt(refundCountStr);
        mRDV_orderNotRefund.setRedNum(refundCount);
//        if (refundCount != null) {
//            orderRefundBadge.setCount(refundCount);
//            if (refundCount > 0) {
//                if (refundCount > 99) {
//                    refundCountStr = "99+";
//                }
//                orderRefundCountView.setVisibility(View.VISIBLE);
//                orderRefundCountView.setText(refundCountStr);
//            } else {
//                orderRefundCountView.setVisibility(View.INVISIBLE);
//            }
//        }
    }

    private void initUserTopView() {
        ImageView mIvSettings = (ImageView) findViewById(R.id.settings);
        /*粉丝的数量*/
        mTv_FansNum = ((TextView) findViewById(R.id.tv_fans_num));
        /*消息数量*/
        mTv_Msg = ((TextView) findViewById(R.id.tv_msg));

        /*点击了粉丝*/
        findViewById(R.id.ll_fans).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("粉丝");
            }
        });

        findViewById(R.id.ll_msg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("消息");
            }
        });

        mIvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                Bundle userData = new Bundle();
                userData.putString("user_face", mUserFaceString);
                intent.putExtras(userData);
                startActivity(intent);
            }
        });
        // 消息
//        findViewById(R.id.messages).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = (new Intent(getActivity(), MyMessageActivity.class));
//                startActivity(intent);
//            }
//        });
    }

    private void registerClick() {
        mIv_user_avatar.setOnClickListener(this);
        // mLl_user_info.setOnClickListener(this);
        mLlGroupCoupons.setOnClickListener(this);

        // mLl_my_friend_circle.setOnClickListener(this);
        mLl_order_has_pay.setOnClickListener(this);
        mLl_order_takeaway_reservation.setOnClickListener(this);

        mLl_my_red_money.setOnClickListener(this);
        mLl_my_collect.setOnClickListener(this);
        // mLl_my_event.setOnClickListener(this);
        // mLl_my_lottery.setOnClickListener(this);
        mLl_comments.setOnClickListener(this);
        mLl_shopping_cart.setOnClickListener(this);
        mLl_my_asset.setOnClickListener(this);

        viewAllOrdersButton.setOnClickListener(this);
//        orderNotPaidView.setOnClickListener(this);
//        orderNotUsedView.setOnClickListener(this);
//        orderNotCommentedView.setOnClickListener(this);
//        orderToRefundView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_user_avatar) {
            clickUserAvatar();
        } else if (v == mLlGroupCoupons) {
            clickGoupCoupons();
        } else if (v == mLl_order_has_pay) {
            clickOrderHasPay();
        } else if (v == mLl_order_takeaway_reservation) {

        } else if (v == mLl_my_collect) {
            clickCollect();
        } else if (v == mLl_my_red_money) {
            clickMyRedEnvelope();
        } else if (v == mLl_comments) {
            clickMyComment();
        } else if (v == mLl_shopping_cart) {
            clickShopping_cart();
        } else if (v == mLl_my_asset) {
            clickWithdraw();
        } else if (v == ll_lookDianpu) {

        } else if (v == ll_myXiaomi) {

        } else if (v == ll_erWeima) {

        }else if (v == viewAllOrdersButton) {
            clickMyOrderView("all");
        }
    }

    private void clickMyShop() {
        Intent intent = new Intent(getActivity(), DistributionStoreWapActivity.class);
        startActivity(intent);
    }

    private void clickMyFriends() {
        if(mActModel!=null) {
            Intent intent = new Intent(getActivity(), DistributionMyXiaoMiActivity.class);

            intent.putExtra("up_name", mActModel.getUp_name());
            //
            intent.putExtra("up_id", mActModel.getUp_id());
            startActivity(intent);
        }else{
            return;
        }
    }

    private void clickErWeiMa() {
        Intent intent = new Intent(getActivity(), DistributionMyQRCodeActivity.class);
        Bundle bundle = new Bundle();
        if (mActModel == null) {
            return;
        }
        MyDistributionUser_dataModel userData = mActModel.getUser_data();
        if (userData == null) {
            return;
        }
        String share_card = userData.getShare_mall_card();
        String user_avatar = userData.getUser_avatar();
        if ("".equals(share_card) || "".equals(user_avatar)) {
            return;
        }
        bundle.putString("card", share_card);
        bundle.putString("photo", user_avatar);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void clickWithdraw() {
        Intent intent = new Intent(getActivity(), AccountMoneyActivity.class);
        startActivity(intent);
    }

    private void clickMyOrderView(String key) {
//		if ("use_wait".equalsIgnoreCase(key)) {
//			startActivity(new Intent(getActivity(), OrderWait2UseActivity.class));
//		}else {
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, key);
        startActivity(intent);
//		}
    }

    /**
     * 我的红包
     */
    private void clickMyRedEnvelope() {
        Intent intent = new Intent(getActivity(), MyRedEnvelopeActivity.class);
        startActivity(intent);
    }


    private void clickUserInfo() {
        Intent intent = new Intent(getActivity(), MyAccountActivity.class);
        startActivity(intent);
    }

    /**
     * 头像被点击
     */
    private void clickUserAvatar() {
        SDDialogMenu dialog = new SDDialogMenu(getActivity());

        String[] arrItem = new String[]{"拍照", "从手机相册选择"};
        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(Arrays.asList(arrItem), getActivity());
        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenuListener() {

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0:
                        mPhotoHandler.getPhotoFromCamera();
                        break;
                    case 1:
                        mPhotoHandler.getPhotoFromAlbum();
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onDismiss(SDDialogMenu dialog) {
            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog) {
            }
        });
        dialog.showBottom();
    }

    /**
     * 我的点评
     */
    private void clickMyComment() {
        startActivity(new Intent(getActivity(), MyCommentActivity.class));
    }

    /**
     * 我的抽奖
     */
    private void clickMyLottery() {
        startActivity(new Intent(getActivity(), MyLotteryActivity.class));
    }

    /**
     * 我的团购券
     */
    private void clickGroupVoucher() {
        startActivity(new Intent(getActivity(), MyCouponListActivity.class));
    }

    /**
     * 我的优惠券
     */
    private void clickGoupCoupons() {
        Intent intent = new Intent(getActivity(), WithdrawLogActivity.class);
        startActivity(intent);
    }

    /**
     * 未付款订单
     */
    private void clickOrderNotPay() {
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, 0);
        startActivity(intent);
    }

    /**
     * 已付款订单
     */
    private void clickOrderHasPay() {
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, 1);
        startActivity(intent);
    }

    /**
     * 我的活动
     */
    private void clickMyEvent() {
        Intent intent = new Intent(getActivity(), MyEventListActivity.class);
        startActivity(intent);
    }

    /**
     * 购物车
     */
    private void clickShopping_cart() {
        startActivity(new Intent(getActivity(), ShopCartActivity.class));
    }

    /**
     * 收藏
     */
    private void clickCollect() {
        startActivity(new Intent(getActivity(), MyCollectionActivity.class));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        refreshMyAccountFragment();
    }

    @Override
    public void onResume() {
        refreshMyAccountFragment();
        showRedDot();
        super.onResume();
    }

    //	private ImageView redBig;
//	private ImageView redMore;
//	private TextView redCount;
//	private BadgeView redDot;
    private void showRedDot() {
//		MessageHelper.msg_Activity=99;
//		MessageHelper.msg_All=100;
        //三种状态
        /*if (MessageHelper.msg_All>0) {

			if (MessageHelper.msg_Activity>99) {
				redDot.setCount(0);
				redBig.setVisibility(View.GONE);
				redMore.setVisibility(View.VISIBLE);
				return;
			}
			
			if (MessageHelper.msg_Activity>0) {
				redCount.setVisibility(View.VISIBLE);
				redCount.setText(""+MessageHelper.msg_Activity);
				redDot.setCount(MessageHelper.msg_Activity);
			}else {
				redDot.setCount(0);
				redCount.setText("");
				redBig.setVisibility(View.VISIBLE);
				redMore.setVisibility(View.GONE);
			}
			
		}else {
			redCount.setVisibility(View.INVISIBLE);
			redDot.setCount(0);
			redBig.setVisibility(View.GONE);
			redMore.setVisibility(View.GONE);
			redCount.setText("");
		}*/
        //目前的消息是显示总数量
//        int tempCount = 0;
//        if (MessageHelper.msg_All >= 99) {
//            tempCount = 99;
//        } else {
//            tempCount = MessageHelper.msg_All;
//        }
//        if (tempCount == 0) {
//            redCount.setVisibility(View.INVISIBLE);
//            redDot.setCount(0);
//            redBig.setVisibility(View.GONE);
//            redMore.setVisibility(View.GONE);
//            redCount.setText("");
//        } else {
//            redCount.setVisibility(View.VISIBLE);
//            redCount.setText("" + tempCount);
//            redDot.setCount(tempCount);
//            redMore.setVisibility(View.GONE);
//            redBig.setVisibility(View.GONE);
//        }
    }

    private void updateMessageCount() {
        RequestModel model = new RequestModel();
        model.putCtl("uc_message");
        model.putAct("msg_num");
        SDRequestCallBack<MessageCount> handler = new SDRequestCallBack<MessageCount>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel != null) {
                    MessageHelper.msg_Activity = actModel.getMsg_activity();
                    int msg_normal = actModel.getMsg_normal();
                    MessageHelper.msg_All = MessageHelper.msg_Activity + msg_normal;
                } else {
                    MessageHelper.msg_Activity = 0;
                    MessageHelper.msg_All = 0;
                }
                showRedDot();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                SDToast.showToast("获取消息数量失败!");
            }

            @Override
            public void onFinish() {
            }
        };

        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    private void refreshMyAccountFragment() {
        updateMessageCount();
        if (!this.isHidden()) {
            initViewState();
            requestMyAccount();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPhotoHandler.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        mPhotoHandler.deleteTakePhotoFiles();
        super.onDestroy();
    }

    public void refreshFragment() {
        if (mPtrsvAll != null) {
            mPtrsvAll.setRefreshing();
        }
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onRedDotViewClick(View v) {
        mRDV_Comsume.setRedNum(0);
        mRDV_MyShop.setRedNum(0);
        mRDV_MyFriend.setRedNum(0);
        mRDV_MyNameCard.setRedNum(0);
        if (v==mRDV_Comsume){
            //团购消费券
            clickGroupVoucher();
        }else if (v==mRDV_MyShop){
            //我的小店
            clickMyShop();
        }else if (v==mRDV_MyFriend){
            //我的战队
            clickMyFriends();
        }else if (v==mRDV_MyNameCard){
            //我的名片
            clickErWeiMa();
//            UserRobRedPacketEndDialogHelper helper=new UserRobRedPacketEndDialogHelper(getActivity(),false);
//            helper.show();
        }else if (v==mRDV_orderNotPay){
            clickMyOrderView("pay_wait");
        }else if (v==mRDV_orderNotUse){
            clickMyOrderView("use_wait");
        }else if (v==mRDV_orderNotComment){
            clickMyOrderView("comment_wait");
        }else if (v==mRDV_orderNotRefund){
            clickMyOrderView("refund");
        }
    }
}