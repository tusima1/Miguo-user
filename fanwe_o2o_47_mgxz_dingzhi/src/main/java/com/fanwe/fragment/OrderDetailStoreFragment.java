package com.fanwe.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class OrderDetailStoreFragment extends StoreConfirmOrderBaseFragment{
	
	@ViewInject(R.id.act_store_et_money_text)
	private ClearEditText mEt_money;
	
	@ViewInject(R.id.tv_store_discount)
	private TextView mTv_discount;
	
	@ViewInject(R.id.tv_store_discount_money)
	private TextView mTv_discountMoney;
	
	@ViewInject(R.id.tv_store_money)
	private TextView mTv_storeMoney;

	private String mStrMoney;
	
	private StoreOrderDetailFragmentListener mListener;
	
	
	public void setmListener(StoreOrderDetailFragmentListener listener)
	{
		this.mListener = listener;
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_order_detail_store);
	}
	
	@Override
	protected void init() {
		
		super.init();
		bindData();
	}
	private void bindData() {
		/*if (!toggleFragmentView(store_ActModel))
		{
			return;
		}*/
		SDViewBinder.setTextView(mTv_discount, String.valueOf((10 - store_ActModel.getDiscount_pay()/10)));
		
		/*mEt_money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();
				if(! number.equals(""))
				{
					int money = Integer.parseInt(number);
					if(money <= 0 )
					{
						MGToast.showToast("请输入金额");
						mEt_money.setTextColor(Color.parseColor("#ff0000"));
					}else
					{
						mEt_money.setText(Color.parseColor("#888888"));
						SDViewBinder.setTextView(mTv_storeMoney, 
								Float.parseFloat(mEt_money.getText().toString())*((10-(store_ActModel.getDiscount_pay()/10)/10))+"元");
						SDViewBinder.setTextView(mTv_discountMoney,String.valueOf(Float.parseFloat(mEt_money.getText().toString())
								-Float.parseFloat(mTv_storeMoney.getText().toString())));
						if(mListener != null)
						{
							mListener.editMoneyChange(true, number);
						}
					}
				}else
				{
					if(mListener != null)
					{
						mListener.editMoneyChange(false, "");
					}
				}
			}
		});*/
	}
	
	public interface StoreOrderDetailFragmentListener
	{
		public void editMoneyChange(String money);
	}
}
