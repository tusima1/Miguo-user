package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.ConfirmOrderActivity;
import com.fanwe.ConfirmTopUpActivity;
import com.fanwe.adapter.PaymentAdapter;
import com.fanwe.customview.MaxHeightListView;
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


	private PaymentAdapter.PaymentTypeChangeListener mListener;

	/** 选中的支付方式id */
	private String mPaymentId;
	@ViewInject(R.id.paylistview)
	private MaxHeightListView paylistview;

	private List<PaymentTypeInfo> listPayment;

	private PaymentAdapter paymentAdapter;


	public void setmListener(PaymentAdapter.PaymentTypeChangeListener listener)
	{
		this.mListener = listener;
	}

	public String  getPaymentId()
	{
		return paymentAdapter.getCurrentPaymentId();
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
		paymentAdapter = new PaymentAdapter(listPayment,true);
		if(mListener!=null){
			paymentAdapter.setPaymentTypeChangeListener(mListener);
		}
		paylistview.setAdapter(paymentAdapter);
	}

	private void resetParams()
	{
		this.mPaymentId = "";
	}

	private void bindData()
	{
		resetParams();
		// TODO 生成支付方式
		paymentAdapter.update(listPayment);

	}

	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}

	public void clearSelectedPayment(boolean notify)
	{
		for(int i = 0 ; i < listPayment.size() ;i ++) {
			PaymentTypeInfo paymentTypeInfo = listPayment.get(i);
			paymentTypeInfo.setChecked(false);
		}
		paymentAdapter.update(listPayment);
		resetParams();
		if (notify)
		{
			if (mListener != null)
			{
				mListener.onPaymentChange(null);
			}
		}
	}


	public List<PaymentTypeInfo> getListPayment() {
		return listPayment;
	}

	public void setListPayment(List<PaymentTypeInfo> listPayment) {
		this.listPayment = listPayment;
		bindData();
	}


}