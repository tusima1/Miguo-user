package com.fanwe.test;

import java.util.List;

import com.fanwe.common.DbManagerX;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class RequestUrlModelDao
{

	private static final int MAX_NUMBER = 20;

	public static void insert(RequestUrlModel model)
	{
		try
		{
			DbManagerX.getDbUtils().save(model);
			deleteOutRangeModel();
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}

	public static List<RequestUrlModel> queryAll()
	{
		List<RequestUrlModel> listModel = null;
		try
		{
			Selector selector = Selector.from(RequestUrlModel.class).orderBy("time", true);
			listModel = DbManagerX.getDbUtils().findAll(selector);
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		return listModel;
	}

	private static void deleteOutRangeModel()
	{
		try
		{
			long count = DbManagerX.getDbUtils().count(RequestUrlModel.class);
			if (count > MAX_NUMBER)
			{
				List<RequestUrlModel> listModel = queryAll();
				for (int i = listModel.size() - 1; i >= MAX_NUMBER; i--)
				{
					DbManagerX.getDbUtils().delete(listModel.get(i));
				}
			}
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}
}
