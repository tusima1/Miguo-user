package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.fanwe.adapter.PaymentAdapter;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.barry.UserUpdateDao;
import com.fanwe.dao.barry.impl.UserUpdateInfoDaoImpl;
import com.fanwe.dao.barry.view.UserUpdateView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.OrderDetailFeeFragment;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.UserUpdateInfoBean;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.MalipayModel;
import com.fanwe.model.PayResultModel;
import com.fanwe.model.Payment_codeModel;
import com.fanwe.model.UpacpappModel;
import com.fanwe.model.WxappModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.shoppingcart.presents.CommonShoppingHelper;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getUserUpgradeOrder.ModelGetUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.ModelPostUserUpgradeOrder;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.wxapp.SDWxappPay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.unionpay.UPPayAssistEx;

import java.util.HashMap;
import java.util.List;

public class ConfirmTopUpActivity extends BaseActivity implements IWXAPIEventHandler, RefreshCalbackView, CallbackView2 {
    @ViewInject(R.id.tv_money)
    private TextView mTv_money;

    @ViewInject(R.id.account)
    private TextView userAccount;

    @ViewInject(R.id.act_confirm_order_ptrsv_all)
    private PullToRefreshScrollView mPtrsvAll;

    @ViewInject(R.id.act_confirm_order_btn_confirm_order)
    private Button mBtnConfirmOrder;

    @ViewInject(R.id.money)
    private CheckBox money;
    public static final String MONEY_PAY_ID = "account";

    /**
     * 00:正式，01:测试
     */
    private static final String UPACPAPP_MODE = "00";

    protected OrderDetailPaymentsFragment mFragPayments;

    protected OrderDetailFeeFragment mFragFees;

    protected PayResultModel mActModel;

    private String mHasPay;
    private CommonShoppingHelper commonShoppingHelper;
    private UserHttpHelper userHttpHelper;
    private PaymentTypeInfo currentPayType;

