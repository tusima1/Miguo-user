package com.fanwe.listener;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public abstract class BaseRequestListener<T>
{
	public void onStart()
	{
	}

	public abstract void onSuccess(ResponseInfo<String> responseInfo, T model);

	public void onFailure(HttpException error, String msg)
	{
	}

	public void onFinish()
	{
	}
}
