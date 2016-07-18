package com.fanwe.dao;

import java.util.List;

import android.text.TextUtils;

import com.fanwe.common.DbManagerX;
import com.fanwe.model.Region_confModel;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class Region_confModelDao
{

	private static Region_confModelDao mInstance = null;

	private Region_confModelDao()
	{
	}

	public static Region_confModelDao getInstance()
	{
		if (mInstance == null)
		{
			syncInit();
		}
		return mInstance;
	}

	private static synchronized void syncInit()
	{
		if (mInstance == null)
		{
			mInstance = new Region_confModelDao();
		}
	}

	public boolean deleteAllData()
	{
		try
		{
			DbManagerX.getDbUtils().deleteAll(Region_confModel.class);
		} catch (DbException e)
		{
			return false;
		}
		return true;
	}

	public boolean deleteOldAndSaveNew(List<Region_confModel> listModel)
	{
		if (listModel != null && listModel.size() > 0)
		{
			if (deleteAllData())
			{
				try
				{
					DbManagerX.getDbUtils().saveAll(listModel);
					return true;
				} catch (DbException e)
				{
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public List<Region_confModel> getCountryList()
	{
		return getListByPid(0);
	}

	public List<Region_confModel> getListByPid(int pid)
	{
		List<Region_confModel> listModel = null;
		try
		{
			listModel = DbManagerX.getDbUtils().findAll(Selector.from(Region_confModel.class).where("pid", "=", pid));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return listModel;
	}

	public List<Region_confModel> getListById(String id)
	{
		if (!TextUtils.isEmpty(id))
		{
			try
			{
				return DbManagerX.getDbUtils().findAll(Selector.from(Region_confModel.class).where("id", "=", id));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Region_confModel> getListByParentModel(Region_confModel parent)
	{
		return getListByPid(parent.getId());
	}

}
