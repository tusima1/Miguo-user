package com.fanwe.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.DeliveryAddressSelectActivty;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Consignee_infoModel;
import com.fanwe.model.Delivery_listModel;
import com.fanwe.model.VoucherModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 留言信息。（参数设置fragment，如留言，手机号，等。。。）
 * 
 * @author js02
 * 
 */
public class OrderDetailParamsFragment extends OrderDetailBaseFragment
{

	@ViewInject(R.id.frag_order_detail_params_ll_all)
	private LinearLayout mLlAll;
	@ViewInject(R.id.frag_order_detail_params_ll_has_leave_message)
	private LinearLayout mLlHasLeaveMessage;

	@ViewInject(R.id.frag_order_detail_params_tv_leave_message)
	private TextView mTvLeaveMessage;

	/** 是否有留言信息 */
	private boolean mHasLeaveMessage = false;

	/** 留言信息 */
	private String content;


	/**
	 * 获取留言信息
	 * 
	 * @return
	 */
	public String getContent()
	{
		return content;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_order_detail_params);
	}

	@Override
	protected void init()
	{
		super.init();
		registeClick();
		bindData();
	}

	protected void bindData()
	{
		if (!toggleFragmentView(mCheckActModel))
		{
			resetParams();
			return;
		}

		setViewsVisibility();

		bindLeaveMessage(content);
	}

	private void resetParams()
	{
		this.content = null;

	}

	@Override
	protected void onRefreshData()
	{
		bindData();
		super.onRefreshData();
	}

	/**
	 * 绑定留言信息
	 * 
	 * @param content
	 */
	protected void bindLeaveMessage(String content)
	{
		if (mHasLeaveMessage)
		{
			SDViewBinder.setTextView(mTvLeaveMessage, content, "");
			this.content = content;
		}
	}


	protected void setViewsVisibility()
	{
		// 设置留言框是否可见
		mHasLeaveMessage = SDViewBinder.setViewsVisibility(mLlHasLeaveMessage, 1);
	}

	private void registeClick()
	{
		mLlHasLeaveMessage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.frag_order_detail_params_ll_has_leave_message:
			clickLeaveMessage();
			break;
		default:
			break;
		}
	}

	/**
	 * 点击留言
	 */
	private void clickLeaveMessage()
	{
		View view = LayoutInflater.from(App.getApplication()).inflate(R.layout.dialog_input_invoice, null);
		final EditText etContent = (EditText) view.findViewById(R.id.dialog_input_invoice_et_content);
		etContent.setText(content);
		new SDDialogCustom().setTextTitle("请输入留言").setCustomView(view).setmListener(new SDDialogCustomListener()
		{

			@Override
			public void onDismiss(SDDialogCustom dialog)
			{
			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				content = etContent.getText().toString();
				mTvLeaveMessage.setText(content);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{
			}
		}).show();
	}




}