    UserUpdateDao userUpdateDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_top_up);
        init();
    }

    private void getPayment() {
        if (commonShoppingHelper == null) {
            commonShoppingHelper = new CommonShoppingHelper(this);
        }
        commonShoppingHelper.getPayment();

        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(this, this);
        }
        userHttpHelper.getUserUpgradeOrder();
    }

    private String orderMoney, orderId;
    private int is_use_account_money = 0;

    private void postUserUpgradeOrder() {
        if (userHttpHelper == null) {
            userHttpHelper = new UserHttpHelper(this, this);
        }
        userHttpHelper.postUserUpgradeOrder(money.isChecked() ? MONEY_PAY_ID : mFragPayments.getPaymentId(), orderId, is_use_account_money);
    }

    private void init() {
        initTitle();
        initClick();
        addFragment();
        initMoneyPay();
        initPullToRefreshScrollView();
        initUserUpdateInfo();
    }

    private void initUserUpdateInfo(){
        userUpdateDao = new UserUpdateInfoDaoImpl(new UserUpdateView() {
            @Override
            public void getUserUpdateInfoSuccess(final UserUpdateInfoBean.Result.Body info) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUserUpdateInfo(info);
                    }
                });
            }

            @Override
            public void getUserUpdateInfoError(String msg) {

            }
        });
        userUpdateDao.getUserUpdateInfo(App.getApplication().getToken());
    }

    private void setUserUpdateInfo(UserUpdateInfoBean.Result.Body info){
        mTv_money.setText("￥" + info.getTotalPrice());
        userAccount.setText("账户余额：" + info.getUser_account_money() +"元使用余额支付");
    }

    private void initClick() {
        mBtnConfirmOrder.setOnClickListener(this);
    }
    public void onRefreshOrderBtn() {
        mBtnConfirmOrder.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
        mBtnConfirmOrder.setClickable(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_confirm_order_btn_confirm_order:
                if (TextUtils.isEmpty(mFragPayments.getPaymentId()) && !money.isChecked()) {
                    MGToast.showToast("请选择支付方式");
                    return;
                }
                if (TextUtils.isEmpty(orderMoney)) {
                    MGToast.showToast("支付金额为空");
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
                postUserUpgradeOrder();
                break;

            default:
                break;
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("确认支付");
    }

    private void addFragment() {
        // 支付方式列表
        mFragPayments = new OrderDetailPaymentsFragment();
        getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments,
                mFragPayments);

        /**
         * 点击支付宝或者微信支付
         */
        mFragPayments.setmListener(new PaymentAdapter.PaymentTypeChangeListener() {

            @Override
            public void onPaymentChange(PaymentTypeInfo model) {
                if(model != null){
                    if(model.isChecked()) {
                        currentPayType = model;
                        money.setChecked(false);
                    }else{
                        currentPayType =null;
                    }
                }
            }
        });


        // 费用信息
        mFragFees = new OrderDetailFeeFragment();
        getSDFragmentManager().replace(R.id.act_confirm_order_fl_fees,
                mFragFees);
    }

    /**
     * 余额支付
     */
    private void initMoneyPay(){
        money.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isPressed()){
                    if(isChecked){
                        mFragPayments.clearSelectedPayment(true);
                    }
                }
            }
        });
    }

    private void initPullToRefreshScrollView() {
        mPtrsvAll.setMode(Mode.PULL_FROM_START);
        mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                getPayment();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {

            }

        });
        mPtrsvAll.setRefreshing();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }



    /**
     * 绑定 支付方式
     *
     * @param datas
     */
    private void bindPayment(List<PaymentTypeInfo> datas) {
        if (!SDCollectionUtil.isEmpty(datas)) {
            if (mFragPayments != null)
                mFragPayments.setListPayment(datas);
        }
    }

    @Override
    public void onFailue(String method, String responseBody) {

        switch(method){
            case UserConstants.USER_UPGRADE_ORDER_GET:
                MGToast.showToast("当前用户暂无法升级。");
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onSuccess(String responseBody) {
        /**
         * 如果是余额支付成功！
         */
        if(money.isChecked()){
            BaseUtils.finishActivity(this);
        }
    }

    private Payment_codeModel mPaymentCodeModel;

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case ShoppingCartconstants.GET_PAYMENT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayment(datas);
                        mPtrsvAll.onRefreshComplete();
                    }
                });
                break;
            case UserConstants.USER_UPGRADE_ORDER_GET:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!SDCollectionUtil.isEmpty(datas)) {
                            ModelGetUserUpgradeOrder bean = (ModelGetUserUpgradeOrder) datas.get(0);
                            orderMoney = bean.getTotal_price();
                            orderId = bean.getOrder_id();
                            SDViewBinder.setTextView(mTv_money, "￥" + orderMoney);
                        }
                    }
                });
                break;
            case UserConstants.USER_UPGRADE_ORDER_POST:

                /**
                 * 如果是余额支付成功！
                 */
                if(money.isChecked()){
                    BaseUtils.finishActivity(this);
                    return;
                }

                if (!SDCollectionUtil.isEmpty(datas)) {
                    ModelPostUserUpgradeOrder bean = (ModelPostUserUpgradeOrder) datas.get(0);
                    mPaymentCodeModel = new Payment_codeModel();
                    String class_name = bean.getClass_name();

                    mHasPay = "pay_wait";
                    HashMap<String, String> config = bean.getConfig();
                    String payMondy = config.get("total_fee");
                    mPaymentCodeModel.setPay_money(payMondy);
                    mPaymentCodeModel.setConfig(config);
                    mPaymentCodeModel.setClass_name(class_name);
                    if (!TextUtils.isEmpty(class_name) && "Aliapp".equals(class_name)) {
                        mPaymentCodeModel.setPayment_name("支付宝支付");
                    } else {
                        mPaymentCodeModel.setPayment_name("微信支付");
                    }
                    clickPay();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String method) {
        switch(method){
            case UserConstants.USER_UPGRADE_ORDER_GET:
                MGToast.showToast("当前用户暂无法升级。");
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onFinish(String method) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int respType = resp.getType();
        switch (respType) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                String content = null;
                switch (resp.errCode) {
                    case 0: // 成功
                        content = "支付成功";
                        break;
                    case -1: // 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                        content = "支付失败";
                        break;
                    case -2: // 无需处理。发生场景：用户不支付了，点击取消，返回APP。
                        content = "取消支付";
                        //showPayment(true);
                        break;

                    default:
                        break;
                }
                if (content != null) {
                    MGToast.showToast(content);
                }
                break;

            default:
                break;
        }
        finish();
    }

    private void clickPay() {
        if (mPaymentCodeModel == null) {
            return;
        }
        String payAction = mPaymentCodeModel.getPay_action();
        String className = mPaymentCodeModel.getClass_name();
        if (!TextUtils.isEmpty(payAction)) // wap
        {
            Intent intent = new Intent(App.getApplication(), AppWebViewActivity.class);
            intent.putExtra(AppWebViewActivity.EXTRA_URL, payAction);
            startActivity(intent);
            return;
        } else {
            if (Constant.PaymentType.MALIPAY.equals(className) || Constant.PaymentType.ALIAPP.equals(className)) // 支付宝sdk新
            {
                payMalipay();
            } else if (Constant.PaymentType.WXAPP.equals(className)) // 微信
            {
                payWxapp();
            } else if (Constant.PaymentType.UPACPAPP.equals(className)) // 银联支付
            {
                payUpacpapp();
            }
        }

    }

    /**
     * 银联支付
     */
    private void payUpacpapp() {
        if (mPaymentCodeModel == null) {
            return;
        }
        UpacpappModel model = mPaymentCodeModel.getUpacpapp();
        if (model == null) {
            MGToast.showToast("获取银联支付参数失败");
            return;
        }

        String tn = model.getTn();
        if (TextUtils.isEmpty(tn)) {
            MGToast.showToast("tn 为空");
            return;
        }
        UPPayAssistEx.startPayByJAR(mActivity, com.unionpay.uppay.PayActivity.class, null, null, tn, UPACPAPP_MODE);
    }

    /**
     * 微信支付
     */
    private void payWxapp() {
        if (mPaymentCodeModel == null) {
            onRefreshOrderBtn();
            return;
        }

        WxappModel model = mPaymentCodeModel.getWxapp();
        if (model == null) {
            MGToast.showToast("获取微信支付参数失败");
            onRefreshOrderBtn();
            return;
        }

        String appId = model.getAppid();
        if (TextUtils.isEmpty(appId)) {
            MGToast.showToast("appId为空");
            return;
        }

        String partnerId = model.getMch_id();
        if (TextUtils.isEmpty(partnerId)) {
            MGToast.showToast("partnerId为空");
            onRefreshOrderBtn();
            return;
        }

        String prepayId = model.getPrepay_id();
        if (TextUtils.isEmpty(prepayId)) {
            MGToast.showToast("prepayId为空");
            onRefreshOrderBtn();
            return;
        }

        String nonceStr = model.getNonce_str();
        if (TextUtils.isEmpty(nonceStr)) {
            MGToast.showToast("nonceStr为空");
            onRefreshOrderBtn();
            return;
        }

        String timeStamp = model.getTime_stamp();
        if (TextUtils.isEmpty(timeStamp)) {
            MGToast.showToast("timeStamp为空");
            onRefreshOrderBtn();
            return;
        }

        String packageValue = model.getPackage_value();
        if (TextUtils.isEmpty(packageValue)) {
            MGToast.showToast("packageValue为空");
            onRefreshOrderBtn();
            return;
        }

        String sign = model.getSign();
        if (TextUtils.isEmpty(sign)) {
            MGToast.showToast("sign为空");
            onRefreshOrderBtn();
            return;
        }

        SDWxappPay.getInstance().setAppId(appId);

        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.packageValue = packageValue;
        req.sign = sign;

        SDWxappPay.getInstance().pay(req);
    }

    /**
     * 支付宝sdk支付(新)
     */
    private void payMalipay() {
        if (mPaymentCodeModel == null) {
            onRefreshOrderBtn();
            return;
        }
        MalipayModel model = mPaymentCodeModel.getMalipay();
        if (model == null) {
            MGToast.showToast("获取支付宝支付参数失败");
            onRefreshOrderBtn();
            return;
        }


        String orderSpec = model.getTextHtml();

        String sign = model.getSign();

        String signType = model.getSign_type();

        if (TextUtils.isEmpty(orderSpec)) {
            onRefreshOrderBtn();
            MGToast.showToast("order_spec为空");
            onRefreshOrderBtn();
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            onRefreshOrderBtn();
            MGToast.showToast("sign为空");
            return;
        }

        if (TextUtils.isEmpty(signType)) {
            onRefreshOrderBtn();
            MGToast.showToast("signType为空");
            return;
        }

        com.fanwe.library.alipay.easy.SDAlipayer payer = new com.fanwe.library.alipay.easy.SDAlipayer(mActivity);
        payer.setmListener(new com.fanwe.library.alipay.easy.SDAlipayer.SDAlipayerListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(PayResult result) {
                String info = result.getMemo();
                String status = result.getResultStatus();
                if ("9000".equals(status)) // 支付成功
                {
                    MGToast.showToast("支付成功");
                    finish();
                } else if ("8000".equals(status)) // 支付结果确认中
                {
                    MGToast.showToast("支付结果确认中");
                    finish();
                } else {
                    MGToast.showToast(info);
                    onRefreshOrderBtn();
                }
            }

            @Override
            public void onFailure(Exception e, String msg) {
                if (e != null) {
                    MGToast.showToast("错误:" + e.toString());
                    onRefreshOrderBtn();
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        MGToast.showToast(msg);
                    }
                }
            }
        });
        payer.pay(orderSpec, sign, signType);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case PAY_SUCCESS_WEIXIN:
                MGToast.showToast("支付成功");
                finish();
                break;
            case PAY_FAILUE_WEIXIN:
                onRefreshOrderBtn();
                break;
            default:
                break;
        }
    }
}
