package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.ConfirmOrderActivity;
import com.fanwe.ConfirmTopUpActivity;
import com.fanwe.customview.SDPaymentListView;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.Mode;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付方式（支付方式fragment）
 *
 * @author js02
 *
 */
public class OrderDetailPaymentsFragment extends OrderDetailBaseFragment
{
	@ViewInject(R.id.ll_payment)
	private LinearLayout mLl_payment;

	private OrderDetailPaymentsFragmentListener mListener;
	private SDViewNavigatorManager mManager = new SDViewNavigatorManager();
	/** 选中的支付方式id */
	private String mPaymentId;

	private List<PaymentTypeInfo> listPayment;


	public void setmListener(OrderDetailPaymentsFragmentListener listener)
	{
		this.mListener = listener;
	}

	public String  getPaymentId()
	{
		return mPaymentId;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_order_detail_payments);
	}

	@Override
	protected void init()
	{
		super.init();
		mManager.setmMode(Mode.CAN_NONE_SELECT);
		//bindData();
	}

	private void resetParams()
	{
		this.mPaymentId = "";
	}

	private void bindData()
	{
		resetParams();
		// TODO 生成支付方式

		if(mLl_payment!=null){
			mLl_payment.removeAllViews();
		}
		List<SDViewBase> listView = new ArrayList<SDViewBase>();
		PaymentTypeInfo foundModel = null;
		for (PaymentTypeInfo model : listPayment)
		{
			if (model.getId() == this.mPaymentId)
			{
				foundModel = model;
			}
			SDPaymentListView view = new SDPaymentListView(getActivity());
			view.setData(model);
			listView.add(view);
			mLl_payment.addView(view);
		}

		mManager.setItems(SDCollectionUtil.toArray(listView));
		mManager.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{

				/**
				 * @author didikee
				 * @date 2016年6月15日 上午10:24:16
				 * 一行代码
				 * #14 通过我的小店购买商品，在订单确认页面，如果已经选择了余额付，不能选择支付宝或者微信付，不勾选余额付，可以选择支付宝或者微信付，但是通过线下付的方式到订单确认页面，如果选择了余额付，可以选择支付宝或者微信付
				 */
				if (OrderDetailPaymentsFragment.this.getActivity() instanceof ConfirmOrderActivity) {
//					ConfirmOrderActivity activity = (ConfirmOrderActivity)OrderDetailPaymentsFragment.this.getActivity();
//					activity.checkPaymentMethod();
				}else if (OrderDetailPaymentsFragment.this.getActivity() instanceof ConfirmTopUpActivity) {
					ConfirmTopUpActivity activity = (ConfirmTopUpActivity)OrderDetailPaymentsFragment.this.getActivity();
					activity.sonFragemtMethod();
				}

				PaymentTypeInfo model = SDCollectionUtil.get(listPayment, index);
				if (model != null)
				{
					mPaymentId = model.getId();
				} else
				{
					resetParams();
				}
				if (mListener != null)
				{
					mListener.onPaymentChange(model);
				}
			}
		});

		// 还原选中
		if (foundModel != null)
		{
			int index = listPayment.indexOf(foundModel);
			mManager.setSelectIndex(index, null, true);
			mPaymentId = foundModel.getId();
		} else
		{
			resetParams();
		}
	}

	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}

	public void clearSelectedPayment(boolean notify)
	{
		mManager.clearSelected();
		resetParams();
		if (notify)
		{
			if (mListener != null)
			{
				mListener.onPaymentChange(null);
			}
		}
	}

	public void setIsCanSelected(boolean isCanSelected)
	{
		mManager.setmClickAble(isCanSelected);
	}

	public interface OrderDetailPaymentsFragmentListener
	{
		public void onPaymentChange(PaymentTypeInfo model);
	}

	public List<PaymentTypeInfo> getListPayment() {
		return listPayment;
	}

	public void setListPayment(List<PaymentTypeInfo> listPayment) {
		this.listPayment = listPayment;
		bindData();
	}


}