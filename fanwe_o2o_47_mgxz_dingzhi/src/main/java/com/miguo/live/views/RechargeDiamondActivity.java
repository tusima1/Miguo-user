package com.miguo.live.views;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.AppWebViewActivity;
import com.fanwe.BaseActivity;
import com.fanwe.adapter.PaymentAdapter;
import com.fanwe.app.App;
import com.fanwe.constant.Constant;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.model.MalipayModel;
import com.fanwe.model.Payment_codeModel;
import com.fanwe.model.WxappModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.reward.RewardConstants;
import com.fanwe.reward.adapters.DiamondGridAdapter;
import com.fanwe.reward.model.DiamondTypeEntity;
import com.fanwe.reward.model.DiamondUserOwnEntity;
import com.fanwe.reward.presenters.DiamondHelper;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.OrderDetailInfo;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.shoppingcart.presents.CommonShoppingHelper;
import com.fanwe.utils.SDFormatUtil;
import com.fanwe.wxapp.SDWxappPay;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.List;

/**
 * 充值买钻页。
 * Created by Administrator on 2016/9/11.
 */
public class RechargeDiamondActivity extends BaseActivity implements RefreshCalbackView {

    /**
     * 00:正式，01:测试
     */
    private static final String UPACPAPP_MODE = "00";
    /**
     * 支付完成。
     */
    private final String PAY_SUCCESS = "3";
    /**
     * 未支付。
     */
    private final String PAY_WAIT = "0";
    /**
     * ALIpay.
     */
    private final String ALIPAY = "Aliapp";


    @ViewInject(R.id.gridview)
    private GridView gridView;

    @ViewInject(R.id.diamond_line)
    private LinearLayout diamond_line;
    /**
     * 大笔对应的钻
     */
    @ViewInject(R.id.diamond_value)
    private TextView diamond_value;


    /**
     * 购买钻对应的金额。
     */
    @ViewInject(R.id.money_value)
    private TextView money_value;

    @ViewInject(R.id.self_diamond)
    private TextView self_diamond;
    /**
     * 支付按钮。
     */
    @ViewInject(R.id.pay_btn)
    private Button pay_btn;
    //支付方式详情
    protected OrderDetailPaymentsFragment mFragPayments;

    private CommonShoppingHelper commonShoppingHelper;
    private DiamondHelper diamondHelper;

    private DiamondGridAdapter diamondGridAdapter;


    private DiamondTypeEntity currentDiamondType;
    private DiamondTypeEntity bigDiamondType;
    private PaymentTypeInfo currentPayType;
    private List<DiamondTypeEntity> diamondTypeEntityList = null;

