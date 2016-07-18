package com.fanwe;

import java.util.ArrayList;

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
import com.fanwe.fragment.OrderDetailParamsFragment.OrderDetailParamsFragmentListener;
import com.fanwe.fragment.OrderDetailPaymentsFragment;
import com.fanwe.fragment.OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.model.Cart_checkActModel;
import com.fanwe.model.Cart_count_buy_totalModel;
import com.fanwe.model.Cart_doneActModel;
import com.fanwe.model.Payment_listModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 确认订单
 * 
 * @author js02
 * 
 */
public class ConfirmOrderActivity extends BaseActivity
{
	public static final int REQUEST_CODE_DELIVERY_ADDRESS = 1;

	protected PullToRefreshScrollView mPtrsvAll;

	protected Button mBtnConfirmOrder;

	protected Cart_checkActModel mCheckActModel;
	
	protected TextView tv_dingdan;
	
	protected ArrayList<Integer> mList =new ArrayList<Integer>();
	// -------------------------fragments
	//商品详情
	protected OrderDetailGoodsFragment mFragGoods;
	//订单参数
	protected OrderDetailParamsFragment mFragParams;
	//订单详情
	protected OrderDetailPaymentsFragment mFragPayments;
	//余额支付
	protected OrderDetailAccountPaymentFragment mFragAccountPayment;
	//费用项目
	protected OrderDetailFeeFragment mFragFees;
	//红包抵扣
	protected MyRedPayMentsFragment mFragMyRed;

