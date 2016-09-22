package com.fanwe.http.listener;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.fanwe.library.utils.SDOtherUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.model.BaseActModel;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public abstract class SDRequestCallBack<E> extends RequestCallBack<String>
{
	public ResponseInfo<String> responseInfo;
	public E actModel;
	public boolean showToast = true;

	public SDRequestCallBack()
	{
		this(true);
	}

	public SDRequestCallBack(boolean showToast)
	{
		this.showToast = showToast;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccessBack(ResponseInfo<String> responseInfo)
	{
		this.responseInfo = responseInfo;

		// 获得泛型class，并将json解析成实体
		Class<E> clazz = null;
		Type type = SDOtherUtil.getType(getClass(), 0);
		if (type instanceof Class)
		{
			clazz = (Class<E>) type;
			actModel = JSON.parseObject(responseInfo.result, clazz);
		}
		// toast
		showToast();
		super.onSuccessBack(responseInfo);
	}

	public void showToast()
	{
		if (showToast && actModel instanceof BaseActModel)
		{
			BaseActModel baseActModel = (BaseActModel) actModel;
			MGToast.showToast(baseActModel.getInfo());
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onSuccess(ResponseInfo<String> responseInfo)
	{
	}
	
	@Override
	public void onFailure(HttpException error, String msg)
	{
		super.onFailure(error, msg);
	}
	
	@Override
	public void onFinish()
	{
		super.onFinish();
	}
}
