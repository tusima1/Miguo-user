package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;

import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.MyRedPayMentsFragment;
import com.fanwe.fragment.MyRedPayMentsFragment.MyredPaymentsFragmentListener;
import com.fanwe.fragment.OrderDetailAccountPaymentFragment;
import com.fanwe.fragment.OrderDetailAccountPaymentFragment.OrderDetailAccountPaymentFragmentListener;
import com.fanwe.fragment.OrderDetailFeeFragment;
import com.fanwe.fragment.OrderDetailGoodsFragment;
import com.fanwe.fragment.OrderDetailParamsFragment;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.OrderDetailInfo;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.shoppingcart.model.ShoppingBody;
import com.fanwe.shoppingcart.presents.CommonShoppingHelper;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.utils.SDFormatUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 确认订单
 *
 * @author js02
 */
public class ConfirmOrderActivity extends BaseActivity implements RefreshCalbackView {
    /**
     * 下拉刷 新。
     */
    protected PullToRefreshScrollView mPtrsvAll;
    /**
     * 确认订单。
     */
    protected Button mBtnConfirmOrder;

    protected ShoppingBody mCheckActModel;
    /**
     * 商品IDS.
     */
    protected ArrayList<String> mList = new ArrayList<String>();
    // -------------------------fragments
    /**
     * 商品详情
     */
    protected OrderDetailGoodsFragment mFragGoods;
    //订单参数
    protected OrderDetailParamsFragment mFragParams;
    //支付方式详情
    protected OrderDetailPaymentsFragment mFragPayments;
    //余额支付 当前余额为零 时，不显示。
    protected OrderDetailAccountPaymentFragment mFragAccountPayment;
    //费用项目
    protected OrderDetailFeeFragment mFragFees;
    //红包抵扣
    protected MyRedPayMentsFragment mFragMyRed;
    /**
     * 商品ID 集合。
     */
    private String mListDeal_id;

    private OutSideShoppingCartHelper outSideShoppingCartHelper;
    private CommonShoppingHelper commonShoppingHelper;

    //总金额。
    float totalFloat = 0.00f;

    //用户余额。
    float yueFloat = 0.00f;

