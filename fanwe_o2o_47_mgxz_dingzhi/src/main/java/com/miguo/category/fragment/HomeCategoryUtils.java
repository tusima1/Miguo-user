package com.miguo.category.fragment;

import com.fanwe.user.UserConstants;
import com.miguo.dao.BannerTypeModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/25.
 */

public class HomeCategoryUtils {

    /**
     * 把JSON 解析成model
     * @param type_id
     * @return
     */
    public static BannerTypeModel parseTypeJson(String type_id){
        JSONObject resultJsonObject = null;
        BannerTypeModel model = new BannerTypeModel();
        try {
            resultJsonObject = new JSONObject(type_id);

            model.setId(resultJsonObject.getString("id"));
            model.setUrl(resultJsonObject.getString("url"));
            model.setTag(resultJsonObject.getString("tag"));
            model.setCate_id(resultJsonObject.getString(UserConstants.CATE_ID));
            model.setTid(resultJsonObject.getString(UserConstants.TID));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }
}
