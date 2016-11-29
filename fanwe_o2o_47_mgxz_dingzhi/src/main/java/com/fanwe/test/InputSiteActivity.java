package com.fanwe.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.InitAdvsMultiActivity;
import com.fanwe.constant.ServerUrl;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * 域名变更界面
 * 
 * @author Administrator
 * 
 */
public class InputSiteActivity extends BaseActivity
{

	@ViewInject(R.id.tv_current_site)
	private TextView mTv_current_site;

	@ViewInject(R.id.btn_submit)
	private Button mBtnSubmit;

	@ViewInject(R.id.btn_request_url)
	private Button mBtn_request_url;

	@ViewInject(R.id.actv_site)
	private AutoCompleteTextView mActv_site;

	private ArrayAdapter<AvailableUrlModel> mAdapter;
	private String mStrSite;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_input_site);
		init();
	}

	private void init()
	{
		initViewState();
		initAutoCompleteTextView();
		bindData();
		registerClick();
	}
	
	private void initAutoCompleteTextView()
	{
		mActv_site.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mActv_site.showDropDown();
			}
		});
	}

	private void initViewState()
	{
		mTv_current_site.setText("当前地址:" + ServerUrl.getAppServerApiUrl());
	}

	private void bindData()
	{
		List<AvailableUrlModel> listModel = AvailableUrlModelDao.queryAll();
		if (!isEmpty(listModel))
		{
			mAdapter = new ArrayAdapter<AvailableUrlModel>(getApplicationContext(), android.R.layout.simple_list_item_1, listModel);
			mActv_site.setAdapter(mAdapter);
		}
	}

	private void registerClick()
	{
		mBtnSubmit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickSubmit();
			}
		});

		mBtn_request_url.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showRequestUrlDialog();
			}
		});

	}

	public static void showRequestUrlDialog()
	{

		List<RequestUrlModel> listModel = RequestUrlModelDao.queryAll();
		if (listModel == null || listModel.isEmpty())
		{
			MGToast.showToast("未找到请求链接");
			return;
		}

		Activity activity = SDActivityManager.getInstance().getLastActivity();
		if (activity == null)
		{
			MGToast.showToast("获得最后一个activity为空");
			return;
		}

		final SDSimpleTextAdapter<RequestUrlModel> adapter = new SDSimpleTextAdapter<RequestUrlModel>(listModel, activity);
		SDDialogMenu dialog = new SDDialogMenu(activity);
		dialog.setAdapter(adapter);
		dialog.setmListener(new SDDialogMenuListener()
		{

			@Override
			public void onItemClick(View v, int index, SDDialogMenu dialog)
			{
				RequestUrlModel model = adapter.getItem(index);
				String text = model.toString();
				SDOtherUtil.copyText(text);
				MGToast.showToast("已经复制链接");
			}

			@Override
			public void onDismiss(SDDialogMenu dialog)
			{

			}

			@Override
			public void onCancelClick(View v, SDDialogMenu dialog)
			{

			}
		});
		dialog.show();
	}

	protected void clickSubmit()
	{
		if (validateParams())
		{
			validateSite(mStrSite);
		}
	}


	

	/**
	 * 验证域名是否有效
	 */
	private void validateSite(final String url){

		RequestModel model = new RequestModel();
		model.putCtl("init");

		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<BaseActModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("正在验证地址是否可用");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				// TODO 保存地址
				AvailableUrlModel model = new AvailableUrlModel();
				model.setUrl(url);
				AvailableUrlModelDao.insertOrUpdate(model);
				bindData();


				SDActivityManager.getInstance().finishAllActivityExcept(InputSiteActivity.class);
				startInitActivity();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
				MGToast.showToast("地址不可用");
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		});
	}

	protected void startInitActivity()
	{
		Intent intent = new Intent(getApplicationContext(), InitAdvsMultiActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean validateParams()
	{
		mStrSite = mActv_site.getText().toString();
		if (isEmpty(mStrSite))
		{
			MGToast.showToast("地址不能为空");
			return false;
		}
		return true;
	}

}
