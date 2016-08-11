package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanwe.adapter.HoltelSearchAdapter;
import com.fanwe.adapter.HoltelSearchAdapter.OnResultSearchListener;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.model.Holtel_cityAct;
import com.fanwe.model.Holtel_indexActmode;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BusinessCircleFragment extends BaseFragment
{
	
	protected List<String>list = new ArrayList<String>();
	@ViewInject(R.id.lv_business)
	protected ListView mLv_business;
	protected HoltelSearchAdapter mAdapter;
	private List<Holtel_cityAct> mList = new ArrayList<Holtel_cityAct>();
	protected OnSearchListListener mListener;
	
	public void setOnSearchData(OnSearchListListener listener)
	{
		this.mListener = listener;
	}
	
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_business);
	}
	
	@Override
	protected void init() 
	{	
		super.init();
		initRequest();
		bindData();
	}
	
	private void initRequest() 
	{
		RequestModel model = new RequestModel();
		model.putCtl("hotel");
		model.putAct("get_conf");
		model.put("city",AppRuntimeWorker.getCityIdByCityName(getArguments().getString("city")));   
		SDRequestCallBack<Holtel_indexActmode> handler = new SDRequestCallBack<Holtel_indexActmode>()
		{
			
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					mList =actModel.getQuan_list();
					mAdapter.updateData(mList);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{

			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	
	protected void bindData()
	{
		mAdapter = new HoltelSearchAdapter(mList, getActivity(),getArguments().getInt("id"));
		mAdapter.setOnResultSearchListener(new OnResultSearchListener() {
			
			@Override
			public void resultSearch(Bundle bundle) {
				if(mListener != null)
				{
					mListener.setSearchData(bundle);
				}
			}
		});
		mLv_business.setAdapter(mAdapter);
		
	}
	public interface OnSearchListListener
	{
		public void setSearchData(Bundle bundle);
		
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
