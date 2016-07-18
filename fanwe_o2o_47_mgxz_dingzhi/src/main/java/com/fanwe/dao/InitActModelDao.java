package com.fanwe.dao;

import com.fanwe.model.Init_indexActModel;

public class InitActModelDao
{
	public static boolean insertOrUpdateModel(Init_indexActModel model)
	{
		return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model);
	}

	public static Init_indexActModel queryModel()
	{
		Init_indexActModel model = JsonDbModelDaoX.getInstance().queryJsonDbModel(Init_indexActModel.class);
		return model;
	}

}
