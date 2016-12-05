package com.fanwe.dao;

import com.fanwe.model.ModelDaoCity;

/**
 * 城市管理
 */
public class CityModelDao {

    public static boolean insertModel(ModelDaoCity model) {
        return JsonDbModelDaoX.getInstance().insertOrUpdateJsonDbModel(model, true);

    }

    public static ModelDaoCity queryModel() {
        return JsonDbModelDaoX.getInstance().queryJsonDbModel(ModelDaoCity.class, true);
    }

    public static void deleteAllModel() {
        JsonDbModelDaoX.getInstance().deleteJsonDbModel(ModelDaoCity.class);
    }

}
