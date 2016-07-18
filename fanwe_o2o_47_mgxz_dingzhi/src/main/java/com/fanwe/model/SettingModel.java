package com.fanwe.model;

import com.fanwe.constant.Constant.LoadImageType;

public class SettingModel
{
	private int _id;

	/** 1:任何网络下都加载图片 0:移动网络下不加载图片 */
	private int loadImageType = LoadImageType.ALL;
	/** 是否打开推送 */
	private int canPushMessage = 1;

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

	public int getLoadImageType()
	{
		return loadImageType;
	}

	public void setLoadImageType(int loadImageType)
	{
		this.loadImageType = loadImageType;
	}

	public int getCanPushMessage()
	{
		return canPushMessage;
	}

	public void setCanPushMessage(int canPushMessage)
	{
		this.canPushMessage = canPushMessage;
	}

}
