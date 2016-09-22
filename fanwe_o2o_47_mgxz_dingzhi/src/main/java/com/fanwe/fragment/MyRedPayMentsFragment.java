package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.user.view.RedPacketListActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class MyRedPayMentsFragment extends OrderDetailBaseFragment{
	
	@ViewInject(R.id.ll_myred)
	private LinearLayout mLl_red;
	
	@ViewInject(R.id.tv_myred)
	private TextView mTv_red;
	
	@ViewInject(R.id.tv_totalred)
	private TextView mTv_totalRed;
	
	protected int request_CODE = 100;

	private ArrayList<String> red_packet_list;
	
	protected MyredPaymentsFragmentListener mListener;

	private int mId;

	String mRedIds = "";
	
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
		if(mCheckActModel!=null&&!TextUtils.isEmpty(mCheckActModel.getId())) {
			Intent intent = new Intent(getActivity(), RedPacketListActivity.class);
			intent.putStringArrayListExtra(ShoppingCartconstants.RED_IDS,  red_packet_list);
			intent.putExtra(ShoppingCartconstants.LIST_DEAL_IDS, mCheckActModel.getId());
			startActivityForResult(intent, request_CODE);
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==request_CODE)  
        {
			if(data!=null) {
				Bundle bundle = data.getExtras();
				red_packet_list = bundle.getStringArrayList("selectedIDs");

				StringBuffer mRedIdsBuffer = new StringBuffer();
				if(red_packet_list!=null&&red_packet_list.size()>0){
					for (int i = 0; i < red_packet_list.size(); i++) {
						mRedIdsBuffer.append(red_packet_list.get(i)+",");
					}
					mRedIds = mRedIdsBuffer.substring(0,mRedIdsBuffer.length()-1);
				}else{
					mRedIds="";
				}

				if (mListener != null) {
					mListener.onRedPaymentChange(mRedIds);
				}
			}
		}
	}


	public String getmRedIds() {
		return mRedIds;
	}

	public void setmRedIds(String mRedIds) {
		this.mRedIds = mRedIds;
	}

	@Override
	protected void onRefreshData() {
		initGetIntent();
		super.onRefreshData();
	}
	public interface MyredPaymentsFragmentListener
	{
		public void onRedPaymentChange(String  mRedIds);
	}
}
