package com.fanwe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.StoreOrderAccountPaymentFragment;
import com.fanwe.fragment.StoreOrderAccountPaymentFragment.StoreOrderAccountPaymentFragmentListener;
import com.fanwe.fragment.StoreOrderFeeFragment;
import com.fanwe.fragment.StoreOrderPaymentsFragment;
import com.fanwe.fragment.StoreOrderPaymentsFragment.StoreOrderPaymentsFragmentListener;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Cart_doneActModel;
import com.fanwe.model.FeeinfoModel;
import com.fanwe.model.Payment_listModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Store_ActModel;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDEventManager;
import com.fanwe.o2o.miguo.R;

import android.app.FragmentManager.BackStackEntry;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreConfirmOrderActivity extends BaseActivity {

	public static final String EXTRA_ID = "mID";
	@ViewInject(R.id.act_store_et_money_text)
	private ClearEditText mEt_money;// 消费总额

	@ViewInject(R.id.tv_store_discount)
	private TextView mTv_discount;

	@ViewInject(R.id.tv_Distrimoney)
	private TextView mTv_DistriMoney;// 预计可返佣金

	@ViewInject(R.id.tv_store_discount_money)
	private TextView mTv_discountMoney;// 展示打折的优惠金额

	@ViewInject(R.id.tv_store_money)
	private TextView mTv_storeMoney;// 实付金额

	@ViewInject(R.id.ll_no_youhui)
	private LinearLayout mLl_no_youhui;
	
	@ViewInject(R.id.v_count_bot)
	private View v_bot;
	@ViewInject(R.id.v_count_top)
	private View v_top;

	@ViewInject(R.id.ll_count)
	private LinearLayout ll_count;
	
	@ViewInject(R.id.act_store_et_money)
	private ClearEditText mEt_NoYouhui;// 不参与优惠的金额

	@ViewInject(R.id.tv_moneycode)
	private TextView mTv_code;
	protected int mId = -1;
	protected Button mBtnConfirmOrder;
	protected int payId = 0;
	protected int select = -1;// 1:余额参与支付,0在线支付
	protected String mMoney;
	protected String pagName;
	protected List<FeeinfoModel> mListModel = new ArrayList<FeeinfoModel>();
	protected StoreOrderPaymentsFragment mFragPayments;
	protected StoreOrderAccountPaymentFragment mFragAccountPayment;

	protected StoreOrderFeeFragment mFragFees;
	private BigDecimal bd3;
	protected float money = 0;// 消费总金额
	protected Store_ActModel mActModel;
	private int size;
	private String[] arrPay;
	private String[] arrValue;
	protected float money2 = 0;// 不参与优惠的金额

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_store_oeder);
		init();
	}

	private void init() {
		initGetIntent();
		findViews();
		addFragments();
		requestData();
		registeClick();
	}

	private void findViews() {
		mBtnConfirmOrder = (Button) findViewById(R.id.act_confirm_order_btn_confirm_order);
	}

	private void addFragments() {
		// 支付方式列表
		mFragPayments = new StoreOrderPaymentsFragment();
		mFragPayments.setmListener(new StoreOrderPaymentsFragmentListener() {
			@Override
			public void onPaymentChange(Payment_listModel model) {
				if (model != null) {
					payId = model.getId();
					pagName = model.getName();
					BigDecimal bd4 = new BigDecimal(mActModel.getMoney());
					bd4 = bd4.setScale(1, BigDecimal.ROUND_HALF_UP);
					float yueMoney = Float.parseFloat(bd4 + "");
					if (money + money2 <= yueMoney) {
						mFragAccountPayment.clearSelectedPayment(false);
					}
					if (yueMoney == 0) {
						select = 0;
					}
				} else {
					payId = 0;
				}
				culcalatePay();
			}
		});
		getSDFragmentManager().replace(
				R.id.act_store_confirm_order_fl_payments, mFragPayments);

		// 余额支付
		mFragAccountPayment = new StoreOrderAccountPaymentFragment();
		mFragAccountPayment
				.setmListener(new StoreOrderAccountPaymentFragmentListener() {
					@Override
					public void onPaymentChange(boolean isSelected) {
						if (isSelected) {
							select = 1;
							BigDecimal bd4 = new BigDecimal(mActModel
									.getMoney());
							bd4 = bd4.setScale(1, BigDecimal.ROUND_HALF_UP);
							float yueMoney = Float.parseFloat(bd4 + "");
							if (money + money2 <= yueMoney) {
								mFragPayments.clearSelectedPayment(false);
								payId = 0;
								select = 1;
							}

						} else {
							select = 0;

						}
						culcalatePay();
					}
				});
		getSDFragmentManager().replace(
				R.id.act_store_confirm_order_fl_account_payments,
				mFragAccountPayment);

		// 费用信息
		mFragFees = new StoreOrderFeeFragment();
	}

	private void initGetIntent() {
		mId = getIntent().getIntExtra(EXTRA_ID, -1);
		if (mId == -1) {
			finish();
		}
	}

	private void initTitle(Store_ActModel actModel) {
		mTitle.setMiddleTextTop(actModel.getStore_name());
	}

	private void requestData() {
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("discount_pay");
		model.put("store_id", mId);
		model.putUser();
		SDRequestCallBack<Store_ActModel> handler = new SDRequestCallBack<Store_ActModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("正在加载");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dealRequestDataSuccess(actModel);
				mActModel = actModel;
				initTitle(actModel);
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

	protected void dealRequestDataSuccess(Store_ActModel actModel) {
		Intent intent = null;
		switch (actModel.getStatus()) {
		case -1:
			intent = new Intent(mActivity, LoginActivity.class);
			startActivity(intent);

			break;
		case 1:
			bindData(actModel);
			break;

		default:
			break;
		}
	}

	protected void bindData(final Store_ActModel mActModel) {
		if (mActModel == null) {
			return;
		}

		if (mActModel.getDiscount_pay() <= 0) {
			SDViewUtil.hide(v_bot);
			SDViewUtil.hide(v_top);
			SDViewUtil.hide(ll_count);
			
		}
		BigDecimal bd2 = new BigDecimal(
				(100 - mActModel.getDiscount_pay()) / 10.0);
		bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(mTv_discount, bd2 + "折");
		
		mEt_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String number = s.toString();
				if (number.equals("")) {
					SDViewUtil.hide(mTv_code);
					SDViewBinder.setTextView(mTv_discountMoney, "");
					SDViewBinder.setTextView(mTv_storeMoney, "");
					money = 0;
					select = 1;
					culcalatePay();
				} else {
					if (number.startsWith(".")) {
						SDToast.showToast("请输入一个正确的数字!");
						return;
					}
					money = Float.parseFloat(number);
					select = 1;
					/**money 与 money2的值是否合理**/
					if (money2>money) {
						calErrorStatement();
					}else {
						culcalatePay();
					}
					
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (start == 0) {
					SDViewUtil.hide(mTv_code);
					SDViewBinder.setTextView(mTv_discountMoney, "");
					SDViewBinder.setTextView(mTv_storeMoney, "");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();

				if (!isEmpty(number)) {
					if (number.startsWith(".")) {
						SDToast.showToast("请输入一个正确的数字!");
						return;
					}
					money = Float.parseFloat(number);
					if (money > 0) {
						BigDecimal bd2 = new BigDecimal((100 - mActModel
								.getDiscount_pay()) / 10.0);
						/** mActModel.getDiscount_pay()为优惠比例,如85折就是85(int类型) **/
						/** money 为消费总金额 **/
						bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
						SDViewUtil.show(mTv_code);
						BigDecimal bd3 = new BigDecimal((money - money2)
								* ((Float.parseFloat(bd2 + "") / 10.0))
								+ money2);
						/** bd3为实付金额 **/

						bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
						SDViewBinder.setTextView(mTv_storeMoney, bd3 + "元");
						BigDecimal bd4 = new BigDecimal((money- money2)
								* ((10 - Float.parseFloat(bd2 + "")) / 10.0));
						bd4 = bd4.setScale(2, BigDecimal.ROUND_HALF_UP);
						/** bd4 为优惠金额 **/

						SDViewBinder.setTextView(mTv_discountMoney,
								String.valueOf(bd4) + "元");
						onlyUseOnlinePayOrYuePay(bd3.floatValue());
						
					}

				}

			}
		});

		mEt_NoYouhui.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String number = s.toString();
				if (number.equals("")) {
					if (money > 0) {
						SDViewUtil.show(mTv_code);
						BigDecimal bd2 = new BigDecimal((100 - mActModel
								.getDiscount_pay()) / 10.0);
						bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
						SDViewUtil.show(mTv_code);
						BigDecimal bd3 = new BigDecimal(money
								* (Float.parseFloat(bd2 + "") / 10.0));
						bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
						SDViewBinder.setTextView(mTv_storeMoney, bd3 + "元");

					} else {
						SDViewUtil.hide(mTv_code);

					}
					money2 = 0;
					select = 1;
					culcalatePay();
				} else {
					if (number.startsWith(".")) {
						SDToast.showToast("请输入一个正确的数字!");
						return;
					}
					money2 = Float.parseFloat(number);
					select = 1;
					/**money 与 money2的值是否合理**/
					if (money2>money) {
						calErrorStatement();
					}else {
						culcalatePay();
					}
					
//					culcalatePay();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				if (start == 0) {
					if (money > 0) {
						SDViewUtil.show(mTv_code);
						BigDecimal bd2 = new BigDecimal((100 - mActModel
								.getDiscount_pay()) / 10.0);
						bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
						SDViewUtil.show(mTv_code);
						BigDecimal bd3 = new BigDecimal(money
								* (Float.parseFloat(bd2 + "") / 10.0));
						bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
						SDViewBinder.setTextView(mTv_storeMoney, bd3 + "元");
					} else {
						SDViewUtil.hide(mTv_code);
					}
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();

				if (isEmpty(number)) {
					number="0";
				}
				if (!isEmpty(number)) {
					if (number.startsWith(".")) {
						SDToast.showToast("请输入一个正确的数字!");
						return;
					}
					money2 = Float.parseFloat(number);
					if (money2 >= 0) {
						boolean yue_noYouHui_juglement = yue_noYouHui_juglement(
								money, money2, false);
						BigDecimal bd2 = new BigDecimal((100 - mActModel
								.getDiscount_pay()) / 10.0);
						bd2 = yue_noYouHui_juglement ? bd2.setScale(2,
								BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

						SDViewUtil.show(mTv_code);
						BigDecimal bd3 = new BigDecimal((money-money2)
								* ((10-Float.parseFloat(bd2 + "")) / 10.0));
						bd3 = yue_noYouHui_juglement ? bd3.setScale(2,
								BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

						SDViewUtil.show(mTv_code);
						BigDecimal bd1 = new BigDecimal((money - money2)
								* ((Float.parseFloat(bd2 + "") / 10.0f))
								+ money2);
						bd1 = yue_noYouHui_juglement ? bd1.setScale(2,
								BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

						SDViewBinder.setTextView(mTv_storeMoney, bd1 + "元");
						SDViewBinder.setTextView(mTv_discountMoney, bd3+"元");

						onlyUseOnlinePayOrYuePay(bd3.floatValue());
						
					}
				}
			}
		});
		// 支付方式列表
		mFragPayments.setmStore_Model(mActModel);
		// 余额支付
		mFragAccountPayment.setmStore_Model(mActModel);

	}

	public void culcalatePay() {
		/**
		 * arrValue 0 --> 商品总价 arrValue 2 --> 总计 arrValue 3 --> 应付总额
		 */
		if (mListModel != null) {
			mListModel.clear();
		}
		if (money <= 0 && money2 <= 0) {
			mFragFees.setListFeeinfo(mListModel);
			getSDFragmentManager().replace(
					R.id.act_store_confirm_order_fl_fees, mFragFees);
			return;
		}
		BigDecimal bd2 = new BigDecimal(
				(100 - mActModel.getDiscount_pay()) / 10.0);
		bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
		if (payId == 0 && select == 1)// 默认的余额支付
		{
			arrPay = getResources().getStringArray(R.array.my_store_arr);
			arrValue = new String[arrPay.length];
			BigDecimal bd4 = new BigDecimal(money);
			bd4 = bd4.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrValue[0] = bd4 + "元";

			BigDecimal bd1 = new BigDecimal((money - money2)
					* ((Float.parseFloat(bd2 + "") / 10.0)) + money2);
			bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);

			if (Float.parseFloat(mActModel.getMoney())
					- Float.parseFloat(bd1 + "") > 0) {
				// 如果账户余额大于商品价格,显示商品价格
				arrValue[1] = bd1 + "元";
			} else {
				BigDecimal bd0 = new BigDecimal(mActModel.getMoney());
				bd0 = bd0.setScale(2, BigDecimal.ROUND_HALF_UP);
				// 否则显示自己的余额.
				arrValue[1] = bd0 + "元";
			}
			arrValue[2] = bd1 + "元";
		} else if (payId != 0 && select == 1) {
			arrPay = getResources().getStringArray(R.array.my_store_arr2);
			arrValue = new String[arrPay.length];
			BigDecimal bd4 = new BigDecimal(money);
			bd4 = bd4.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrValue[0] = bd4 + "元";
			arrValue[1] = pagName;
			BigDecimal bd1 = new BigDecimal((money - money2)
					* ((Float.parseFloat(bd2 + "") / 10.0)) + money2);
			bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);

			BigDecimal bd0 = new BigDecimal(mActModel.getMoney());
			bd0 = bd0.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrValue[2] = bd0 + "元";
			arrValue[3] = bd1 + "元";
			BigDecimal bd3 = new BigDecimal(
					 Float.parseFloat(bd1 + ""));
			bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrValue[4] = bd3 + "元";
		} else if (payId != 0 && select == 0) {
			arrPay = getResources().getStringArray(R.array.my_store);
			arrValue = new String[arrPay.length];

			BigDecimal bd4 = new BigDecimal(money);
			bd4 = bd4.setScale(2, BigDecimal.ROUND_HALF_UP);

			arrValue[0] = bd4 + "元";
			arrValue[1] = pagName;
			BigDecimal bd1 = new BigDecimal((money - money2)
					* ((Float.parseFloat(bd2 + "") / 10.0)) + money2);
			bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrValue[2] = bd1 + "元";
			arrValue[3] = bd1 + "元";
			
		}

		for (int i = 0; i < arrPay.length; i++) {
			FeeinfoModel feeinfoModel = new FeeinfoModel();
			feeinfoModel.setName(arrPay[i]);
			feeinfoModel.setValue(arrValue[i]);
			mListModel.add(feeinfoModel);
		}
		mFragFees.setListFeeinfo(mListModel);
		getSDFragmentManager().replace(R.id.act_store_confirm_order_fl_fees,
				mFragFees);
	}

	/**
	 * 确认支付
	 */
	protected void requestDoneOrder() {
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("do_discount_pay");
		model.putUser();
		model.put("store_id", mId);
		model.put("deal_total_price", money);
		model.put("payment", payId);
		model.put("all_account_money", select);
		model.put("deal_other_price", money2);

		SDRequestCallBack<Cart_doneActModel> handler = new SDRequestCallBack<Cart_doneActModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("正在加载");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dealRequestDoneOrderSuccess(actModel);
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

	protected void dealRequestDoneOrderSuccess(Cart_doneActModel actModel) {
		Intent intent = null;
		switch (actModel.getStatus()) {
		case -1:
			intent = new Intent(mActivity, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case 1:
			CommonInterface.updateCartNumber();
			SDEventManager.post(EnumEventTag.DONE_CART_SUCCESS.ordinal());
			intent = new Intent(mActivity, PayActivity.class);
			intent.putExtra(PayActivity.EXTRA_ORDER_ID, actModel.getOrder_id());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	private void registeClick() {
		mBtnConfirmOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!yue_noYouHui_juglement(money, money2, true)) {
					return;
				}
				BigDecimal bd4 = new BigDecimal(mActModel.getMoney());
				bd4 = bd4.setScale(1, BigDecimal.ROUND_HALF_UP);
				float yueMoney = Float.parseFloat(bd4 + "");
				if (money == 0) {
					SDToast.showToast("请输入消费金额");
				} else if (money > yueMoney && payId == 0) {
					SDToast.showToast("请选择支付方式");

				} else {
					if (v.isClickable()) {
						v.setBackgroundResource(R.drawable.layer_main_color_corner_press);
						v.setClickable(false);
						//60s可点
						
					}else{
						v.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
						v.setClickable(true);
					}
					requestDoneOrder();
				}
			}

		});
	}

	@Override
	protected void onNeedRefreshOnResume() {
		requestData();
		super.onNeedRefreshOnResume();
	}

	/** 当总的支付金额 可以 被 账户余额 支付 时,用户只能选择 在线支付和余额支付 中的一种 **/
	private void onlyUseOnlinePayOrYuePay(float shouldpay) {

		float yue = Float.parseFloat(mActModel.getMoney());
		if (shouldpay <= yue) {
			// 只能使用其中一种支付方式
			// 默认选择余额支付

			// culcalatePay();
			select = -1;
		}
	}

	/** 不参与优惠的金额应该小于 消费总额 **/
	private boolean yue_noYouHui_juglement(float yue, float noYouHui,
			boolean showToast) {
		if (yue < noYouHui) {
			// 不参与优惠的金额变红
			mEt_NoYouhui.setTextColor(getResources().getColor(
					R.color.store_youhui_money));
			if (showToast) {
				SDToast.showToast("不参与优惠的金额过大!");
			}
			return false;
		}
		mEt_NoYouhui.setTextColor(getResources()
				.getColor(R.color.text_new_home));
		return true;
	}

	private void calErrorStatement() {
		if (mListModel != null) {
			mListModel.clear();
		}
		if (money <= 0 && money2 <= 0) {
			mFragFees.setListFeeinfo(mListModel);
			getSDFragmentManager().replace(
					R.id.act_store_confirm_order_fl_fees, mFragFees);
			return;
		}
		if (payId == 0 && select == 1)// 默认的余额支付
		{

			arrValue[0] = 0 + "元";
			arrValue[1] = 0 + "元";
			arrValue[2] = 0 + "元";
		} else if (payId != 0 && select == 1) {
			arrPay = getResources().getStringArray(R.array.my_store_arr2);
			arrValue = new String[arrPay.length];
			arrValue[0] = 0 + "元";
			arrValue[1] = 0 + "元";
			arrValue[2] = 0 + "元";
			arrValue[3] = 0 + "元";
			arrValue[4] = 0 + "元";
		} else if (payId != 0 && select == 0) {
			arrPay = getResources().getStringArray(R.array.my_store);
			arrValue = new String[arrPay.length];

			arrValue[0] = 0 + "元";
			arrValue[1] = pagName;
			arrValue[2] = 0 + "元";
			arrValue[3] = 0 + "元";

		}

		for (int i = 0; i < arrPay.length; i++) {
			FeeinfoModel feeinfoModel = new FeeinfoModel();
			feeinfoModel.setName(arrPay[i]);
			feeinfoModel.setValue(arrValue[i]);
			mListModel.add(feeinfoModel);
		}
		mFragFees.setListFeeinfo(mListModel);
		getSDFragmentManager().replace(R.id.act_store_confirm_order_fl_fees,
				mFragFees);

	}
}