    private OrderDetailInfo orderDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.recharge_diamond_activity);
        init();
    }

    private void init() {
        initTitle();

        diamondGridAdapter = new DiamondGridAdapter(this);

        gridView.setAdapter(diamondGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (diamondTypeEntityList == null || diamondTypeEntityList.size() < 1) {
                    return;
                }
                for (int i = 0; i < diamondTypeEntityList.size(); i++) {
                    DiamondTypeEntity entity0 = diamondTypeEntityList.get(i);
                    if (i == position) {
                        currentDiamondType = entity0;
                        entity0.setChecked(true);
                    } else {
                        entity0.setChecked(false);
                    }
                }
                diamondGridAdapter.setDatas(diamondTypeEntityList);
                diamondGridAdapter.notifyDataSetChanged();
                setColor(false);
            }
        });

        diamond_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDiamondType = bigDiamondType;
                for (int i = 0; i < diamondTypeEntityList.size(); i++) {
                    DiamondTypeEntity entity0 = diamondTypeEntityList.get(i);
                        entity0.setChecked(false);
                }
                diamondGridAdapter.setDatas(diamondTypeEntityList);
                diamondGridAdapter.notifyDataSetChanged();
                setColor(true);
            }
        });
        initFragment();
        getBaseData();
    }


    public void setColor(boolean checked) {
        if (checked) {
            diamond_value.setTextColor(getResources().getColor(R.color.white));
            money_value.setTextColor(getResources().getColor(R.color.white));
            diamond_line.setBackground(getResources().getDrawable(R.drawable.bg_orange_small));
        } else {
            diamond_value.setTextColor(getResources().getColor(R.color.main_color));
            money_value.setTextColor(getResources().getColor(R.color.main_color_press));
            diamond_line.setBackground(getResources().getDrawable(R.drawable.bg_orange_smallline));
        }
    }

    private void initFragment() {

        // 支付方式列表
        mFragPayments = new OrderDetailPaymentsFragment();

        mFragPayments.setmListener(new PaymentAdapter.PaymentTypeChangeListener() {

            @Override
            public void onPaymentChange(PaymentTypeInfo model) {
                currentPayType = model;
            }
        });


        getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments, mFragPayments);
        commonShoppingHelper = new CommonShoppingHelper(this);
        diamondHelper = new DiamondHelper(this);
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidate()) {
                    String content = "您确认以 ￥" + currentDiamondType.getPrice() + " 的价格购买 " + currentDiamondType.getDiamond() + " 米果钻吗?";
                    showTitleDialog(content);
                }
            }
        });
    }

    private void showTitleDialog(final String context) {
        new SDDialogConfirm()
                .setTextContent(
                        context)
                .setmListener(new SDDialogCustom.SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        // 支付操作。
                        String payment_id = currentPayType.getId();
                        String diamond_id = currentDiamondType.getId();
                        diamondHelper.createDiamondOrder(payment_id, diamond_id);
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }


    /**
     * 确认是否允许进入下一步。
     *
     * @return boolean
     */
    public boolean checkValidate() {
        if (currentDiamondType == null) {
            MGToast.showToast("请选择您想购买的米钻。");
            return false;
        }
        if (currentPayType == null) {
            MGToast.showToast("请选择一种支付方式。");
            return false;
        }
        return true;
    }

    /*
   取支付方式 。
    */
    public void getBaseData() {
        diamondHelper.GetDiamondList();
        diamondHelper.getUserDiamond();
        commonShoppingHelper.getPayment();
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
     * 绑定 当前用户已经有的果钻数。
     *
     * @param diamondUserOwnEntityList
     */
    private void bindUserDiamond(List<DiamondUserOwnEntity> diamondUserOwnEntityList) {
        if (diamondUserOwnEntityList != null) {
            DiamondUserOwnEntity entityOwn = diamondUserOwnEntityList.get(0);
            float value = SDFormatUtil.stringToFloat(entityOwn.getDiamond_android()) + SDFormatUtil.stringToFloat(entityOwn.getCommon_diamond());

            self_diamond.setText(value + "");

        }
    }

    String payStatus;
    String mOrderId;

    /**
     * 设置支付结果。
     */
    private void bindPayDiamondResult(List<OrderDetailInfo> datas) {

        if (datas != null && datas.size() > 0) {
            orderDetailInfo = datas.get(0);


            payStatus = orderDetailInfo.getOrder_info().getOrder_status();
            mOrderId = orderDetailInfo.getOrder_info().getOrder_id();
            if (!PAY_SUCCESS.equals(payStatus)) {
                if (TextUtils.isEmpty(mOrderId)) {
                    MGToast.showToast("id为空");
                    finish();
                    return;
                }
            }
            bindData();
        } else {
            payFailue();
        }


    }

    private void gotoPayHistoryActivity() {
        Intent intent = new Intent(RechargeDiamondActivity.this, PayHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    private Payment_codeModel mPaymentCodeModel;

    private void bindData() {
        if (TextUtils.isEmpty(payStatus)) {
            return;
        }
        if (PAY_SUCCESS.equals(payStatus)) {
            //支付成功 处理。
            gotoPayHistoryActivity();
        } else if (PAY_WAIT.equals(payStatus)) {
            //支付未完成。
            mPaymentCodeModel = new Payment_codeModel();
            String class_name = orderDetailInfo.getClass_name();

            HashMap<String, String> config = orderDetailInfo.getConfig();
            String payMoney = config.get("total_fee_format");
            mPaymentCodeModel.setPay_money(payMoney);
            mPaymentCodeModel.setConfig(config);
            mPaymentCodeModel.setClass_name(class_name);
            if (!TextUtils.isEmpty(class_name) && ALIPAY.equals(class_name)) {
                mPaymentCodeModel.setPayment_name("支付宝支付");
            } else {
                mPaymentCodeModel.setPayment_name("微信支付");
            }

            if (mPaymentCodeModel == null) {
                MGToast.showToast("支付信息为空。");
                return;
            }
            clickPay();
        }
    }

    private void payFailue(){
        MGToast.showToast("支付失败。");
    }
    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case PAY_SUCCESS_WEIXIN:
               gotoPayHistoryActivity();
                break;
            case PAY_FAILUE_WEIXIN:
                payFailue();
                break;
            default:
                break;
        }
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
            }
        }

    }

    /**
     * 支付宝sdk支付(新)
     */
    private void payMalipay() {
        if (mPaymentCodeModel == null) {
            return;
        }
        MalipayModel model = mPaymentCodeModel.getMalipay();
        if (model == null) {
            MGToast.showToast("获取支付宝支付参数失败");
            return;
        }

        String orderSpec = model.getTextHtml();

        String sign = model.getSign();

        String signType = model.getSign_type();

        if (TextUtils.isEmpty(orderSpec)) {
            MGToast.showToast("order_spec为空");
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            MGToast.showToast("sign为空");
            return;
        }

        if (TextUtils.isEmpty(signType)) {
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
                    gotoPayHistoryActivity();
                } else if ("8000".equals(status)) // 支付结果确认中
                {
                    MGToast.showToast("支付结果确认中");
                    gotoPayHistoryActivity();
                } else {
                    MGToast.showToast(info);
                }
                //requestPayOrder();
            }

            @Override
            public void onFailure(Exception e, String msg) {

                if (e != null) {
                    MGToast.showToast("错误:" + e.toString());
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        MGToast.showToast(msg);
                    }
                }
            }
        });
        payer.pay(orderSpec, sign, signType);
    }

    /**
     * 微信支付
     */
    private void payWxapp() {
        if (mPaymentCodeModel == null) {
            return;
        }

        WxappModel model = mPaymentCodeModel.getWxapp();
        if (model == null) {
            MGToast.showToast("获取微信支付参数失败");
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
            return;
        }

        String prepayId = model.getPrepay_id();
        if (TextUtils.isEmpty(prepayId)) {
            MGToast.showToast("prepayId为空");
            return;
        }

        String nonceStr = model.getNonce_str();
        if (TextUtils.isEmpty(nonceStr)) {
            MGToast.showToast("nonceStr为空");
            return;
        }

        String timeStamp = model.getTime_stamp();
        if (TextUtils.isEmpty(timeStamp)) {
            MGToast.showToast("timeStamp为空");
            return;
        }

        String packageValue = model.getPackage_value();
        if (TextUtils.isEmpty(packageValue)) {
            MGToast.showToast("packageValue为空");
            return;
        }

        String sign = model.getSign();
        if (TextUtils.isEmpty(sign)) {
            MGToast.showToast("sign为空");
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



    private void bindDiamondType(List<DiamondTypeEntity> diamondTypeEntityList) {

        if (diamondTypeEntityList != null) {
            int size = diamondTypeEntityList.size();
            if (size < 7) {
                diamondGridAdapter.setDatas(diamondTypeEntityList);
            } else {
                bigDiamondType = diamondTypeEntityList.get(size - 1);
                money_value.setText("￥ " + bigDiamondType.getPrice() + "元");
                diamond_value.setText(bigDiamondType.getDiamond() + "钻石");
                diamondTypeEntityList.remove(size - 1);
                diamondGridAdapter.setDatas(diamondTypeEntityList);
            }
            this.diamondTypeEntityList = diamondTypeEntityList;
            diamondGridAdapter.notifyDataSetChanged();
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("充值页面");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextTop("充值记录");

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        //进入充值记录页。
        gotoPayHistoryActivity();
    }

    @Override
    public void onFailue(String method, String responseBody) {
        MGToast.showToast(responseBody);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case ShoppingCartconstants.GET_PAYMENT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayment(datas);
                    }
                });

                break;
            case RewardConstants.BUY_DIAMOND:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindDiamondType(datas);
                    }
                });
                break;

            case RewardConstants.USER_DIAMOND:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindUserDiamond(datas);
                    }
                });
                break;
            case RewardConstants.DIAMOND_ORDER:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindPayDiamondResult(datas);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentDiamondType = null;
        currentPayType = null;
    }
}
