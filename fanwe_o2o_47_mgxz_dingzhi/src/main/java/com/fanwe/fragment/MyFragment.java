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
import com.fanwe.MyEventListActivity;
import com.fanwe.MyLotteryActivity;
import com.fanwe.MyMessageActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.UploadUserHeadActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
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
import com.fanwe.model.MessageCount;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.user.view.RedPacketListActivity;
import com.fanwe.user.view.customviews.RedDotView;
import com.fanwe.utils.MGStringFormatter;
import com.fanwe.utils.MoneyFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
import com.sunday.eventbus.SDBaseEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 我的fragment
 *
 * @author js02
 */
public class MyFragment extends BaseFragment implements RedDotView.OnRedDotViewClickListener, CallbackView2 {
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
    private TextView mTvCoupons = null; // 总佣金

    @ViewInject(R.id.hongbao_count)
    private TextView mHongbaoCount;//红包

    @ViewInject(R.id.iv_vip)
    private ImageView mIv_vip;//vip等级的图标

    @ViewInject(R.id.btn_view_all_orders)
    private View viewAllOrdersButton;


    @ViewInject(R.id.ll_order_has_pay)
    private LinearLayout mLl_order_has_pay; // 已付款订单

    @ViewInject(R.id.tv_order_not_comment)
    private TextView mTv_order_not_comment;


    // 我的红包
    @ViewInject(R.id.ll_my_red_money)
    private LinearLayout mLl_my_red_money;


    @ViewInject(R.id.ll_my_collect)
    private LinearLayout mLl_my_collect; // 我的收藏

    @ViewInject(R.id.ll_comments)
    private LinearLayout mLl_comments;

    @ViewInject(R.id.tv_comments_number)
    private TextView mTv_comments;//评论

    @ViewInject(R.id.tv_collect_number)
    private TextView mTv_collect;

    @ViewInject(R.id.ll_my_asset)
    private LinearLayout mLl_my_asset;

    @ViewInject(R.id.ll_shopping_cart)
    private LinearLayout mLl_shopping_cart;// 购物车

    //    private TextView mTv_totalMomey;// 总佣金
    private TextView mTv_tixian;// 提现现金
    private TextView mTv_used;// 已使用现金
    private TextView mTv_Predict;


    private PhotoHandler mPhotoHandler;
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
    private UserHttpHelper httpHelper;
    private ModelPersonalHome modelPersonalHome;


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
        initPullToRefreshScrollView();
        initMyOrders();
        addTopView();
        setView();

        httpHelper = new UserHttpHelper(getContext(), this);
    }

    private void setView() {
        mUserFaceString = App.getInstance().getUserIcon();
        SDViewBinder.setTextView(mTvUsername, App.getInstance().getUserNickName(), "");
        SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIv_user_avatar,
                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case UPLOAD_USER_HEAD_SUCCESS:
                SDViewBinder.setImageView(App.getInstance().getUserIcon(), mIv_user_avatar,
                        ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
                break;
            case UPLOAD_USER_INFO_SUCCESS:
                SDViewBinder.setTextView(mTvUsername, App.getInstance().getUserNickName(), "");
                break;
            default:
                break;
        }
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
        //TODO 实际提现
        /**
         * 实际提现
         */
        mll_tixian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
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
            }
        });
        //TODO 我的战队
        /**
         * 我的战队
         */
        mLl_Predict.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("去我的战队");
