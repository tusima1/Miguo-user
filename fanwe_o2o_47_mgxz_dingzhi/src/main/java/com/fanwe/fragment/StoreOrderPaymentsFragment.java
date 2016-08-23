package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.customview.SDPaymentListView;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.Mode;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.Payment_listModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class StoreOrderPaymentsFragment extends StoreConfirmOrderBaseFragment{

	@ViewInject(R.id.ll_payment)
	private LinearLayout mLl_payment;

	private StoreOrderPaymentsFragmentListener mListener;
	private SDViewNavigatorManager mManager = new SDViewNavigatorManager();
	/** 选中的支付方式id */
	private int mPaymentId;

	public void setmListener(StoreOrderPaymentsFragmentListener listener)
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

	@SuppressWarnings("rawtypes")
	private void bindData()
	{
		if (!toggleFragmentView(store_ActModel))
		{
			resetParams();
			return;
		}
		final List<Payment_listModel> listPayment = store_ActModel.getPayment_list();
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
			//view.setData(model);
			listView.add(view);
			mLl_payment.addView(view);
		}
		mManager.setItems(SDCollectionUtil.toArray(listView));
		mManager.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
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

	public interface StoreOrderPaymentsFragmentListener
	{
		public void onPaymentChange(Payment_listModel model);
	}

}