	private ArrayList<Integer> mListDeal_id;

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
		initIntentData();
		findViews();
		initTitle();
		registeClick();
		addFragments();
		initPullToRefreshScrollView();
	}

	private void initIntentData()
	{
		mListDeal_id=getIntent().getExtras().getIntegerArrayList("list_id");
	}

	private void findViews()
	{
		mPtrsvAll = (PullToRefreshScrollView) findViewById(R.id.act_confirm_order_ptrsv_all);
		mBtnConfirmOrder = (Button) findViewById(R.id.act_confirm_order_btn_confirm_order);
//		tv_dingdan = (TextView)findViewById(R.id.tv_panduan);
	}

	private void addFragments()
	{
		// 绑定商品数据
		mFragGoods = new OrderDetailGoodsFragment();
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_goods, mFragGoods);
		
		// 订单参数
		mFragParams = new OrderDetailParamsFragment();
		mFragParams.setmListener(new OrderDetailParamsFragmentListener()
		{
			@Override
			public void onCalculate()
			{
				requestCalculate();
			}
		});
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_params, mFragParams);

		// 支付方式列表
		mFragPayments = new OrderDetailPaymentsFragment();
		mFragPayments.setmListener(new OrderDetailPaymentsFragmentListener()
		{
			@Override
			public void onPaymentChange(Payment_listModel model)
			{
				requestCalculate();
			}
		});
		getSDFragmentManager().replace(R.id.act_confirm_order_fl_payments, mFragPayments);
		//红包
		mFragMyRed = new MyRedPayMentsFragment();
		mFragMyRed.setmListener(new MyredPaymentsFragmentListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onRedPaymentChange(ArrayList list) {
				
				mList = list;
				requestCalculate();
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
				
				requestCalculate();
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
	 * 获取数据
	 */
	protected void requestData()
	{
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("check");
		model.putUser();
		model.put("deals", mListDeal_id);

		SDRequestCallBack<Cart_checkActModel> handler = new SDRequestCallBack<Cart_checkActModel>()
		{
			
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("正在加载");
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				dealRequestDataSuccess(actModel);
				
			}
			
			@Override
			public void onFailure(HttpException error, String msg)
			{

			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				mPtrsvAll.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	protected void dealRequestDataSuccess(Cart_checkActModel actModel)
	{
		Intent intent = null;
		switch (actModel.getStatus())
		{
		case -1:
			intent = new Intent(mActivity, LoginActivity.class);
			startActivity(intent);
			break;
		case 1:
			mCheckActModel = actModel;
			if(mCheckActModel.getRed_packet_total()>0 && mCheckActModel.getRed_packet_info() != null)
			{
				mList.add(mCheckActModel.getRed_packet_info().getId());
			}else 
			{
				mList.clear();
			}
			bindData();
			requestCalculate();
			break;

		default:
			break;
		}
	}

	protected void bindData()
	{
		if (mCheckActModel == null)
		{
			return;
		}
//		if(mCheckActModel.getListCartGroupsGoods().size() > 1)
//		{
//			
//			SDViewUtil.show(tv_dingdan);
//		}else
//		{
//			SDViewUtil.hide(tv_dingdan);
//		}
		
		// 绑定商品数据
		mFragGoods.setmCheckActModel(mCheckActModel);

		// 订单参数
		mFragParams.setmCheckActModel(mCheckActModel);
		
		// 支付方式列表
		mFragPayments.setmCheckActModel(mCheckActModel);
		
		// 余额支付
		mFragAccountPayment.setmCheckActModel(mCheckActModel);
		
		//红包
		mFragMyRed.setmCheckActModel(mCheckActModel);
	}
	
	protected void fillCalculateParams(RequestModel model)
	{
		if (model != null)
		{
			if (mFragParams != null)
			{
				model.put("delivery_id", mFragParams.getDelivery_id());
				model.put("ecvsn", mFragParams.getEcv_sn());
			}
			if (mFragPayments != null)
			{
				model.put("payment", mFragPayments.getPaymentId());
			}
			if (mFragAccountPayment != null)
			{
				model.put("all_account_money", mFragAccountPayment.getUseAccountMoney());
			}
		}
	}

	/**
	 * 计算价格
	 */
	protected void requestCalculate()
	{
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("count_buy_total");
		model.put("deals", mListDeal_id);
		if(!mList.isEmpty() || mList != null)
		{
			model.put("red_packet_ids", mList);
			
		}else
		{
			model.put("red_packet_ids", null);
		}
		model.putUser();
		fillCalculateParams(model);
		SDRequestCallBack<Cart_count_buy_totalModel> handler = new SDRequestCallBack<Cart_count_buy_totalModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("正在加载");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				// TODO 绑定所需费用信息
				dealRequestCalculateSuccess(actModel);
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
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

			mFragFees.setListFeeinfo(actModel.getFeeinfo());

			// TODO 更新各商家运费
			if (mFragGoods != null)
			{
				mCheckActModel.setCalculateModel(actModel);
				mFragGoods.setmCheckActModel(mCheckActModel);
			}

			break;

		default:
			break;
		}
	}

	protected void fillDoneOrderParams(RequestModel model)
	{
		if (model != null)
		{
			if (mFragParams != null)
			{
				model.put("delivery_id", mFragParams.getDelivery_id());
				model.put("ecvsn", mFragParams.getEcv_sn());
				model.put("content", mFragParams.getContent());
			}
			if (mFragPayments != null)
			{
				model.put("payment", mFragPayments.getPaymentId());
			}
			if (mFragAccountPayment != null)
			{
				model.put("all_account_money", mFragAccountPayment.getUseAccountMoney());
			}
		}
	}

	/**
	 * 确认订单
	 */
	protected void requestDoneOrder()
	{
		
		 
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("done");
		model.put("deals", mListDeal_id);
		model.put("red_packet_ids",mList);
		model.putUser();
		fillDoneOrderParams(model);

		SDRequestCallBack<Cart_doneActModel> handler = new SDRequestCallBack<Cart_doneActModel>()
		{
			
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("正在加载");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				dealRequestDoneOrderSuccess(actModel);
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{

			}

			@Override
			public void onFinish()
			{
				mBtnConfirmOrder.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
				mBtnConfirmOrder.setClickable(true);
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

	}

	protected void dealRequestDoneOrderSuccess(Cart_doneActModel actModel)
	{
		Intent intent = null;
		switch (actModel.getStatus())
		{
		case -1:
			intent = new Intent(mActivity, LoginActivity.class);
			startActivity(intent);
			break;
		case 1:
			CommonInterface.updateCartNumber();
			SDEventManager.post(EnumEventTag.DONE_CART_SUCCESS.ordinal());
			intent = new Intent(mActivity, PayActivity.class);
			intent.putExtra(PayActivity.EXTRA_ORDER_ID, actModel.getOrder_id());
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	private void registeClick()
	{
		mBtnConfirmOrder.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v){
				
				
				//支付方式选择。
//				if(mFragPayments==null|| mFragPayments.getPaymentId()==0){
//					
//				}
//				
				
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

	public void sonFragemtMethod() {
		mFragAccountPayment.performClick();
	}
}