package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
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
import com.fanwe.fragment.OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.model.Cart_count_buy_totalModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.fanwe.shoppingcart.model.ShoppingBody;
import com.fanwe.shoppingcart.presents.CommonShoppingHelper;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.utils.SDFormatUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 确认订单
 * 
 * @author js02
 * 
 */
public class ConfirmOrderActivity extends BaseActivity implements RefreshCalbackView
{
	/**
	 * 下拉刷 新。
	 */
	protected PullToRefreshScrollView mPtrsvAll;
	/**
	 *确认订单。
	 */
	protected Button mBtnConfirmOrder;

	protected ShoppingBody mCheckActModel;
	/**
	 * 商品IDS.
	 */
	protected ArrayList<String> mList =new ArrayList<String>();
	// -------------------------fragments
	/**
	 *  商品详情
	 */
	protected OrderDetailGoodsFragment mFragGoods;
	//订单参数
	protected OrderDetailParamsFragment mFragParams;
	//支付方式详情
	protected OrderDetailPaymentsFragment mFragPayments;
	//余额支付
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
	float totalFloat=0.00f;

	//用户余额。
	float yueFloat=0.00f;

	private  PaymentTypeInfo currentPayType;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_confirm_order);
		init();
	}

	protected void init()
	{
		outSideShoppingCartHelper= new OutSideShoppingCartHelper(this);
		commonShoppingHelper = new CommonShoppingHelper(this);
		initIntentData();
		findViews();
		initTitle();
		registeClick();
		addFragments();
		initPullToRefreshScrollView();
	}

	private void initIntentData()
	{
		mListDeal_id=getIntent().getExtras().getString("list_id");
	}

	private void findViews()
	{
		mPtrsvAll = (PullToRefreshScrollView) findViewById(R.id.act_confirm_order_ptrsv_all);
		mBtnConfirmOrder = (Button) findViewById(R.id.act_confirm_order_btn_confirm_order);
	}

	private void addFragments()
	{
		// 绑定商品数据
		mFragGoods = new OrderDetailGoodsFragment();
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_goods, mFragGoods);
		
		// 留言信息
		mFragParams = new OrderDetailParamsFragment();
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_params, mFragParams);

		// 支付方式列表
		mFragPayments = new OrderDetailPaymentsFragment();
		getPayType();
		mFragPayments.setmListener(new OrderDetailPaymentsFragmentListener() {

			@Override
			public void onPaymentChange(PaymentTypeInfo model) {
				currentPayType = model;
				changePayType(1);
			}
		});

		getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments, mFragPayments);
		//红包
		mFragMyRed = new MyRedPayMentsFragment();
		mFragMyRed.setmListener(new MyredPaymentsFragmentListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onRedPaymentChange(ArrayList list) {
				
				if(list!=null&&list.size()>0) {
                    String redIDs = listToString(list);
					requestBenefit(redIDs);
				}
			}
		});
		getSDFragmentManager().replace(R.id.act_my_red_pay,mFragMyRed);
		
		// 余额支付
		mFragAccountPayment = new OrderDetailAccountPaymentFragment();
		mFragAccountPayment.setmListener(new OrderDetailAccountPaymentFragmentListener()
		{
			@Override
			public void onPaymentChange(boolean isSelected)
			{

				changePayType(0);
			}
		});

		getSDFragmentManager().replace(R.id.act_confirm_order_fl_account_payments, mFragAccountPayment);

		// 费用信息
		mFragFees = new OrderDetailFeeFragment();
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_fees, mFragFees);
	}

	private void initPullToRefreshScrollView()
	{
		mPtrsvAll.setMode(Mode.PULL_FROM_START);
		mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				requestData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				
			}

		});
		mPtrsvAll.setRefreshing();
	}

	/**
	 *
	 * 改变支付方式 0 余额支付 。1 第三方支付
	 * @param type
     */
	public void changePayType(int type){
		//总金额。
		 totalFloat = SDFormatUtil.stringToFloat(mCheckActModel.getTotal());
		//用户余额。
		 yueFloat = SDFormatUtil.stringToFloat(mCheckActModel.getUserAccountMoney());
       //当前 选择余额支付
		if(type==0){
			if(totalFloat<=yueFloat){
				mFragPayments.clearSelectedPayment(false);
				mFragFees.setCurrentFeeInfoModel(null);
			}
			mFragFees.setIfYueChecked(true);
			mFragFees.refreshData();
		}else{
			if(totalFloat<=yueFloat){
				mFragAccountPayment.clearSelectedPayment(false);
			}
			mFragFees.setCurrentFeeInfoModel(currentPayType);
			mFragFees.setIfYueChecked(false);
			mFragFees.refreshData();
		}
	}
  /*
  取支付方式 。
   */
	public void getPayType(){
		commonShoppingHelper.getPayment();
	}
	/**
	 * 获取数据
	 */
	protected void requestData()
	{
		outSideShoppingCartHelper.getDingdanDetail(mListDeal_id);
	}

	protected void dealRequestDataSuccess(ShoppingBody actModel)
	{

		   mCheckActModel = actModel;
			bindData();

	}

	protected void bindData()
	{
		if (mCheckActModel == null)
		{
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
	}

	/**
	 * 绑定 支付方式
	 * @param datas
     */
	private void bindPayment(List<PaymentTypeInfo> datas){
		if(datas!=null&&datas.size()>0){
			mFragPayments.setListPayment(datas);
		}
	}

	/**
	 * 显示优惠后金额。
	 * @param datas
     */
	private void dealBenifit(List<HashMap<String,String>> datas){
		if(datas!=null){
			HashMap<String,String> maps =datas.get(0);
			String youhuiPrice = maps.get(ShoppingCartconstants.YOUHUI_PRICE);
			mCheckActModel.setYouhuiPrice(youhuiPrice);
			mFragFees.refreshData();
		}

	}
	
	protected void fillCalculateParams(RequestModel model)
	{
		if (model != null) {
			if (mFragParams != null) {

				if (mFragPayments != null) {
					model.put("payment", mFragPayments.getPaymentId());
				}
				if (mFragAccountPayment != null) {
					model.put("all_account_money", mFragAccountPayment.getUseAccountMoney());
				}
			}
		}
	}

	/**
	 * 选择红包后取优惠金额。
	 * @param redIDs
     */
	private void requestBenefit(String redIDs){
		outSideShoppingCartHelper.getBenefitCount(mCheckActModel.getId(),redIDs);
	}

	protected void dealRequestCalculateSuccess(Cart_count_buy_totalModel actModel)
	{
		Intent intent = null;
		switch (actModel.getStatus())
		{
		case -1:
			intent = new Intent(mActivity, LoginActivity.class);
			startActivity(intent);
			break;
		case 1:
			// 支付方式不让选中,或者隐藏
			double payPrice = SDTypeParseUtil.getDouble(actModel.getPay_price());
			if (payPrice == 0)
			{
				if (mFragPayments != null)
				{
					mFragPayments.clearSelectedPayment(false);
//					mFragPayments.setIsCanSelected(false);
				}
			} else
			{
				if (mFragPayments != null)
				{
					mFragPayments.setIsCanSelected(true);
				}
			}

			break;

		default:
			break;
		}
	}


	/**
	 * 确认并 生成订单。
	 */
	protected void requestDoneOrder()
	{
		if (mFragPayments != null&&mFragParams!=null&&mFragMyRed!=null&&mFragAccountPayment!=null)
		{
			//支付方式
			String payDisp = mFragPayments.getPaymentId();
			// 留言
			String content = mFragParams.getContent();
			//红包列表。
			String red_id =mFragMyRed.getIdList();
			//是否使用余额支付。
			String balances_flg = mFragAccountPayment.getUseAccountMoney()+"";
			//提交并生成订单。
			outSideShoppingCartHelper.createOrder(mCheckActModel.getId(), balances_flg, red_id, payDisp,content);
		}else{
			return;
		}
	}

	public void onFinish(){
		mBtnConfirmOrder.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
		mBtnConfirmOrder.setClickable(true);
	}

	/**
	 *  生成订单并 进入支付页。
	 * @param datas
     */

	protected void dealRequestDoneOrderSuccess(List datas)
	{
		if(datas!=null&&datas.size()>0){
			HashMap<String,String> map =(HashMap<String,String>)datas.get(0);
	    	Intent intent = null;
			CommonInterface.updateCartNumber();
			SDEventManager.post(EnumEventTag.DONE_CART_SUCCESS.ordinal());
			intent = new Intent(mActivity, PayActivity.class);
			intent.putExtra(PayActivity.EXTRA_ORDER_ID, map.get(ShoppingCartconstants.ORDER_ID));
			startActivity(intent);
			finish();
		}else{
			SDToast.showToast("订单提交失败。");
		}

	}

	private void registeClick()
	{
		mBtnConfirmOrder.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v){
				
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
		});
	}

	protected void initTitle()
	{
		mTitle.setMiddleTextTop("确认订单");
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case USER_DELIVERY_CHANGE:
			setmIsNeedRefreshOnResume(true);
			break;
			
		default:
			break;
		}
	}
	@Override
	protected void onNeedRefreshOnResume()
	{
		requestData();
		super.onNeedRefreshOnResume();
	}

	/**
	 * 选择支付方式 。
	 */
	public void checkPaymentMethod() {

		mFragAccountPayment.performClick();
	}

	@Override
	public void onFailue(String method, String message) {
		switch (method){
			case ShoppingCartconstants.SP_CART_TOORDER_GET:
			 	SDToast.showToast(message);
				onFinish();
				break;
			case ShoppingCartconstants.ORDER_INFO_CREATE:
                 SDToast.showToast(message);
				break;
			default:
				break;
		}
	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onSuccess(String method, List datas) {
		switch (method){
			case ShoppingCartconstants.SP_CART_TOORDER_GET:
				onFinish();
				if(datas!=null&&datas.size()>0){
					dealRequestDataSuccess((ShoppingBody) datas.get(0));
				}

				break;
			case ShoppingCartconstants.ORDER_INFO_CREATE:
				dealRequestDoneOrderSuccess(datas);
				   break;
			case ShoppingCartconstants.GET_PAYMENT:
				bindPayment(datas);
				break;
			case ShoppingCartconstants.SP_CART_TOORDER_POST:
				dealBenifit(datas);
			default:
				break;
		}

	}

	/**
	 * list 转 string.
	 * @param list
	 * @return
     */
	private String listToString(ArrayList<String> list){
		int size = list.size();
		StringBuffer str = new StringBuffer();
		for(int i = 0 ; i < size ; i++){
			str.append(list.get(i)+",");
		}
		if(str.length()>1){
			return str.substring(0,str.length()-1);
		}
		return "";
	}
	@Override
	public void onFailue(String responseBody) {

	}
}