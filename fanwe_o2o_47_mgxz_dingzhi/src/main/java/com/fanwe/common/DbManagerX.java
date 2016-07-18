package com.fanwe.common;

import com.fanwe.app.App;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;

public class DbManagerX
{

	private static final String DB_DIR = null;
	private static final String DB_NAME = "Fanwe.db";
	private static final int DB_VERSION = 2;

	private static DbUtils mDbUtils = null;

	private DbManagerX()
	{
	}

	public static DbUtils getDbUtils()
	{
		if (mDbUtils == null)
		{
			mDbUtils = DbUtils.create(App.getApplication(), DB_DIR, DB_NAME, DB_VERSION, new FwDbUpgradeListener());
			init();
		}
		return mDbUtils;
	}

	private static void init()
	{
		mDbUtils.configAllowTransaction(true);
	}

	static class FwDbUpgradeListener implements DbUpgradeListener
	{

		@Override
		public void onUpgrade(DbUtils db, int oldVersion, int newVersion)
		{
			if (newVersion == 2)
			{
				try
				{
					db.dropDb();
				} catch (DbException e)
				{
					e.printStackTrace();
				}
			}

		}

	}

}
