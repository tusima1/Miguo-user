package com.fanwe;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.adapter.PayorderCodesAdapter;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.constant.Constant.PaymentType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dialog.ShareAfterPaytDialog;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.MalipayModel;
import com.fanwe.model.Payment_ShareModel;
import com.fanwe.model.Payment_codeModel;
import com.fanwe.model.Payment_doneActCouponlistModel;
import com.fanwe.model.UpacpappModel;
import com.fanwe.model.WxappModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.OrderDetailInfo;
import com.fanwe.shoppingcart.model.Order_info;
import com.fanwe.shoppingcart.model.Share_info;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.umeng.UmengShareManager.onSharedListener;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.DisPlayUtil;
import com.fanwe.wxapp.SDWxappPay;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiHomeActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.unionpay.UPPayAssistEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PayActivity extends BaseActivity implements RefreshCalbackView, CallbackView, ShareAfterPaytDialog.GetSalary {
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

    /**
     * 订单id (int)
     */
    public static final String EXTRA_ORDER_ID = "extra_order_id";
    public static final String ORDER_ENTITY = "order_detail";

    @ViewInject(R.id.act_pay_tv_order_sn)
    private TextView mTvOrderSn;

    @ViewInject(R.id.act_pay_tv_pay_info)
    private TextView mTvPayInfo;

    @ViewInject(R.id.act_pay_layout_pay_info)
    private LinearLayout mLayoutPayInfo;

    @ViewInject(R.id.act_pay_btn_pay)
    private Button mBtnPay;
    /**
     * 查看消费券。
     */
    @ViewInject(R.id.act_quanlist_btn)
    private Button mBtnQuan;

    @ViewInject(R.id.act_pay_ll_scan_code)
    private LinearLayout mLlScanCodes;

    @ViewInject(R.id.rootView)
    private LinearLayout rootView;

    @ViewInject(R.id.top_line)
    private View top_line;

    protected ImageView mIv_share;
    /**
     * 订单编号。
     */
    private String mOrderId;

    private OrderDetailInfo orderDetailInfo;

    private Payment_codeModel mPaymentCodeModel;

    private String mHasPay;
    private List<Drawable> listTotal = new ArrayList<Drawable>();

    protected String title;
    protected String clickUrl;
    protected String imageUrl;
    protected String content;
    private PopupWindow pop;
    private OutSideShoppingCartHelper outSideShoppingCartHelper;
    private Share_info share_info;

    private CommonHttpHelper commonHttpHelper;
    private String shareRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_pay);
        init();
        getRecordId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
    }

    private void init() {
        getIntentData();
        initfews();
        initTitle();
        //	requestPayOrder();
        registeClick();
    }


    private UMShareListener shareResultCallback = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            MGToast.showToast("分享成功！现金红包将在验券后到账");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            MGToast.showToast(share_media + "分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            MGToast.showToast(share_media + "分享取消");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void initfews() {

        mIv_share = (ImageView) findViewById(R.id.iv_share_red);
        /*
         * popupView = getLayoutInflater() .inflate(R.layout.pop_share_red,
		 * null);
		 *
		 * popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
		 * LayoutParams.WRAP_CONTENT,true);
		 */
    }


    /**
     * 绑定二维码
     *
     * @param couponlist
     */
    protected void bindCouponlistData(List<Payment_doneActCouponlistModel> couponlist) {
        if (couponlist != null && couponlist.size() > 0) {
            mLlScanCodes.setVisibility(View.VISIBLE);
            mLlScanCodes.removeAllViews();
            PayorderCodesAdapter adapter = new PayorderCodesAdapter(couponlist, PayActivity.this);
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                mLlScanCodes.addView(view);
            }
        } else {
            mLlScanCodes.setVisibility(View.GONE);
        }
    }

    String payStatus;

    private void bindData() {
        SDViewBinder.setTextView(mTvOrderSn, orderDetailInfo.getOrder_info().getOrder_sn());
        if (TextUtils.isEmpty(payStatus)) {
            return;
        }
        if (PAY_SUCCESS.equals(payStatus)) {
            //支付成功 处理。
            showShareDialog();
            SDViewUtil.show(mBtnQuan);
            SDEventManager.post(EnumEventTag.PAY_ORDER_SUCCESS.ordinal());
            mHasPay = "all";
            mBtnPay.setVisibility(View.GONE);
            SDViewBinder.setTextView(mTvPayInfo, orderDetailInfo.getOrder_info().getName());
            if (!isEmpty(orderDetailInfo.getShare_url())) {
                SDViewUtil.show(mIv_share);
            } else {
                SDViewUtil.hide(mIv_share);
            }
            mLayoutPayInfo.setVisibility(View.GONE);
        } else if (PAY_WAIT.equals(payStatus)) {
            //支付未完成。
            mPaymentCodeModel = new Payment_codeModel();
            String class_name = orderDetailInfo.getClass_name();

            mHasPay = "pay_wait";
            mBtnPay.setVisibility(View.VISIBLE);
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
                MGToast.showToast("payment_code is null");
                return;
            }
            SDViewBinder.setTextView(mTvPayInfo, orderDetailInfo.getOrder_info().getName());
            mBtnPay.setText(mPaymentCodeModel.getPay_moneyFormat());
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mOrderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        if (intent.getSerializableExtra(ORDER_ENTITY) != null && intent.getSerializableExtra(ORDER_ENTITY) instanceof OrderDetailInfo) {
            orderDetailInfo = (OrderDetailInfo) intent.getSerializableExtra(ORDER_ENTITY);
        }
        if (orderDetailInfo == null) {
            MGToast.showToast("订单信息为空");
            finish();
            return;
        }
        payStatus = orderDetailInfo.getOrder_info().getOrder_status();
        share_info = orderDetailInfo.getShare_info();
        if (!PAY_SUCCESS.equals(payStatus)) {
            if (TextUtils.isEmpty(mOrderId)) {
                MGToast.showToast("id为空");
                finish();
                return;
            }
        }
        bindData();
    }

    ShareAfterPaytDialog dialog;

    /**
     * 分享领红包
     */
    private void showShareDialog() {
        if (share_info != null) {
            if (TextUtils.isEmpty(share_info.getSalarySum()) || DataFormat.toDouble(share_info.getSalarySum()) == 0) {
                return;
            }
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog = new ShareAfterPaytDialog(PayActivity.this, share_info, shareResultCallback);
            dialog.setGetSalary(this);
            dialog.setCancelable(false);
            dialog.setCloseListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //不要佣金
                    if (outSideShoppingCartHelper == null) {
                        outSideShoppingCartHelper = new OutSideShoppingCartHelper(PayActivity.this);
                    }
                    outSideShoppingCartHelper.getOrderOperator(mOrderId, "0");
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void getSalary() {
        //要佣金
        if (outSideShoppingCartHelper == null) {
            outSideShoppingCartHelper = new OutSideShoppingCartHelper(PayActivity.this);
        }
        outSideShoppingCartHelper.getOrderOperator(mOrderId, "1");
    }

    private void initTitle() {
        if (!PAY_SUCCESS.equals(payStatus)) {
            mTitle.setMiddleTextTop("订单支付");
        } else {
            mTitle.setMiddleTextTop("支付成功");
        }

        mTitle.setLeftImageLeft(0);
        mTitle.setLeftTextBot("订单列表");

        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("刷新");
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        // TODO 跳到订单列表
        Intent intent = new Intent(getApplicationContext(), MyOrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, mHasPay);
        startActivity(intent);
        // finish();
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        if (outSideShoppingCartHelper == null) {
            outSideShoppingCartHelper = new OutSideShoppingCartHelper(PayActivity.this);
        }
        outSideShoppingCartHelper.getOrderInfo(mOrderId);
    }

    private void registeClick() {
        mBtnPay.setOnClickListener(this);
        mIv_share.setOnClickListener(this);
        mBtnQuan.setOnClickListener(this);
    }


    public void showPayment(boolean ifShow) {
        View v = findViewById(R.id.act_pay_btn_pay);
        if (!ifShow) {
            v.setBackgroundResource(R.drawable.layer_main_color_corner_press);
            v.setClickable(false);
        } else {
            v.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
            v.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_pay_btn_pay:
                showPayment(true);
                clickPay();
                break;
            case R.id.iv_share_red:
                clickShare();
                break;
            case R.id.act_quanlist_btn:
                startActivity(new Intent(this, MyCouponListActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private void clickShare() {
        SDDialogMenu dialog = new SDDialogMenu(this);

        String[] arrItem = new String[]{"给好友发红包"};
        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(Arrays.asList(arrItem), this);
        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenuListener() {
            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0:
                        clickShareUmeng();
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

    protected void clickShareUmeng() {
        UmengShareManager.share(this, title, content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
        UmengShareManager.setonSharedListener(new onSharedListener() {

            @Override
            public void doSuccess() {
                showTitleDialog("是否查看佣金", 1);
            }

            @Override
            public void doFail() {
                showTitleDialog("佣金分享失败，请重试", 2);
            }
        });
        UmengShareManager.setShareTag(1, PayActivity.this, mOrderId);
    }

    private void showTitleDialog(final String location, final int type) {
        new SDDialogConfirm()
                .setTextContent(
                        location)
                .setmListener(new SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        if (type == 1) {
                            // 去首页的"我的"页面
                            Intent intent = new Intent(PayActivity.this, HiHomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("index", 3);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            clickShareUmeng();
                        }
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }


    public List<Drawable> loadData() {
        TypedArray arrItem = getResources().obtainTypedArray(R.array.share_image);
        List<Drawable> list = new ArrayList<Drawable>();
        for (int i = 0; i < arrItem.length(); i++) {
            list.add(arrItem.getDrawable(i));
        }
        arrItem.recycle();
        return list;
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
            if (PaymentType.MALIPAY.equals(className) || PaymentType.ALIAPP.equals(className)) {
                // 支付宝sdk新
                payMalipay();
            } else if (PaymentType.WXAPP.equals(className)) {
                // 微信
                if (!BaseUtils.isWeixinAvilible(this)) {
                    MGToast.showToast("未安装微信");
                    return;
                }
                payWxapp();
            } else if (PaymentType.UPACPAPP.equals(className)) {
                // 银联支付
                payUpacpapp();
            }
        }

    }

    /**
     * 支付结束页。
     */
    public void payFinish() {
        SDViewUtil.show(mBtnQuan);
        SDViewUtil.hide(mBtnPay);
        showShareDialog();
    }

    /**
     * 支付失败。
     */
    public void payFailue() {
        SDViewUtil.hide(mBtnQuan);
        showPayment(true);
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
                    showPayment(false);
                    payFinish();
                } else if ("8000".equals(status)) // 支付结果确认中
                {
                    MGToast.showToast("支付结果确认中");
                    showPayment(false);
                    payFinish();
                } else {
                    MGToast.showToast(info);
                    payFailue();
                }
                //requestPayOrder();
            }

            @Override
            public void onFailure(Exception e, String msg) {
                payFailue();
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

    @Override
    protected void onResume() {
        // requestPayOrder();

        if (pop != null) {
            pop.dismiss();
            pop = null;
        }
        super.onResume();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case PAY_SUCCESS_WEIXIN:
                showPayment(false);
                payFinish();
                break;
            case PAY_FAILUE_WEIXIN:
                payFailue();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(PayActivity.this, HiHomeActivity.class));
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSharePop(Payment_ShareModel infos, float money) {
        if (pop != null) {
            return;
        }
        View inflate = View.inflate(PayActivity.this, R.layout.pop_act_pay, null);
        ImageButton ib_close = (ImageButton) inflate.findViewById(R.id.ib_x);
        TextView tv_money = (TextView) inflate.findViewById(R.id.tv_money);
        ImageView iv_share = (ImageView) inflate.findViewById(R.id.iv_share);
        title = infos.getTitle();

        content = infos.getSummary();
        if (content.equals("")) {
            content = title;
        }
        clickUrl = infos.getUrl();

        imageUrl = infos.getLogo_icon();

        pop = new PopupWindow(inflate, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                false);
        ib_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                pop = null;
            }
        });
        tv_money.setText(TextMoney.textFarmat(money));
        iv_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO share
                clickShareUmeng();
            }
        });

        // pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(false);
        // pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        pop.showAsDropDown(top_line, 0, 0);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        pop.getContentView().measure(0, 0);
        int measuredWidth = pop.getContentView().getMeasuredWidth();
        int xPos = (width - measuredWidth) / 2;
        pop.update(top_line, xPos, DisPlayUtil.dp2px(PayActivity.this, 110), LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onFailue(String method, String responseBody) {

    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<OrderDetailInfo> items;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (ShoppingCartconstants.GET_ORDER_INFO.equals(method)) {
            items = datas;
            msg.what = 0;
        } else if (CommonConstants.CREATE_SHARE_RECORD.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelCreateShareRecord bean = (ModelCreateShareRecord) datas.get(0);
                shareRecordId = bean.getId();
                if (dialog != null) {
                    dialog.setShareRecordId(shareRecordId);
                }
            }
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(items)) {
                        OrderDetailInfo orderDetailInfo = items.get(0);
                        Order_info order_info = orderDetailInfo.getOrder_info();
                        if ("3".equals(order_info.getOrder_status()) || "5".equals(order_info.getOrder_status())) {
                            //支付成功 处理。
                            SDViewUtil.show(mBtnQuan);
                            SDEventManager.post(EnumEventTag.PAY_ORDER_SUCCESS.ordinal());
                            mHasPay = "all";
                            mBtnPay.setVisibility(View.GONE);
                            SDViewBinder.setTextView(mTvPayInfo, orderDetailInfo.getOrder_info().getName());
                            if (!isEmpty(orderDetailInfo.getShare_url())) {
                                SDViewUtil.show(mIv_share);
                            } else {
                                SDViewUtil.hide(mIv_share);
                            }
                            mLayoutPayInfo.setVisibility(View.GONE);
                        } else {
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
                                MGToast.showToast("payment_code is null");
                                return;
                            }
                            SDViewBinder.setTextView(mTvOrderSn, orderDetailInfo.getOrder_info().getOrder_sn());
                            SDViewBinder.setTextView(mTvPayInfo, orderDetailInfo.getOrder_info().getName());
                            mBtnPay.setText(mPaymentCodeModel.getPay_moneyFormat());
                        }
                    }
                    break;
            }
        }
    };

    private void getRecordId() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(PayActivity.this, this);
        }
        commonHttpHelper.createShareRecord(Constant.ShareType.WEB_HOME, "");
    }

}
