package com.fanwe.dao;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.common.DbManagerX;
import com.fanwe.library.utils.AESUtil;
import com.fanwe.model.JsonDbModel;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.utils.JsonUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

public class JsonDbModelDaoX
{

	private static JsonDbModelDaoX mInstance = null;

	private JsonDbModelDaoX()
	{
	}

	public static JsonDbModelDaoX getInstance()
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
			mInstance = new JsonDbModelDaoX();
		}
	}

	public <T> boolean insertOrUpdateJsonDbListModel(List<ShoppingCartInfo> object, Class<T> clazz, boolean encrypt){
		deleteJsonDbListModel(clazz);
		if (object != null) {
			for (int i = 0; i < object.size(); i++) {
				ShoppingCartInfo shoppingCartInfo = object.get(i);
				try {
					JsonDbModel jsonDbModel = new JsonDbModel();
					jsonDbModel.setKey(clazz.getName()+"_"+shoppingCartInfo.getId());
					String json = JsonUtil.object2Json(shoppingCartInfo);
					if (encrypt) // 需要加密
					{
						json = AESUtil.encrypt(json);
					}
					jsonDbModel.setValue(json);
					DbManagerX.getDbUtils().save(jsonDbModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	private <T> boolean insertJsonDbModel(T model, boolean encrypt)
	{
		if (model != null)
		{
			try
			{
				JsonDbModel jsonDbModel = new JsonDbModel();
				jsonDbModel.setKey(model.getClass().getName());
				String json = JsonUtil.object2Json(model);
				if (encrypt) // 需要加密
				{
					json = AESUtil.encrypt(json);
				}
				jsonDbModel.setValue(json);
				DbManagerX.getDbUtils().save(jsonDbModel);
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	public <T> boolean insertOrUpdateJsonDbModel(T model, boolean encrypt)
	{
		if (model != null)
		{
			deleteJsonDbModel(model.getClass());
			return insertJsonDbModel(model, encrypt);
		} else
		{
			return false;
		}
	}

	public <T> boolean insertOrUpdateJsonDbModel(T model)
	{
		return insertOrUpdateJsonDbModel(model, false);
	}


	public <T> boolean deleteJsonDbModel(Class<T> clazz)
	{
	 return	 deleteJsonDbModel(clazz,clazz.getName());
	}
	public <T> boolean deleteJsonDbModel(Class<T> clazz,String className)
	{
		if (clazz != null)
		{
			try
			{
				DbManagerX.getDbUtils().delete(JsonDbModel.class, WhereBuilder.b("key", "=", className));
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	public  <T> boolean deleteJsonDbListModel(Class<T> clazz){
		try
		{
			DbManagerX.getDbUtils().delete(JsonDbModel.class, WhereBuilder.b("key","like",clazz.getName()+"%"));
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public List queryJsonDbListModel(Class clazz, boolean decrypt)
	{
		List list = new ArrayList();
		if (clazz != null)
		{
			try
			{
				List<JsonDbModel> listJsonDbModel = DbManagerX.getDbUtils().findAll(
						Selector.from(JsonDbModel.class).where("key", "like", clazz.getName()+"%"));
				if (listJsonDbModel != null ) {
					for (int i = 0; i < listJsonDbModel.size(); i++) {
						JsonDbModel jsonDbModel = listJsonDbModel.get(i);
						String value = jsonDbModel.getValue();
						if (value != null) {
							if (decrypt) // 需要解密
							{
								value = AESUtil.decrypt(value);
							}
							list.add(JsonUtil.json2Object(value, clazz));
						}
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return list;
	}

	public <T> T queryJsonDbModel(Class<T> clazz, boolean decrypt)
	{
		if (clazz != null)
		{
			try
			{
				List<JsonDbModel> listJsonDbModel = DbManagerX.getDbUtils().findAll(
						Selector.from(JsonDbModel.class).where("key", "=", clazz.getName()));
				if (listJsonDbModel != null && listJsonDbModel.size() == 1)
				{
					JsonDbModel jsonDbModel = listJsonDbModel.get(0);
					String value = jsonDbModel.getValue();
					if (value != null)
					{
						if (decrypt) // 需要解密
						{
							value = AESUtil.decrypt(value);
						}
						return JsonUtil.json2Object(value, clazz);
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public <T> T queryJsonDbModel(Class<T> clazz)
	{
		return queryJsonDbModel(clazz, false);
	}



}
