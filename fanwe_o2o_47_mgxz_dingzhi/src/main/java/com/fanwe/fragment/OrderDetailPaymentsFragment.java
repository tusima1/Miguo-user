package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.fanwe.model.Payment_listModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 订单详情页（支付方式fragment）
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
	private int mPaymentId;

	public void setmListener(OrderDetailPaymentsFragmentListener listener)
	{
		this.mListener = listener;
	}

	public int getPaymentId()
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
		bindData();
	}

	private void resetParams()
	{
		this.mPaymentId = 0;
	}

	private void bindData()
	{
		if (!toggleFragmentView(mCheckActModel))
		{
			resetParams();
			return;
		}
		if (!toggleFragmentView(mCheckActModel.getShow_payment()))
		{
			resetParams();
			return;
		}
		final List<Payment_listModel> listPayment = mCheckActModel.getPayment_list();
		if (!toggleFragmentView(listPayment))
		{
			resetParams();
			return;
		}

		// TODO 生成支付方式
		mLl_payment.removeAllViews();
		List<SDViewBase> listView = new ArrayList<SDViewBase>();
		Payment_listModel foundModel = null;
		for (Payment_listModel model : listPayment)
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
					ConfirmOrderActivity activity = (ConfirmOrderActivity)OrderDetailPaymentsFragment.this.getActivity();
					activity.sonFragemtMethod();
				}else if (OrderDetailPaymentsFragment.this.getActivity() instanceof ConfirmTopUpActivity) {
					ConfirmTopUpActivity activity = (ConfirmTopUpActivity)OrderDetailPaymentsFragment.this.getActivity();
					activity.sonFragemtMethod();
				}
				
				Payment_listModel model = SDCollectionUtil.get(listPayment, index);
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
		public void onPaymentChange(Payment_listModel model);
	}

}