package com.fanwe;

import java.math.BigDecimal;

import com.fanwe.app.App;
import com.fanwe.constant.Constant.PaymentType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.OrderDetailAccountPaymentFragment;
import com.fanwe.fragment.OrderDetailFeeFragment;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.fragment.OrderDetailAccountPaymentFragment.OrderDetailAccountPaymentFragmentListener;
import com.fanwe.fragment.OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Cart_checkActModel;
import com.fanwe.model.MalipayModel;
import com.fanwe.model.PayActModel;
import com.fanwe.model.PayResultModel;
import com.fanwe.model.Payment_codeModel;
import com.fanwe.model.Payment_doneActModel;
import com.fanwe.model.Payment_listModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_HomeModel;
import com.fanwe.model.UpacpappModel;
import com.fanwe.model.WxappModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.wxapp.SDWxappPay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDEventManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.utils.Log;
import com.unionpay.UPPayAssistEx;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ConfirmTopUpActivity extends BaseActivity implements IWXAPIEventHandler{
	@ViewInject(R.id.tv_money)
	private TextView mTv_money;

	@ViewInject(R.id.act_confirm_order_ptrsv_all)
	private PullToRefreshScrollView mPtrsvAll;

	@ViewInject(R.id.act_confirm_order_btn_confirm_order)
	private Button mBtnConfirmOrder;

	/** 00:正式，01:测试 */
	private static final String UPACPAPP_MODE = "00";

	protected OrderDetailPaymentsFragment mFragPayments;
	protected OrderDetailAccountPaymentFragment mFragAccountPayment;
	protected OrderDetailFeeFragment mFragFees;

	protected PayResultModel mActModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_top_up);
		init();
	}

	private void init()
	{
		initTitle();
		initClick();
		addFragment();
		initPullToRefreshScrollView();
	}

	private void initClick()
	{
		mBtnConfirmOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_confirm_order_btn_confirm_order:
			if (v.isClickable()) {
				v.setBackgroundResource(R.drawable.layer_main_color_corner_press);
				v.setClickable(false);
				//60s可点
				
			}else{
				v.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
				v.setClickable(true);
			}
			clickBt();
			break;

		default:
			break;
		}
	}

	private void clickBt() 
	{
		if(mFragPayments.getPaymentId() == 0 &&  mFragAccountPayment.getUseAccountMoney() == 0)
		{
			SDToast.showToast("请选择支付方式");
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("uc_fx");
		model.putAct("upgrade_commit");
		if (mFragPayments != null)
		{
			model.put("id",mFragPayments.getPaymentId());
		}
		if (mFragAccountPayment != null)
		{
			model.put("all_account_money", mFragAccountPayment.getUseAccountMoney());
		}
		
		SDRequestCallBack<PayActModel> handler = new SDRequestCallBack<PayActModel>() {

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1)
				{
					mActModel = actModel.getResult();
					if(mActModel != null)
					{
						payMent();
					}else
					{
						Intent intent = new Intent(ConfirmTopUpActivity.this,MemberRankActivity.class);
						startActivity(intent);
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

	protected void payMent() {
		if (mActModel == null) 
		{
			return;
		}
		String payAction = mActModel.getPayment_name();
		String className = mActModel.getClass_name();
		if (TextUtils.isEmpty(payAction)) // wap
		{
			Intent intent = new Intent(App.getApplication(),
					AppWebViewActivity.class);
			intent.putExtra(AppWebViewActivity.EXTRA_URL, payAction);
			startActivity(intent);
			return;
		} else
		{
			if (PaymentType.MALIPAY.equals(className)
					|| PaymentType.ALIAPP.equals(className)) // 支付宝sdk新
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

	private void initTitle() {
		mTitle.setMiddleTextTop("确认支付");
	}

	private void addFragment() {
		// 支付方式列表
		mFragPayments = new OrderDetailPaymentsFragment();
		mFragPayments.setmListener(new OrderDetailPaymentsFragmentListener() {
			@Override
			public void onPaymentChange(Payment_listModel model) {
				// requestCalculate();
			}
		});
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments,
				mFragPayments);

		// 余额支付
		mFragAccountPayment = new OrderDetailAccountPaymentFragment();
		mFragAccountPayment
				.setmListener(new OrderDetailAccountPaymentFragmentListener() {
					@Override
					public void onPaymentChange(boolean isSelected) {
						// requestCalculate();
					}
				});
		getSDFragmentManager()
				.replace(R.id.act_confirm_order_fl_account_payments,
						mFragAccountPayment);

		// 费用信息
		mFragFees = new OrderDetailFeeFragment();
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_fees,
				mFragFees);
	}

	private void initPullToRefreshScrollView() {
		mPtrsvAll.setMode(Mode.PULL_FROM_START);
		mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				requestData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {

			}

		});
		mPtrsvAll.setRefreshing();
	}

	protected void requestData() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_fx");
		model.putAct("upgrade");
		SDRequestCallBack<Cart_checkActModel> handler = new SDRequestCallBack<Cart_checkActModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("正在加载");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				dealRequestDataSuccess(actModel);

			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mPtrsvAll.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	protected void dealRequestDataSuccess(Cart_checkActModel actModel) {
		if (actModel == null) {
			return;
		}
		BigDecimal bd = new BigDecimal(actModel.getFee());
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(mTv_money, "￥"+String.valueOf(bd));
		// 支付方式列表
		mFragPayments.setmCheckActModel(actModel);
		// 余额支付
		mFragAccountPayment.setmCheckActModel(actModel);
	}

	/**
	 * 支付宝sdk支付(新)
	 */
	private void payMalipay() {
		if (mActModel == null) {
			return;
		}
		MalipayModel model = mActModel.getMalipay();
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

		com.fanwe.library.alipay.easy.SDAlipayer payer = new com.fanwe.library.alipay.easy.SDAlipayer(
				mActivity);
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
					Intent intent = new Intent(ConfirmTopUpActivity.this,MemberRankActivity.class);
					startActivity(intent);
					finish();
				} else if ("8000".equals(status)) // 支付结果确认中
				{
					SDToast.showToast("支付结果确认中,请等待5分钟");
					
				} else {
					SDToast.showToast(info);
				}
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

	/**
	 * 银联支付
	 */
	private void payUpacpapp() {
		if (mActModel == null) {
			return;
		}

		UpacpappModel model = mActModel.getUpacpapp();
		if (model == null) {
			SDToast.showToast("获取银联支付参数失败");
			return;
		}

		String tn = model.getTn();
		if (TextUtils.isEmpty(tn)) {
			SDToast.showToast("tn 为空");
			return;
		}
		UPPayAssistEx.startPayByJAR(mActivity,
				com.unionpay.uppay.PayActivity.class, null, null, tn,
				UPACPAPP_MODE);
	}

	/**
	 * 微信支付
	 */
	private void payWxapp() {
		if (mActModel == null) {
			return;
		}

		WxappModel model = mActModel.getWxapp();
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

	@Override
	public void onReq(BaseReq arg0) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		int respType = resp.getType();
		switch (respType)
		{
		case ConstantsAPI.COMMAND_PAY_BY_WX:
			String content = null;
			switch (resp.errCode)
			{
			case 0: // 成功
				content = "支付成功";
				Intent intent = new Intent(ConfirmTopUpActivity.this,MemberRankActivity.class);
				startActivity(intent);
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
			if (content != null)
			{
				SDToast.showToast(content);
			}
			break;

		default:
			break;
		}
		finish();
		
	}
	public void sonFragemtMethod() {
		mFragAccountPayment.performClick();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		confirmRankState();
	}
	
	private void confirmRankState() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_home");
		model.putAct("homepage");
		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_HomeModel>()
				{

					@Override
					public void onStart() {
						super.onStart();
						SDDialogManager.showProgressDialog("");
					}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						if (actModel.getStatus() == 1){
							int rank = actModel.getDist().getRank();
							if (rank >=2) {
//								Intent intent = new Intent(ConfirmTopUpActivity.this,MemberRankActivity.class);
//								startActivity(intent);
								SDToast.showToast("升级成功!");
								finish();
							}else {
								//DoNothing
							}
						}
					}

					@Override
					public void onFinish(){
						SDDialogManager.dismissProgressDialog();
					}
				});
	}

}
