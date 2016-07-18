package com.fanwe.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.fanwe.customview.SDPaymentListView;
import com.fanwe.library.customview.SDViewBase.SDViewBaseListener;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 订单详情页（余额支付fragment）
 * 
 * @author js02
 * 
 */
public class OrderDetailAccountPaymentFragment extends OrderDetailBaseFragment
{

	@ViewInject(R.id.plv_account_money)
	private SDPaymentListView mPlv_account_money;

	private OrderDetailAccountPaymentFragmentListener mListener;

	public void setmListener(OrderDetailAccountPaymentFragmentListener listener)
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
		if (!toggleFragmentView(mCheckActModel))
		{
			return;
		}

		int hasAccount = mCheckActModel.getHas_account();
		if (hasAccount == 0)
		{
			mPlv_account_money.onNormal();
			hideFragmentView();
			return;
		}

		double accountMoney = SDTypeParseUtil.getDouble(mCheckActModel.getAccount_money());
		if (accountMoney <= 0)
		{
			mPlv_account_money.onNormal();
			hideFragmentView();
			return;
		} else
		{
			showFragmentView();
		}

		mPlv_account_money.mTv_name.setText("账户余额：" + mCheckActModel.getAccount_moneyFormat());
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
	
	/**当被选中时无法再选择其他的支付方式,所以在选择其他支付方式前手动取消 余额支付 的选中状态**/
	public void performClick() {
		boolean ismSelected = mPlv_account_money.ismSelected();
		if (ismSelected) {
			mPlv_account_money.performClick();
		}
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

	public interface OrderDetailAccountPaymentFragmentListener
	{
		public void onPaymentChange(boolean isSelected);
	}

}