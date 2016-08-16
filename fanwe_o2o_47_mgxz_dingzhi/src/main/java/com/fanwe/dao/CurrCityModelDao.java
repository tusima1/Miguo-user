package com.fanwe.dao;

import com.fanwe.model.CitylistModel;

/**
 * 当前选中的城市
 */
public class CurrCityModelDao {

    public static boolean insertModel(CitylistModel model) {
        return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model, true);

    }

    public static CitylistModel queryModel() {
        return JsonDbModelDaoX.getInstance().queryJsonDbModel(CitylistModel.class, true);
    }

    public static void deleteAllModel() {
        JsonDbModelDaoX.getInstance().deleteJsonDbModel(CitylistModel.class);
    }

}
