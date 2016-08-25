package com.fanwe.fragment;

import java.math.BigDecimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.fanwe.customview.SDPaymentListView;
import com.fanwe.fragment.OrderDetailAccountPaymentFragment.OrderDetailAccountPaymentFragmentListener;
import com.fanwe.library.customview.SDViewBase.SDViewBaseListener;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class StoreOrderAccountPaymentFragment extends StoreConfirmOrderBaseFragment{

	//@ViewInject(R.id.plv_account_money)
	protected SDPaymentListView mPlv_account_money;

	private StoreOrderAccountPaymentFragmentListener mListener;
	
	public void setmListener(StoreOrderAccountPaymentFragmentListener listener)
	{
		this.mListener = listener;
	}

	public int getUseAccountMoney()
	{
		int useAccountMoney = 0;
		if (mPlv_account_money.ismSelected())
		{
			useAccountMoney = 1;
		} else
		{
			useAccountMoney = 0;
		}
		return useAccountMoney;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_order_detail_account_payment);
	}

	@Override
	protected void init()
	{
		super.init();
		bindData();
		afterBindData();
	}

	private void bindData()
	{
		if (!toggleFragmentView(store_ActModel))
		{
			return;
		}

		
		double accountMoney = SDTypeParseUtil.getDouble(store_ActModel.getMoney());
		if (accountMoney <= 0)
		{
			mPlv_account_money.onNormal();
			hideFragmentView();
			return;
		} else
		{
			showFragmentView();
		}
		BigDecimal bd2 = new BigDecimal(store_ActModel.getMoney());
		bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_UP);
		mPlv_account_money.mTv_name.setText("账户余额：" +bd2);
		if (mPlv_account_money.ismSelected())
		{
			mPlv_account_money.onSelected();
		} else
		{
			mPlv_account_money.onNormal();
		}

		mPlv_account_money.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mPlv_account_money.toggleSelected();
			}
		});

		mPlv_account_money.setmListenerState(new SDViewBaseListener()
		{
			@Override
			public void onSelected_SDViewBase(View v)
			{
				if (mListener != null)
				{
					mListener.onPaymentChange(mPlv_account_money.ismSelected());
				}
			}

			@Override
			public void onPressed_SDViewBase(View v)
			{
			}

			@Override
			public void onNormal_SDViewBase(View v)
			{
				if (mListener != null)
				{
					mListener.onPaymentChange(mPlv_account_money.ismSelected());
				}
			}

			@Override
			public void onFocus_SDViewBase(View v)
			{
			}
		});

	}

	private void afterBindData()
	{
		mPlv_account_money.onSelected();
	}

	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}

	public void clearSelectedPayment(boolean notify)
	{
		mPlv_account_money.onNormal();
		if (notify)
		{
			if (mListener != null)
			{
				mListener.onPaymentChange(mPlv_account_money.ismSelected());
			}
		}
	}

	public interface StoreOrderAccountPaymentFragmentListener
	{
		public void onPaymentChange(boolean isSelected);
	}

}
