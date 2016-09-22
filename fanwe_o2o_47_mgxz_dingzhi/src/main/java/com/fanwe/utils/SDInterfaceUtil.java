package com.fanwe.utils;

import android.text.TextUtils;

import com.miguo.live.views.customviews.MGToast;
import com.fanwe.model.BaseActModel;

public class SDInterfaceUtil
{

	public static boolean isActModelNull(BaseActModel actModel)
	{
		if (actModel != null)
		{
			if (!TextUtils.isEmpty(actModel.getInfo()))
			{
				MGToast.showToast(actModel.getInfo());
			}
			return false;
		} else
		{
			MGToast.showToast("接口访问失败或者json解析出错!");
			return true;
		}
	}

}
