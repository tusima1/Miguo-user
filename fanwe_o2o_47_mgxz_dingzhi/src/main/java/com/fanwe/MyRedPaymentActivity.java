package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyRedEnvelopeAdapter;
import com.fanwe.adapter.MyRedPaymentAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.RedEnvelopeModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_ecv_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;


public class MyRedPaymentActivity extends BaseActivity {
	
	public static int result_CODE =101;

	@ViewInject(R.id.ptrsv_all)
	private PullToRefreshListView mPtrsv_all;
	
	@ViewInject(R.id.ch_box)
	private CheckBox mCh_box;
	
	@ViewInject(R.id.bt_confirm)
	private Button mBt_confirm;

	protected Uc_ecv_indexActModel mActModel;
	
	protected MyRedPaymentAdapter mAdapter;
	protected List<RedEnvelopeModel> mListModel = new ArrayList<RedEnvelopeModel>();

	private int mId;

	private ArrayList<Integer> mList = new ArrayList<Integer>();

	protected RedEnvelopeModel redEnvelopeModel;

	protected float money=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.activity_my_red_payment);
		init();
	}

	private void init() {
		
		initGetIntent();
		initTitle();
		initDefaultData();
		initRegisterClick();
		initPullToRefreshListView();
	}

	private void initGetIntent() {
		
		mId = getIntent().getIntExtra("id", -1);
		
	}

	private void initRegisterClick() {
		
		if(mAdapter != null)
		{	mList.clear();
			mList =mAdapter.getPosition();
		}
		mCh_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked)
				{
					mList.clear();;
				}else
				{	mList.clear();
					mList =mAdapter.getPosition();
				}
			}
		});
		mBt_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mList == null || isEmpty(mList) && mCh_box.isChecked() == false)
				{
					SDToast.showToast("请选择红包");
					return;
				}
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				
				bundle.putIntegerArrayList("mId", mList);
				if(mList != null)
				{
					for (int i = 0; i < mList.size(); i++) {
						int redId = mList.get(i);
						for(RedEnvelopeModel model:mActModel.getList())
						{
							if(model.getId() == redId)
							{
								money = money + model.getMoney();
							}
						}
					}
				}
				bundle.putFloat("money", money);
				intent.putExtras(bundle);
				setResult(result_CODE, intent);
				finish();
			}
		});
	}

	private void initDefaultData() {
		
		mAdapter = new MyRedPaymentAdapter(mListModel, this,mId);
		mPtrsv_all.getRefreshableView().setAdapter(mAdapter);
	}

	private void initPullToRefreshListView() {
		mPtrsv_all.setMode(Mode.PULL_FROM_START);
		mPtrsv_all.setOnRefreshListener(new OnRefreshListener2<ListView>()
				{
					@Override
					public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
					{
						
						requestData();
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
					{
						
					}
				});
		mPtrsv_all.setRefreshing();
	}

	protected void requestData() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_red_packet");
		model.put("invalid", 0 );
		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Uc_ecv_indexActModel actModel = JsonUtil.json2Object(responseInfo.result, Uc_ecv_indexActModel.class);
				if (actModel.getStatus() > 0)
				{
					mActModel = actModel;
					mAdapter.updateData(actModel.getList());
				}
			}
			
			@Override
			public void onFinish()
			{
				mPtrsv_all.onRefreshComplete();
				super.onFinish();
			}
		});
	}

	private void initTitle() {
		
		mTitle.setMiddleTextTop("红包");
		
	}
}