//                Intent intent = new Intent(getActivity(), DistributionMyXiaoMiActivity.class);
//                intent.putExtra("yes", true);
//                intent.putExtra("money", mActModel.getYuji());
//                intent.putExtra("up_name", mActModel.getUp_name());
//                intent.putExtra("up_id", mActModel.getUp_id());
//                startActivity(intent);
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
        mRDV_Comsume = ((RedDotView) findViewById(R.id.rdv_consume));
        mRDV_MyShop = ((RedDotView) findViewById(R.id.rdv_my_shop));
        mRDV_MyFriend = ((RedDotView) findViewById(R.id.rdv_my_friend));
        mRDV_MyNameCard = ((RedDotView) findViewById(R.id.rdv_my_name_card));
        mRDV_orderNotPay = ((RedDotView) findViewById(R.id.rdv_order_not_paid));
        mRDV_orderNotUse = ((RedDotView) findViewById(R.id.rdv_order_not_used));
        mRDV_orderNotComment = ((RedDotView) findViewById(R.id.rdv_order_not_commented));
        mRDV_orderNotRefund = ((RedDotView) findViewById(R.id.rdv_order_to_refund));

        int color = Color.parseColor("#999999");
        mRDV_Comsume.setAllParams("消费券", R.drawable.bg_groupvacher, 0, Color.WHITE);
        mRDV_MyShop.setAllParams("我的小店", R.drawable.bg_xiaodian, 0, Color.WHITE);
        mRDV_MyFriend.setAllParams("我的战队", R.drawable.bg_xiaomi, 0, Color.WHITE);
        mRDV_MyNameCard.setAllParams("我的名片", R.drawable.bg_erweima, 0, Color.WHITE);

        mRDV_orderNotPay.setAllParams("待付款", R.drawable.ic_obligation, 0, color);
        mRDV_orderNotUse.setAllParams("待使用", R.drawable.ic_ready_for_use, 0, color);
        mRDV_orderNotComment.setAllParams("待评价", R.drawable.ic_to_rank, 0, color);
        mRDV_orderNotRefund.setAllParams("退款", R.drawable.ic_refund, 0, color);

        mRDV_Comsume.setOnRedDotViewClickListener(this);
        mRDV_MyShop.setOnRedDotViewClickListener(this);
        mRDV_MyFriend.setOnRedDotViewClickListener(this);
        mRDV_MyNameCard.setOnRedDotViewClickListener(this);
        mRDV_orderNotPay.setOnRedDotViewClickListener(this);
        mRDV_orderNotUse.setOnRedDotViewClickListener(this);
        mRDV_orderNotComment.setOnRedDotViewClickListener(this);
        mRDV_orderNotRefund.setOnRedDotViewClickListener(this);
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


    private void initPullToRefreshScrollView() {
        mPtrsvAll.setMode(Mode.PULL_FROM_START);
        mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

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

    protected void bindData() {
        if (modelPersonalHome == null) {
            MGLog.e("personalHome 为null");
            return;
        }
        String fx_level = modelPersonalHome.getFx_level();
        if ("1".equals(fx_level)) {
            mIv_vip.setImageResource(R.drawable.ic_rank_3);
        } else if ("2".equals(fx_level)) {
            mIv_vip.setImageResource(R.drawable.ic_rank_2);
        } else if ("3".equals(fx_level)) {
            mIv_vip.setImageResource(R.drawable.ic_rank_1);
        }

//        SDViewBinder.setTextView(mTv_tixian, MoneyFormat.format(userData.getFx_money()));
        //可提现
        SDViewBinder.setTextView(mTv_tixian, "￥ " + modelPersonalHome.getWithdrawals());
//        SDViewBinder.setTextView(mTv_totalMomey, MoneyFormat.format(userData.getFx_total_balance()));
        //已使用
        SDViewBinder.setTextView(mTv_used, MoneyFormat.format(modelPersonalHome.getUse_money() * 1.0f), "￥ 0.00");

        //预计可提现佣金
        SDViewBinder.setTextView(mTv_Predict, "￥ " + modelPersonalHome.getForecast_estimated_money(), "￥ 0.00");
//        SDViewBinder.setTextView(mTv_Predict, MoneyFormat.format(personalHome.getForecast_estimated_money()), "￥ 0.00");
//        SDViewBinder.setImageView(actModel.getUser_avatar(), mIv_user_avatar,
//                ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());

//		SDViewBinder.setTextView(mTvUsername, actModel.getUser_name(), "未找到");

        //余额
        SDViewBinder.setTextView(mTvBalance, modelPersonalHome.getNow_user_account_money(), "￥ 0.00");

        SDViewBinder.setTextView(mTv_user_score, modelPersonalHome.getNow_user_points(), "0");
        SDViewBinder.setTextView(mTvCoupons, modelPersonalHome.getTotal_user_commission(), "0");
        //红包数
        SDViewBinder.setTextView(mHongbaoCount, modelPersonalHome.getRed_packet_count(), "0");
        //评价
        SDViewBinder.setTextView(mTv_comments, "0", "0");
        SDViewBinder.setTextView(mTv_collect, modelPersonalHome.getCollect(), "0");

        //粉丝
        mTv_FansNum.setText(modelPersonalHome.getFans_count());
        //消息
        mTv_Msg.setText(modelPersonalHome.getUser_message());
        // 待评价
        String strWaitComment = null;
        int waitComment = SDTypeParseUtil.getInt(modelPersonalHome.getPending_evaluation());
        if (waitComment > 0) {
            strWaitComment = "待评价 " + waitComment;
        }
        SDViewBinder.setTextViewsVisibility(mTv_order_not_comment, strWaitComment);

        // 待付款订单数量
        String notPaidCountStr = modelPersonalHome.getPending_pay();
        Integer notPaidCount = Integer.parseInt(notPaidCountStr);
        mRDV_orderNotPay.setRedNum(notPaidCount);
        // 消费券
        String groupVoucherCountStr = modelPersonalHome.getCoupons_count();
        Integer groupVoucherCount = Integer.parseInt(groupVoucherCountStr);
        mRDV_Comsume.setRedNum(groupVoucherCount);
        // 我的战队
        //TODO 战队没看见数据
        mRDV_MyFriend.setRedNum(0);

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
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
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
    }

    private void registerClick() {
        mIv_user_avatar.setOnClickListener(this);
        mLlGroupCoupons.setOnClickListener(this);
        mLl_order_has_pay.setOnClickListener(this);
        mLl_my_red_money.setOnClickListener(this);
        mLl_my_collect.setOnClickListener(this);
        mLl_comments.setOnClickListener(this);
        mLl_shopping_cart.setOnClickListener(this);
        mLl_my_asset.setOnClickListener(this);
        viewAllOrdersButton.setOnClickListener(this);
        mTvUsername.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_user_avatar) {
            clickUserAvatar();
        } else if (v == mLlGroupCoupons) {
            //资金日志
            startActivity(WithdrawLogActivity.class);
        } else if (v == mLl_order_has_pay) {
            clickOrderHasPay();
        } else if (v == mLl_my_collect) {
            //收藏
            startActivity(MyCollectionActivity.class);
        } else if (v == mLl_my_red_money) {
            //我的红包
//            startActivity(MyRedEnvelopeActivity.class);
            startActivity(RedPacketListActivity.class);
        } else if (v == mLl_comments) {
            //我的点评
            startActivity(MyCommentActivity.class);
        } else if (v == mLl_shopping_cart) {
            //购物车
            startActivity(ShopCartActivity.class);
        } else if (v == mLl_my_asset) {
            //账户余额
            startActivity(AccountMoneyActivity.class);
        } else if (v == viewAllOrdersButton) {
            clickMyOrderView("all");
        } else if (v == mTvUsername) {
            clickUserInfo();
        }
    }

    private void clickMyShop() {
        Intent intent = new Intent(getActivity(), DistributionStoreWapActivity.class);
        startActivity(intent);
    }

    //TODO 朋友
    private void clickMyFriends() {
        Intent intent = new Intent(getActivity(), DistributionMyXiaoMiActivity.class);
        startActivity(intent);
    }

    private void clickMyOrderView(String key) {
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, key);
        startActivity(intent);
    }


    private void clickUserInfo() {
        Intent intent = new Intent(getActivity(), MemberRankActivity.class);
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
     * 我的抽奖
     */
    private void clickMyLottery() {
        startActivity(new Intent(getActivity(), MyLotteryActivity.class));
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
     * goto 新Activity
     *
     * @param clazz 类
     */
    public void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        refreshMyAccountFragment();
    }

    @Override
    public void onResume() {
        refreshMyAccountFragment();
        super.onResume();
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
//                showRedDot();
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
        if (v == mRDV_Comsume) {
            //团购消费券
            startActivity(MyCouponListActivity.class);
        } else if (v == mRDV_MyShop) {
            //我的小店
            clickMyShop();
        } else if (v == mRDV_MyFriend) {
            //我的战队
            clickMyFriends();
        } else if (v == mRDV_MyNameCard) {
            //我的名片
            startActivity(DistributionMyQRCodeActivity.class);
        } else if (v == mRDV_orderNotPay) {
            clickMyOrderView("pay_wait");
        } else if (v == mRDV_orderNotUse) {
            clickMyOrderView("use_wait");
        } else if (v == mRDV_orderNotComment) {
            clickMyOrderView("comment_wait");
        } else if (v == mRDV_orderNotRefund) {
            clickMyOrderView("refund");
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
}