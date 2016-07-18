package com.fanwe.test;

import com.fanwe.common.DbManagerX;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class AvailableUrlModelDao
{

	public static void insertOrUpdate(AvailableUrlModel model)
	{
		Selector selector = Selector.from(AvailableUrlModel.class).where("url", "=", model.getUrl());
		try
		{
			AvailableUrlModel dbModel = DbManagerX.getDbUtils().findFirst(selector);
			if (dbModel != null)
			{
				dbModel.setTime(model.getTime());
				/**
				 * 将参数 null 改为 "",避免了Android studio 报错
				 */
				DbManagerX.getDbUtils().update(dbModel, "");
			} else
			{
				DbManagerX.getDbUtils().save(model);
			}
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}

	public static List<AvailableUrlModel> queryAll()
	{
		List<AvailableUrlModel> listModel = null;
		try
		{
			Selector selector = Selector.from(AvailableUrlModel.class).orderBy("time", true);
			listModel = DbManagerX.getDbUtils().findAll(selector);
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		return listModel;
	}
}
