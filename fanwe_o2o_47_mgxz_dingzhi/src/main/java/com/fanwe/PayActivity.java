package com.fanwe;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.fanwe.constant.Constant.PaymentType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.MalipayModel;
import com.fanwe.model.Payment_ShareModel;
import com.fanwe.model.Payment_codeModel;
import com.fanwe.model.Payment_doneActCouponlistModel;
import com.fanwe.model.Payment_doneActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.UpacpappModel;
import com.fanwe.model.WxappModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.umeng.UmengShareManager.onSharedListener;
import com.fanwe.utils.DisPlayUtil;
import com.fanwe.wxapp.SDWxappPay;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDEventManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.unionpay.UPPayAssistEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PayActivity extends BaseActivity implements IWXAPIEventHandler {
	/** 00:正式，01:测试 */
	private static final String UPACPAPP_MODE = "00";

	/** 订单id (int) */
	public static final String EXTRA_ORDER_ID = "extra_order_id";

	@ViewInject(R.id.act_pay_tv_order_sn)
	private TextView mTvOrderSn;

	@ViewInject(R.id.act_pay_tv_pay_info)
	private TextView mTvPayInfo;

	@ViewInject(R.id.act_pay_btn_pay)
	private Button mBtnPay;

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

	private Payment_codeModel mPaymentCodeModel;

	private String mHasPay;
	private List<Drawable> listTotal = new ArrayList<Drawable>();

	protected String title;
	protected String clickUrl;
	protected String imageUrl;
	protected String content;
	private PopupWindow pop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_pay);
		init();
	}

	private void init() {
		getIntentData();
		initfews();
		initTitle();
		requestPayOrder();
		registeClick();
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

	private void requestPayOrder() {
		if (!TextUtils.isEmpty(App.getInstance().getToken())) {
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("payment");
		model.putAct("done");
		model.putUser();
		model.put("id", mOrderId);
		SDRequestCallBack<Payment_doneActModel> handler = new SDRequestCallBack<Payment_doneActModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					SDViewBinder.setTextView(mTvOrderSn, actModel.getOrder_sn());
					int payStatus = actModel.getPay_status();
					if (payStatus == 1)// 订单已经付款
					{
						SDEventManager.post(EnumEventTag.PAY_ORDER_SUCCESS.ordinal());
						mHasPay = "all";
						mBtnPay.setVisibility(View.GONE);
						SDViewBinder.setTextView(mTvPayInfo, actModel.getPay_info());
						bindCouponlistData(actModel.getCouponlist());
						if (!isEmpty(actModel.getShare_url())) {
							SDViewUtil.show(mIv_share);
							title = actModel.getShare_title();
							content = actModel.getShare_info();
							clickUrl = actModel.getShare_url();
							imageUrl = actModel.getShare_ico();
						} else {
							SDViewUtil.hide(mIv_share);
						}
						if (actModel.getShare() != null && actModel.getSalary() > 0) {
							showSharePop(actModel.getShare(), actModel.getSalary());
						}
					} else {
						mHasPay = "pay_wait";
						mBtnPay.setVisibility(View.VISIBLE);
						mPaymentCodeModel = actModel.getPayment_code();
						if (mPaymentCodeModel == null) {
							SDToast.showToast("payment_code is null");
							return;
						}
						SDViewBinder.setTextView(mTvPayInfo, mPaymentCodeModel.getPay_info());
						mBtnPay.setText(mPaymentCodeModel.getPay_moneyFormat());
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
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

	private void getIntentData() {
		mOrderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
		if (TextUtils.isEmpty(mOrderId)) {
			SDToast.showToast("id为空");
			finish();
			return;
		}
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("订单支付");

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
		requestPayOrder();

	}

	private void registeClick() {
		mBtnPay.setOnClickListener(this);
		mIv_share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_pay_btn_pay:
			if (v.isClickable()) {
				v.setBackgroundResource(R.drawable.layer_main_color_corner_press);
				v.setClickable(false);
				//60s可点
				
			}else{
				v.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
				v.setClickable(true);
			}
			clickPay();
			break;
		case R.id.iv_share_red:
			clickShare();
			break;
		default:
			break;
		}
	}

	private void clickShare() {
		SDDialogMenu dialog = new SDDialogMenu(this);

		String[] arrItem = new String[] { "给好友发红包" };
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
				showTitleDialog("是否查看佣金",1);
			}
			
			@Override
			public void doFail() {
				showTitleDialog("佣金分享失败，请重试",2);
			}
		});
		UmengShareManager.setShareTag(1, PayActivity.this, mOrderId);
	}

	private void showTitleDialog(final String location,final int type) {
		new SDDialogConfirm()
				.setTextContent(
						location)
				.setmListener(new SDDialogCustomListener() {
					@Override
					public void onDismiss(SDDialogCustom dialog) {

					}

					@Override
					public void onClickConfirm(View v, SDDialogCustom dialog) {
						if (type==1) {
							// 去首页的"我的"页面
							Intent intent = new Intent(PayActivity.this, MainActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("index", 3);
							intent.putExtras(bundle);
							startActivity(intent);
						}else{
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
			if (PaymentType.MALIPAY.equals(className) || PaymentType.ALIAPP.equals(className)) // 支付宝sdk新
			{
				payMalipay();
			} else if (PaymentType.WXAPP.equals(className)) // 微信
			{
				payWxapp();
			} else if (PaymentType.UPACPAPP.equals(className)) // 银联支付
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
			SDToast.showToast("获取银联支付参数失败");
			return;
		}

		String tn = model.getTn();
		if (TextUtils.isEmpty(tn)) {
			SDToast.showToast("tn 为空");
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
			SDToast.showToast("获取微信支付参数失败");
			return;
		}

		String appId = model.getAppid();
		if (TextUtils.isEmpty(appId)) {
			SDToast.showToast("appId为空");
			return;
		}

		String partnerId = model.getPartnerid();
		if (TextUtils.isEmpty(partnerId)) {
			SDToast.showToast("partnerId为空");
			return;
		}

		String prepayId = model.getPrepayid();
		if (TextUtils.isEmpty(prepayId)) {
			SDToast.showToast("prepayId为空");
			return;
		}

		String nonceStr = model.getNoncestr();
		if (TextUtils.isEmpty(nonceStr)) {
			SDToast.showToast("nonceStr为空");
			return;
		}

		String timeStamp = model.getTimestamp();
		if (TextUtils.isEmpty(timeStamp)) {
			SDToast.showToast("timeStamp为空");
			return;
		}

		String packageValue = model.getPackagevalue();
		if (TextUtils.isEmpty(packageValue)) {
			SDToast.showToast("packageValue为空");
			return;
		}

		String sign = model.getSign();
		if (TextUtils.isEmpty(sign)) {
			SDToast.showToast("sign为空");
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
			SDToast.showToast("获取支付宝支付参数失败");
			return;
		}

		String orderSpec = model.getOrder_spec();

		String sign = model.getSign();

		String signType = model.getSign_type();

		if (TextUtils.isEmpty(orderSpec)) {
			SDToast.showToast("order_spec为空");
			return;
		}

		if (TextUtils.isEmpty(sign)) {
			SDToast.showToast("sign为空");
			return;
		}

		if (TextUtils.isEmpty(signType)) {
			SDToast.showToast("signType为空");
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
					SDToast.showToast("支付成功");
				} else if ("8000".equals(status)) // 支付结果确认中
				{
					SDToast.showToast("支付结果确认中");
				} else {
					SDToast.showToast(info);
				}
				requestPayOrder();
			}

			@Override
			public void onFailure(Exception e, String msg) {
				if (e != null) {
					SDToast.showToast("错误:" + e.toString());
				} else {
					if (!TextUtils.isEmpty(msg)) {
						SDToast.showToast(msg);
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
	public void onReq(BaseReq arg0) {

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
				break;

			default:
				break;
			}
			if (content != null) {
				SDToast.showToast(content);
			}
			break;

		default:
			break;
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(PayActivity.this, MainActivity.class));
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

}
