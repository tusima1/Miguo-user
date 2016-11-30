package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	@ViewInject(R.id.mPlv_account_money)
	private LinearLayout mPlv_account_money;
	@ViewInject(R.id.tv_name)
	private TextView account_money_text;
	@ViewInject(R.id.iv_selected)
	private CheckBox account_checkBox;

	private OrderDetailAccountPaymentFragmentListener mListener;

	public void setmListener(OrderDetailAccountPaymentFragmentListener listener)
	{
		this.mListener = listener;
	}

	public int getUseAccountMoney()
	{
		int useAccountMoney;
		if (account_checkBox.isChecked())
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
	}

	private void bindData()
	{
		if (!toggleFragmentView(mCheckActModel))
		{
			return;
		}
		double accountMoney = SDTypeParseUtil.getDouble(mCheckActModel.getUserAccountMoney());
		if (accountMoney <= 0)
		{
			account_checkBox.setChecked(false);
			hideFragmentView();
			return;
		} else
		{
			showFragmentView();
		}

		account_money_text.setText("账户余额：" + mCheckActModel.getUserAccountMoney());

		account_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mListener.onPaymentChange(isChecked);
			}
		});

	}
	


	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}

	public void clearSelectedPayment(boolean notify)
	{
		account_checkBox.setChecked(false);

		if (notify)
		{
			if (mListener != null)
			{
				mListener.onPaymentChange(false);
			}
		}
	}

	public interface OrderDetailAccountPaymentFragmentListener
	{
		void onPaymentChange(boolean isSelected);
	}

}