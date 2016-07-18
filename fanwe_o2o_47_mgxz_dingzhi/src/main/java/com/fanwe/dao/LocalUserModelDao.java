package com.fanwe.dao;

import com.fanwe.model.LocalUserModel;

public class LocalUserModelDao
{

	public static boolean insertModel(LocalUserModel model)
	{
		return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model, true);

	}

	public static LocalUserModel queryModel()
	{
		return JsonDbModelDaoX.getInstance().queryJsonDbModel(LocalUserModel.class, true);
	}

	public static void deleteAllModel()
	{
		JsonDbModelDaoX.getInstance().deleteJsonDbModel(LocalUserModel.class);
	}

}