    private PaymentTypeInfo currentPayType;
    /**
     * 订单编号。
     */
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_confirm_order);
        init();
    }

    protected void init() {
        outSideShoppingCartHelper = new OutSideShoppingCartHelper(this);
        commonShoppingHelper = new CommonShoppingHelper(this);
        initIntentData();
        findViews();
        initTitle();
        registeClick();
        addFragments();
        initPullToRefreshScrollView();
        getPayType();
    }

    private void initIntentData()

    {
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getStringExtra("orderId");
        } else {
            mListDeal_id = getIntent().getExtras().getString("list_id");
        }
    }

    private void findViews() {
        mPtrsvAll = (PullToRefreshScrollView) findViewById(R.id.act_confirm_order_ptrsv_all);
        mBtnConfirmOrder = (Button) findViewById(R.id.act_confirm_order_btn_confirm_order);
    }

    private void addFragments() {
        // 绑定商品数据
        mFragGoods = new OrderDetailGoodsFragment();
        getSDFragmentManager().replace(R.id.act_confirm_order_fl_goods, mFragGoods);

        // 留言信息
        mFragParams = new OrderDetailParamsFragment();
        getSDFragmentManager().replace(R.id.act_confirm_order_fl_params, mFragParams);

        // 支付方式列表
        mFragPayments = new OrderDetailPaymentsFragment();


        mFragPayments.setmListener(new OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener() {

            @Override
            public void onPaymentChange(PaymentTypeInfo model) {
                currentPayType = model;
                changePayType(1, false);
            }
        });


        getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments, mFragPayments);
        //红包
        mFragMyRed = new MyRedPayMentsFragment();
        mFragMyRed.setmListener(new MyredPaymentsFragmentListener() {

            @SuppressWarnings("rawtypes")
            @Override
            public void onRedPaymentChange(String redIds) {

                if (!TextUtils.isEmpty(redIds)) {
                    requestBenefit(redIds);
                } else {
                    mCheckActModel.setYouhuiPrice(0 + "");
                    mFragFees.refreshData();
                }
            }
        });
        getSDFragmentManager().replace(R.id.act_my_red_pay, mFragMyRed);

        // 余额支付
        mFragAccountPayment = new OrderDetailAccountPaymentFragment();
        mFragAccountPayment.setmListener(new OrderDetailAccountPaymentFragmentListener() {
            @Override
            public void onPaymentChange(boolean isSelected) {

                changePayType(0, isSelected);
            }
        });

        getSDFragmentManager().replace(R.id.act_confirm_order_fl_account_payments, mFragAccountPayment);

        // 费用信息
        mFragFees = new OrderDetailFeeFragment();
        getSDFragmentManager().replace(R.id.act_confirm_order_fl_fees, mFragFees);
    }

    private void initPullToRefreshScrollView() {
        mPtrsvAll.setMode(Mode.PULL_FROM_START);
        mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

        });
        mPtrsvAll.setRefreshing();
    }

    /**
     * 当前钱是否够支付当前订单。
     *
     * @return
     */
    public boolean ifMoneyEnough() {
        //第三方支付是否被 选择。
        String thirdPaymentId = mFragPayments.getPaymentId();
        //余额支付是否被 选择
        int yuePay = mFragAccountPayment.getUseAccountMoney();

        totalFloat = SDFormatUtil.stringToFloat(mCheckActModel.getTotal());
        //用户余额。
        yueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney());

        if (TextUtils.isEmpty(thirdPaymentId) || "0".equals(thirdPaymentId)) {
            //未选择第三方支付
            if (yuePay == 0) {
                //未选择余额支付
                return false;
            } else {
                if (totalFloat > yueFloat) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            //选择了第三方支付
            return true;
        }
    }

    /**
     * 改变支付方式 0 余额支付 。1 第三方支付
     *
     * @param type
     * @param isChecked 是否选择余额支付。
     */
    public void changePayType(int type, boolean isChecked) {
        //总金额。
        totalFloat = SDFormatUtil.stringToFloat(mCheckActModel.getTotal());
        //用户余额。
        yueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney());
        //当前 选择余额支付
        if (type == 0) {
            if (isChecked) {
                if (totalFloat <= yueFloat) {
                    mFragPayments.clearSelectedPayment(false);
                    mFragFees.setCurrentFeeInfoModel(null);
                }
                mFragFees.setIfYueChecked(true);
            } else {
                mFragFees.setIfYueChecked(false);
            }
        } else {
            if (totalFloat <= yueFloat) {
                mFragAccountPayment.clearSelectedPayment(false);
            }
            mFragFees.setCurrentFeeInfoModel(currentPayType);
            mFragFees.setIfYueChecked(false);
        }
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragFees.refreshData();
            }
        });

    }

    /*
    取支付方式 。
     */
    public void getPayType() {
        commonShoppingHelper.getPayment();
    }

    /**
     * 获取数据
     */
    protected void requestData() {
        //来自订单 页
        if (!TextUtils.isEmpty(orderId)) {
            outSideShoppingCartHelper.getDindanDetailById(orderId);
        } else {
            //来自购物车页。
            outSideShoppingCartHelper.getDingdanDetail(mListDeal_id);
        }
    }

    protected void dealRequestDataSuccess(ShoppingBody actModel) {

        mCheckActModel = actModel;
        bindData();

    }

    protected void bindData() {
        if (mCheckActModel == null) {
            return;
        }

        // 绑定商品数据
        mFragGoods.setmCheckActModel(mCheckActModel);

        // 留言参数
        mFragParams.setmCheckActModel(mCheckActModel);

        // 支付方式列表
        mFragPayments.setmCheckActModel(mCheckActModel);

        // 余额支付
        mFragAccountPayment.setmCheckActModel(mCheckActModel);

        //红包
        mFragMyRed.setmCheckActModel(mCheckActModel);

        mFragFees.setmCheckActModel(mCheckActModel);
        if (!TextUtils.isEmpty(orderId)) {
            findViewById(R.id.act_my_red_pay).setVisibility(View.GONE);
        }
    }

    /**
     * 绑定 支付方式
     *
     * @param datas
     */
    private void bindPayment(List<PaymentTypeInfo> datas) {
        if (datas != null && datas.size() > 0) {
            mFragPayments.setListPayment(datas);
        }
    }

    /**
     * 显示优惠后金额。
     *
     * @param datas
     */
    private void dealBenifit(List<HashMap<String, String>> datas) {
        if (datas != null) {
            HashMap<String, String> maps = datas.get(0);
            String youhuiPrice = maps.get(ShoppingCartconstants.YOUHUI_PRICE);
            mCheckActModel.setYouhuiPrice(youhuiPrice);
            mFragFees.refreshData();
        }
    }


    /**
     * 选择红包后取优惠金额。
     *
     * @param redIDs
     */
    private void requestBenefit(String redIDs) {
        outSideShoppingCartHelper.getBenefitCount(mCheckActModel.getId(), redIDs);
    }


    /**
     * 确认并 生成订单。
     */
    protected void requestDoneOrder() {
        //支付方式
        String payDisp = mFragPayments.getPaymentId();
        // 留言
        String content = mFragParams.getContent();
        //红包列表。
        String red_id = mFragMyRed.getmRedIds();
        //是否使用余额支付。
        String balances_flg = mFragAccountPayment.getUseAccountMoney() + "";
        //提交并生成订单。
        if (!TextUtils.isEmpty(orderId)) {
            //String order_id,String payment,String is_use_account_money
            outSideShoppingCartHelper.payByOrderId(orderId, payDisp, balances_flg);
        } else {
            outSideShoppingCartHelper.createOrder(mCheckActModel.getId(), balances_flg, red_id, payDisp, content);
        }
    }

    public void onFinish() {
        mBtnConfirmOrder.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
        mBtnConfirmOrder.setClickable(true);
    }

    /**
     * 生成订单并 进入支付页。
     *
     * @param datas
     */

    protected void dealRequestDoneOrderSuccess(List datas) {
        if (datas != null && datas.size() > 0) {
            OrderDetailInfo orderDetailInfo = (OrderDetailInfo) datas.get(0);
            CommonInterface.updateCartNumber();
            SDEventManager.post(EnumEventTag.DONE_CART_SUCCESS.ordinal());
            Intent intent = new Intent(mActivity, PayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(PayActivity.ORDER_ENTITY, orderDetailInfo);
            bundle.putString(PayActivity.EXTRA_ORDER_ID, orderDetailInfo.getOrder_info().getOrder_id());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            MGToast.showToast("订单提交失败。");
        }

    }

    private void registeClick() {

        mBtnConfirmOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFragPayments == null || mFragParams == null || mFragMyRed == null || mFragAccountPayment == null || mCheckActModel == null) {
                    return;
                }
                if (!ifMoneyEnough()) {
                    MGToast.showToast("请选择一种支付方式");
                    return;
                }
                if (v.isClickable()) {
                    v.setBackgroundResource(R.drawable.layer_main_color_corner_press);
                    v.setClickable(false);
                    //60s可点

                } else {
                    v.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
                    v.setClickable(true);
                }
                requestDoneOrder();
            }
        });
    }

    protected void initTitle() {
        mTitle.setMiddleTextTop("确认订单");
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {

            default:
                break;
        }
    }

    @Override
    protected void onNeedRefreshOnResume() {
        requestData();
        super.onNeedRefreshOnResume();
    }


    private void onError(String message) {
        MGToast.showToast(message);
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPtrsvAll.onRefreshComplete();
                onFinish();
            }
        });
    }

    @Override
    public void onFailue(String method, String message) {
        switch (method) {
            case ShoppingCartconstants.SP_CART_TOORDER_GET:
                onError(message);
                break;
            case ShoppingCartconstants.ORDER_INFO_CREATE:
                onError(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case ShoppingCartconstants.SP_CART_TOORDER_GET:
                //onFinish();
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrsvAll.onRefreshComplete();

                        if (datas != null && datas.size() > 0) {
                            dealRequestDataSuccess((ShoppingBody) datas.get(0));
                        }
                    }
                });


                break;
            case ShoppingCartconstants.ORDER_INFO_CREATE:

                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dealRequestDoneOrderSuccess(datas);
                    }
                });
                break;
            case ShoppingCartconstants.GET_PAYMENT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayment(datas);
                    }
                });

                break;
            case ShoppingCartconstants.SP_CART_TOORDER_POST:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dealBenifit(datas);
                    }
                });
            default:
                break;
        }

    }

    /**
     * list 转 string.
     *
     * @param list
     * @return
     */
    private String listToString(ArrayList<String> list) {
        int size = list.size();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < size; i++) {
            str.append(list.get(i) + ",");
        }
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    @Override
    public void onFailue(String responseBody) {

    }
}