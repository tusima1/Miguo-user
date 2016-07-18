package com.fanwe.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.fanwe.MyRedPaymentActivity;
import com.fanwe.fragment.OrderDetailPaymentsFragment.OrderDetailPaymentsFragmentListener;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.model.CartGroupGoodsModel;
import com.fanwe.model.Payment_listModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyRedPayMentsFragment extends OrderDetailBaseFragment{
	
	@ViewInject(R.id.ll_myred)
	private LinearLayout mLl_red;
	
	@ViewInject(R.id.tv_myred)
	private TextView mTv_red;
	
	@ViewInject(R.id.tv_totalred)
	private TextView mTv_totalRed;
	
	protected int request_CODE = 101;
	protected ArrayList<Integer> mList = new ArrayList<Integer>();
	
	protected MyredPaymentsFragmentListener mListener;

	private int mId;

	private BigDecimal bd1;

	private BigDecimal bd;

	private BigDecimal bd2;
	
	public void setmListener(MyredPaymentsFragmentListener listener)
	{
		this.mListener = listener;
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.flag_my_red);
	}
	@Override
	protected void init() {
		super.init();
		initGetIntent();
		registerClick();
	}
	protected void initGetIntent() {
		if (!toggleFragmentView(mCheckActModel))
		{
			return;
		}
		if(mCheckActModel.getRed_packet_total() > 0 && mCheckActModel.getRed_packet_info() != null)
		{
			bd = new BigDecimal(mCheckActModel.getRed_packet_info().getMoney());
			bd1 = new BigDecimal(mCheckActModel.getRed_packet_total());
			String arr[] = String.valueOf(mCheckActModel.getRed_packet_info().getMoney()).split("\\.");
			String arr1[] = String.valueOf(mCheckActModel.getRed_packet_total()).split("\\.");
			if(Integer.parseInt(arr[1]) > 0 )
			{
				bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
				
			}else
			{
				bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
				
			}
			mTv_red.setText("￥"+bd);
			mTv_red.setTextColor(Color.parseColor("#FB6F08"));
			mLl_red.setClickable(true);
			if(Integer.parseInt(arr1[1]) >0)
			{
				bd1 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
			}else
			{
				bd1 = bd1.setScale(0, BigDecimal.ROUND_HALF_UP);
			}
			mTv_totalRed.setText("最高可抵"+bd1+"元");
		}else
		{
			bd2 = new BigDecimal(mCheckActModel.getRed_packet_total());
			String arr1[] = String.valueOf(mCheckActModel.getRed_packet_total()).split("\\.");
			if(Integer.parseInt(arr1[1]) > 0)
			{
				bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP);
			}else
			{
				bd2 = bd2.setScale(0, BigDecimal.ROUND_HALF_UP);
			}
			mTv_totalRed.setText("最高可抵"+bd2+"元");
			mTv_red.setText("无红包可使用");
			mLl_red.setClickable(false);
		}
		
	}
	private void registerClick() {
		
		mLl_red.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ll_myred:
			clickMyRed();
			break;
			
		default:
			break;
		}
	}
	private void clickMyRed() {
		Intent intent = new Intent(getActivity(),MyRedPaymentActivity.class);
		mId =mCheckActModel.getRed_packet_info().getId();
		intent.putExtra("id", mId);
		startActivityForResult(intent, request_CODE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==request_CODE)  
        {  
            if (resultCode==MyRedPaymentActivity.result_CODE)  
            {  
            	Bundle bundle = data.getExtras();
                mList = bundle.getIntegerArrayList("mId");
                if(mListener != null)
                {
                	mListener.onRedPaymentChange(mList);
                }
                float money = bundle.getFloat("money");
                if(mList == null || money == 0)
                {
                	mTv_red.setText("不使用红包");
                }else
                {
                	String arr[] = String.valueOf(money).split("\\.");
                	BigDecimal bd = new BigDecimal(money);
                	if(Integer.parseInt(arr[1]) >0)
                	{
                		bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                	}else
                	{
                		bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                	}
                	mTv_red.setText("￥"+bd);
                	mTv_red.setTextColor(Color.parseColor("#FB6F08"));
            			
                }
            }  
        }  
	}
	@Override
	protected void onRefreshData() {
		initGetIntent();
		super.onRefreshData();
	}
	public interface MyredPaymentsFragmentListener
	{
		public void onRedPaymentChange(ArrayList list);
	}
}
