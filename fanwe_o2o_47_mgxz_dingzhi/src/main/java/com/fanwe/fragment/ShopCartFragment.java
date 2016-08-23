package com.fanwe.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fanwe.ConfirmOrderActivity;
import com.fanwe.MainActivity;
import com.fanwe.adapter.ShopCartAdapter;
import com.fanwe.adapter.ShopCartAdapter.ShopCartSelectedListener;
import com.fanwe.app.AppHelper;
import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.HorizontalSlideDeleteListView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.model.Cart_check_cartActModel;
import com.fanwe.model.Cart_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 购物车
 *
 * @author js02
 *
 */
public class ShopCartFragment extends BaseFragment {
	@ViewInject(R.id.lv_cart_goods)
	private HorizontalSlideDeleteListView mLvCartGoods;
	@ViewInject(R.id.rl_empty)
	private RelativeLayout mRlEmpty;
	/** 手机登录布局 */
	@ViewInject(R.id.ll_phone_login)
	private LinearLayout mLlPhoneLogin;
	/** 手机登录输入手机号的输入框 */
	@ViewInject(R.id.et_mobile)
	private ClearEditText mEt_mobile;
	/** 验证码输入框 */
	@ViewInject(R.id.et_code)
	private ClearEditText mEt_code;
	/** 推荐人手机号 */
	@ViewInject(R.id.et_reference)
	private ClearEditText mEt_reference;
	/** 发送验证码按钮 */
	@ViewInject(R.id.btn_send_code)
	private SDSendValidateButton mBtn_send_code;

	@ViewInject(R.id.tv_unlogin_buy)
	private TextView mTvUnLoginBuy;

	/** 结算 */
	@ViewInject(R.id.ll_bottom)
	private LinearLayout mLl_count;

	/** 编辑下得全选 */
	@ViewInject(R.id.iv_xuanze)
	private CheckBox mCB_xuanze;

	@ViewInject(R.id.tv_sum)
	private TextView mTv_sum;

	@ViewInject(R.id.bt_account)
	private Button mBt_account;

	/** 删除 */
	@ViewInject(R.id.ll_compile)
	private LinearLayout mLl_compile;

	@ViewInject(R.id.cb_xuanze)
	private CheckBox mCb_xuanze;

	@ViewInject(R.id.bt_addTo_collect)
	private Button mBt_addToCollect;

	@ViewInject(R.id.bt_delect)
	private Button mBt_delect;

	@ViewInject(R.id.content_ptr)
	private PullToRefreshScrollView mContentPtr;//内容部分,可下拉刷新

	private ShopCartAdapter mAdapter;
	private Cart_indexActModel mActModel;

