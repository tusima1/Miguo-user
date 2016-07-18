package com.fanwe.dao;

import java.util.List;

import com.fanwe.app.App;
import com.fanwe.common.DbManagerX;
import com.fanwe.constant.Constant.LoadImageType;
import com.fanwe.model.SettingModel;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

public class SettingModelDao
{

	public static boolean insertOrCreateModel(SettingModel model)
	{
		if (model != null)
		{
			if (hasSettingModel())
			{
				return true;
			} else
			{
				try
				{
					DbManagerX.getDbUtils().save(model);
					return true;
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static SettingModel queryModel()
	{
		try
		{
			List<SettingModel> listModel = DbManagerX.getDbUtils().findAll(SettingModel.class);
			if (listModel != null && listModel.size() == 1)
			{
				return listModel.get(0);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static int getLoadImageType()
	{
		SettingModel model = queryModel();
		if (model != null)
		{
			return model.getLoadImageType();
		} else
		{
			return LoadImageType.ALL;
		}
	}

	public static int canPushMessage()
	{
		SettingModel model = queryModel();
		if (model != null)
		{
			return model.getCanPushMessage();
		} else
		{
			return 1;
		}
	}

	public static boolean hasSettingModel()
	{
		SettingModel model = queryModel();
		if (model != null)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public static boolean updateLoadImageType(int loadImageType)
	{
		boolean result = false;
		SettingModel model = queryModel();
		if (model != null)
		{
			try
			{
				model.setLoadImageType(loadImageType);
				DbManagerX.getDbUtils().update(model, WhereBuilder.b("_id", "=", model.get_id()));
				result = true;
			} catch (Exception e)
			{
				result = false;
			}
		}
		App.getApplication().mRuntimeConfig.updateIsCanLoadImage();
		return result;
	}

	public static boolean updateCanPushMessage(int canPushMessage)
	{
		SettingModel model = queryModel();
		if (model != null)
		{
			try
			{
				model.setCanPushMessage(canPushMessage);
				DbManagerX.getDbUtils().update(model, WhereBuilder.b("_id", "=", model.get_id()));
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

}
