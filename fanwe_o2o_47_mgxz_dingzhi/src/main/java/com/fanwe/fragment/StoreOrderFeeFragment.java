package com.fanwe.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.adapter.OrderFeeItemAdapter;
import com.fanwe.model.FeeinfoModel;
import com.fanwe.o2o.miguo.R;

public class StoreOrderFeeFragment extends StoreConfirmOrderBaseFragment{
	
	//@ViewInject(R.id.frag_order_detail_fee_ll_fees)
	private LinearLayout mLlFees = null;

	
	private List<FeeinfoModel> mListModel;

	public void setListFeeinfo(List<FeeinfoModel> listModel)
	{
		this.mListModel = listModel;
		refreshData();
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_order_detail_fee);
	}

	@Override
	protected void init()
	{
		super.init();
		bindData();
	}
	
	

	private void bindData()
	{
		if (!toggleFragmentView(mListModel))
		{
			return;
		}

		mLlFees.removeAllViews();
		OrderFeeItemAdapter adapter = new OrderFeeItemAdapter(mListModel, getActivity());
		for (int i = 0; i < mListModel.size(); i++)
		{
			View itemView = adapter.getView(i, null, null);
			mLlFees.addView(itemView);
		}
	}

	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}
}