	private String mStrMobile;
	private String mStrCode;
	private String mStrReference;
	/**
	 * 当前是否编辑状态。
	 */
	private boolean mFalg = false;
	private boolean isScore = false;
	/**
	 * 购物车商品列表。
	 */
	private List<CartGoodsModel> listModel;

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.frag_shop_cart);
	}

	@Override
	protected void init() {
		super.init();
		initTitle();
		registeClick();
		initSDSendValidateButton();
		initPull2RefreshSrcollView();
	}

	/**
	 * 添加下拉刷新功能
	 */
	private void initPull2RefreshSrcollView() {
		mContentPtr.setMode(Mode.PULL_FROM_START);
		mContentPtr.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				requestData();
				resetInitData();
			}
		});
		mContentPtr.setRefreshing();
	}

	protected void resetInitData() {
		mFalg=false;
		isScore=false;
		mActModel=null;
		listModel=null;
		mCb_xuanze.setChecked(false);
		mCB_xuanze.setChecked(false);
		SDViewUtil.hide(mLl_compile);


		//重置下巴(结算)
		mBt_account.setText("结算");
		mBt_account.setBackgroundColor(getResources().getColor(
				R.color.text_fenxiao));
		mBt_account.setClickable(false);
		mTv_sum.setText("0.00");
	}

	/**
	 * 初始化发送验证码按钮
	 */
	private void initSDSendValidateButton() {
		mBtn_send_code.setmListener(new SDSendValidateButtonListener() {
			@Override
			public void onTick() {

			}

			@Override
			public void onClickSendValidateButton() {
				requestSendCode();
			}
		});
	}

	/**
	 * 请求验证码
	 */
	protected void requestSendCode() {
		mStrMobile = mEt_mobile.getText().toString();
		if (TextUtils.isEmpty(mStrMobile)) {
			SDToast.showToast("请输入手机号码");
			mEt_mobile.requestFocus();
			return;
		}
		CommonInterface.requestValidateCode(mStrMobile, 0,
				new SDRequestCallBack<Sms_send_sms_codeActModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() > 0) {
							mBtn_send_code.stopTickWork();
							mBtn_send_code.setmDisableTime(actModel
									.getLesstime());
							mBtn_send_code.startTickWork();
						}
					}

					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("");
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	private void registeClick() {
		mTvUnLoginBuy.setOnClickListener(this);
		mBt_account.setOnClickListener(this);
		mBt_addToCollect.setOnClickListener(this);
		mBt_delect.setOnClickListener(this);
		// 编辑状态下
		mCB_xuanze.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = mCB_xuanze.isChecked();
				BigDecimal bd = checkListModelStateAndSumMoney(isChecked);
				mTv_sum.setText(String.valueOf(bd));
				if (isChecked) {
					mBt_account.setText("结算" + "（" + listModel.size() + "）");
					mBt_account.setBackgroundColor(getResources().getColor(
							R.color.main_color));
					mBt_account.setClickable(true);
				} else {
					mBt_account.setText("结算");
					mBt_account.setBackgroundColor(getResources().getColor(
							R.color.text_fenxiao));
					mBt_account.setClickable(false);
				}
			}
		});

		// 删除状态下
		mCb_xuanze.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int size = 0;
				mBt_delect.setClickable(isChecked);
				if (listModel == null || listModel.size() < 1) {
					return;
				}
				size = listModel.size();
				for (int i = 0; i < size; i++) {
					CartGoodsModel model = listModel.get(i);
					model.setEdit(isChecked);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 初始化标题栏。
	 */
	private void initTitle() {
		mTitle.setMiddleTextTop("购物车");
		if (getActivity() instanceof MainActivity) {
			mTitle.setLeftImageLeft(0);
		}
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextBot("编辑");
	}

	/**
	 * 改变购物车列表里面的商品是否被选择属性,同时计算总金额。。
	 *
	 * @param isChecked
	 *            是否被选择。
	 */
	private BigDecimal checkListModelStateAndSumMoney(boolean isChecked) {
		int size = 0;
		float sumMoney = 0.00f;
		BigDecimal value = new BigDecimal(0.00);
		if (listModel == null || listModel.size() < 1) {
			return value;
		}
		size = listModel.size();
		for (int i = 0; i < size; i++) {
			CartGoodsModel model = listModel.get(i);
			model.setChecked(isChecked);

			if (model.isChecked()) {
				sumMoney += model.getSumPrice();
			}
		}
		mAdapter.notifyDataSetChanged();
		value = new BigDecimal(sumMoney);
		value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
		return value;
	}

	/**
	 * 更新总金额和结算数量,以及是否全选的状态。
	 */
	public void updateSumMoneyAndCount() {
		BigDecimal sumMoney = getSumMoney();
		int count = getSumSeleted(1);
		if (count == listModel.size()) {
			mCB_xuanze.setChecked(true);
		} else {
			mCB_xuanze.setChecked(false);
		}
		mTv_sum.setText(String.valueOf(sumMoney));
		if (count > 0) {
			mBt_account.setText("结算" + "（" + count + "）");
			mBt_account.setBackgroundColor(getResources().getColor(
					R.color.main_color));
			mBt_account.setClickable(true);
			mBt_delect.setClickable(true);
		} else {
			mBt_account.setText("结算");
			mBt_account.setBackgroundColor(getResources().getColor(
					R.color.text_fenxiao));
			mBt_account.setClickable(false);
			mBt_delect.setClickable(false);
		}

	}

	/**
	 * 获取总金额。
	 *
	 * @return
	 */
	private BigDecimal getSumMoney() {
		int size = 0;
		float sumMoney = 0.00f;
		BigDecimal value = new BigDecimal(0.00);
		if (listModel == null || listModel.size() < 1) {
			return value;
		}
		size = listModel.size();
		for (int i = 0; i < size; i++) {
			CartGoodsModel model = listModel.get(i);
			if (model.isChecked()) {
				sumMoney += model.getSumPrice();
			}
		}
		value = new BigDecimal(sumMoney);
		value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
		return value;
	}

	/**
	 * 获取已经选中听选项.
	 *
	 * @param type
	 *            type=1 获取非编辑状态下的已经被选择的数量。type=2 获取编辑状态下的被选择的数量。
	 * @return
	 */
	private Integer getSumSeleted(int type) {
		int size = 0;
		int count = 0;
		if (listModel == null || listModel.size() < 1) {
			return count;
		}
		size = listModel.size();
		for (int i = 0; i < size; i++) {
			CartGoodsModel model = listModel.get(i);
			boolean checked = false;
			if (type == 1) {
				checked = model.isChecked();
			} else {
				checked = model.isEdit();
			}
			if (checked) {
				count++;
			}
		}
		return count;
	}

	private ArrayList<Integer> getSumSeletedIds(int type) {
		int size = 0;
		if (listModel == null || listModel.size() < 1) {
			return null;
		}
		ArrayList<Integer> mSeletedGoods = new ArrayList<Integer>();
		size = listModel.size();
		for (int i = 0; i < size; i++) {
			CartGoodsModel model = listModel.get(i);
			boolean checked = false;
			if (type == 1) {
				checked = model.isChecked();
			} else {
				checked = model.isEdit();
			}
			if (checked) {
				if (type == 1) {
					mSeletedGoods.add(model.getId());
				} else {
					mSeletedGoods.add(model.getDeal_id());
				}
			}
		}
		return mSeletedGoods;
	}

	/**
	 * 计算每一个各类商品的总小计金额。
	 */
	private void initSumPrice() {
		int size = 0;
		if (listModel == null || listModel.size() < 1) {
			return;
		}
		size = listModel.size();
		for (int i = 0; i < size; i++) {
			float sumPrice = 0.00f;
			CartGoodsModel model = listModel.get(i);
			int is_first = model.getIs_first();
			is_first -= model.getCheck_first();
			if (is_first > 0) {
				if (is_first > model.getNumber()) {
					is_first = model.getNumber();
				}
			} else {
				is_first = 0;
			}
			sumPrice = is_first * model.getIs_first_price();
			sumPrice += (model.getNumber() - is_first)
					* Float.parseFloat(model.getUnit_price());
			model.setSumPrice(sumPrice);
		}
	}

	@Override
	public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
		super.onCLickLeft_SDTitleSimple(v);
		requestCheckCart2();
	}

	  private void requestCheckCart2()
			{
				if(mAdapter == null)
				{
					return;
				}

				RequestModel request = new RequestModel();
				request.putCtl("cart");
				request.putAct("check_cart");
				request.putUser();
				if(mAdapter != null)
				{
					request.put("num", mAdapter.getMapNumber());
				}
				SDRequestCallBack<Cart_check_cartActModel> handler = new SDRequestCallBack<Cart_check_cartActModel>()
					{
						@Override
						public void onStart()
						{

						}
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo)
						{
						if (actModel.getStatus() == 1)
						{

						}
					}

					@Override
					public void onFinish()
					{

					}
				};
				InterfaceServer.getInstance().requestInterface(request, handler);
		}
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
		if (mFalg) {
			SDViewUtil.hide(mLl_compile);
			if (mAdapter.getCount() != 0) {
				SDViewUtil.show(mLl_count);
			}
			mTitle.getItemRight(0).setTextBot("编辑");
			mFalg = false;

		} else {
			SDViewUtil.hide(mLl_count);
			if (mAdapter.getCount() != 0) {
				SDViewUtil.show(mLl_compile);
			}
			mTitle.getItemRight(0).setTextBot("完成");
			mFalg = true;
		}
		if (mAdapter != null) {
		//	mAdapter.setmIsDelect(mFalg);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void getmAdapterListener() {
		mAdapter.setOnShopCartSelectedListener(new ShopCartSelectedListener() {
			@Override
			public void onSelectedListener() {
				updateSumMoneyAndCount();
			}

			@Override
			public void onDelSelectedListener(CartGoodsModel model,
					boolean isChecked) {
				// 非编辑状态
				if (mFalg) {
					int count = getSumSeleted(2);
					if (count == listModel.size()) {
						mCb_xuanze.setChecked(true);
					} else {
						mCb_xuanze.setChecked(false);
					}
				}
			}

			@Override
			public void onTitleNumChangeListener(int num) {
				updateTitleNum(num);
			}
		});
		mLvCartGoods.setAdapter(mAdapter);
	}

	/**
	 * 去结算
	 */
	private void clickSettleAccounts() {
		if (mLlPhoneLogin.getVisibility() == View.VISIBLE) {

			mStrMobile =mEt_mobile.getText().toString();
			if (TextUtils.isEmpty(mStrMobile)) {
				SDToast.showToast("请输入手机号");
				return;
			}
			mStrCode = mEt_code.getText().toString();
			if (TextUtils.isEmpty(mStrCode)) {
				SDToast.showToast("请输入验证码");
				return;
			}
			mStrReference = mEt_reference.getText().toString();
		}
		/*
		 * if (!AppHelper.isLogin(getActivity())) // 未登录 {
		 * SDToast.showToast("登入后立即去结算！"); }else{
		 */
		// TODO 去结算
		requestCheckCart();
	}

	private void requestCheckCart() {

		if (mActModel == null) {
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("check_cart");
		model.putUser();
		if (mAdapter != null) {
			model.put("num", mAdapter.getMapNumber());
		}

		model.put("mobile", mStrMobile);
		model.put("sms_verify", mStrCode);
		model.put("invite_mobile", mStrReference);
		SDRequestCallBack<Cart_check_cartActModel> handler = new SDRequestCallBack<Cart_check_cartActModel>() {
			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍候");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					CommonInterface.updateCartNumber();
					User_infoModel model = actModel.getUser_data();
					if (model != null) {
						LocalUserModel.dealLoginSuccess(model, false);
					}
					SDViewUtil.hide(mLlPhoneLogin);
					// TODO 跳到确认订单界面
					startConfirmOrderActivity();
				}
			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	/**
	 * 进入商品购买确认页。
	 */
	private void startConfirmOrderActivity() {
		if (listModel != null && listModel.size() > 0 && !mFalg) {
			ArrayList<Integer> mSeletedGoods = getSumSeletedIds(1);

			Intent intent = new Intent(getActivity(),
					ConfirmOrderActivity.class);
			Bundle bundle = new Bundle();
			bundle.putIntegerArrayList("list_id", mSeletedGoods);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			requestData();
		}
	}

	private void requestData() {
		CommonInterface
				.requestCart(new SDRequestCallBack<Cart_indexActModel>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							mActModel = actModel;
							bindData();
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {
						mContentPtr.onRefreshComplete();
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	protected void bindData() {
		if (mActModel == null) {
			return;
		}

		ShopcartRecommendFragment fragRecommend = new ShopcartRecommendFragment();
		fragRecommend.setmActModel(mActModel);
		getSDFragmentManager().replace(R.id.frag_shop_cart_fl_recommend,
				fragRecommend);

		if (mActModel.getIs_score() == 1) {
			isScore = true;
		}

		listModel = mActModel.getListCartGoods();
		if (listModel == null) {
			mTitle.setMiddleTextTop("购物车");
			SDViewUtil.hide(mLl_count);
			SDViewUtil.hide(mLl_compile);
			mTitle.initRightItem(0);
		} else {
			mTitle.initRightItem(1);
			if (mFalg) {
				mTitle.getItemRight(0).setTextBot("完成");
				SDViewUtil.show(mLl_compile);
			} else {
				mTitle.getItemRight(0).setTextBot("编辑");
				SDViewUtil.show(mLl_count);
			}
			mTitle.setMiddleTextTop("购物车" + "（" + listModel.size() + "）");
		}
		initSumPrice();
		SDViewUtil.toggleEmptyMsgByList(listModel, mRlEmpty);
		// 初始化adapter.
//		mAdapter = new ShopCartAdapter(listModel, getActivity(), isScore,
//				false, mTv_sum);
		getmAdapterListener();
		mLvCartGoods.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		changeViewState();
	}

	private void changeViewState() {
		if (listModel == null) {
			return;
		}
		if (AppHelper.isLogin()) {
			SDViewUtil.hide(mLlPhoneLogin);
		} else {
			SDViewUtil.show(mLlPhoneLogin);
			// 把图片隐藏
			SDViewUtil.hide(mRlEmpty);
			changeUnloginBuyButton();
		}
	}

	private void changeUnloginBuyButton() {
		if (AppHelper.isLogin()) {
			mTvUnLoginBuy.setText("绑定手机号码");
			SDViewUtil.hide(mEt_reference);
		} else {
			mTvUnLoginBuy.setText("登录");
			int registerRebate = AppRuntimeWorker.getRegister_rebate();
			switch (registerRebate) {
			case 1:
			case 2:
				SDViewUtil.show(mEt_reference);
				break;

			default:
				SDViewUtil.hide(mEt_reference);
				break;
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			requestData();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_unlogin_buy:
			clickSettleAccounts();
			break;
		case R.id.bt_account:
			clickSettleAccounts();
			break;
		case R.id.bt_addTo_collect:
			clickAddToCollect();
			break;
		case R.id.bt_delect:
			clickDelect(false);
			break;
		default:
			break;
		}

	}

	/**
	 * 删除商品事件。
	 */
	private void clickDelect(final boolean formCollect) {
		// 如果是从移除收藏夹跳转过来。
		int size = getSumSeleted(2);
		if (size == 0) {
			SDToast.showToast("请选择想删除的商品");
			return;
		}
		RequestModel request = new RequestModel();
		request.putCtl("cart");
		request.putAct("del");
		request.putUser();
		final ArrayList<CartGoodsModel> deleteList = new ArrayList<CartGoodsModel>();
		ArrayList<Integer> deleteIds = new ArrayList<Integer>();
		for (int i = 0; i < listModel.size(); i++) {
			CartGoodsModel model = listModel.get(i);
			if (model.isEdit()) {
				deleteIds.add(model.getId());
				deleteList.add(model);
			}
		}
		request.put("id", deleteIds);
		SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {
			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("正在删除");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					// 删除成功
					listModel.removeAll(deleteList);
			//		mAdapter.setData(listModel);
					mAdapter.notifyDataSetChanged();

					SDEventManager.post(EnumEventTag.DELETE_CART_GOODS_SUCCESS
							.ordinal());
					int size = listModel.size();
					updateTitleNum(size);
					deleteList.clear();
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
		InterfaceServer.getInstance().requestInterface(request, handler);
	}

	/**
	 * 更新Title上的商品数量
	 * @param num
	 */
	private void updateTitleNum(int num){
		if (num == 0) {
			SDViewUtil.hide(mLl_compile);
			SDViewUtil.hide(mLl_count);
			mTitle.setMiddleTextTop("购物车");
		}else {
			mTitle.setMiddleTextTop("购物车" + "（" + num + "）");
		}
	}

	/**
	 * 移动到收藏夹。
	 */
	private void clickAddToCollect() {
		int size = getSumSeleted(2);
		if (size == 0) {
			SDToast.showToast("请选择想移动到收藏夹的商品");
			return;
		}
		if (!AppHelper.isLogin()) {
			SDToast.showToast("请先登入!");
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("deal");
		model.putAct("add_collects");
		ArrayList<Integer> collectIds = getSumSeletedIds(2);

		model.put("id", collectIds);
		model.putUser();
		SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					clickDelect(true);
					SDToast.showToast(actModel.getInfo());
				}
			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(HttpMethod.POST, model,
				null, false, handler);
	}

	@Override
	protected void onNeedRefreshOnResume() {
		requestData();
		super.onNeedRefreshOnResume();
	}

	@Override
	public void onEventMainThread(SDBaseEvent event) {
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt())) {
		case DONE_CART_SUCCESS:
			mAdapter.updateData(null);
			setmIsNeedRefreshOnResume(true);
			break;
		case ADD_CART_SUCCESS:
			setmIsNeedRefreshOnResume(true);
			break;
		case DELETE_CART_GOODS_SUCCESS:
			setmIsNeedRefreshOnResume(true);
			break;
		case LOGIN_SUCCESS:
			setmIsNeedRefreshOnResume(true);
			break;
		case LOGOUT:
			changeUnloginBuyButton();
			break;
		case CONFIRM_IMAGE_CODE:
			if (SDActivityManager.getInstance().isLastActivity(getActivity())) {
				requestSendCode();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		mBtn_send_code.stopTickWork();
		super.onDestroy();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}