package com.miguo.category.fragment;

import com.fanwe.user.UserConstants;
import com.miguo.entity.BannerTypeModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/25.
 */

public class HomeCategoryUtils {

    /**
     * 把JSON 解析成model
     *
     * @param type_id
     * @return
     */
    public static BannerTypeModel parseTypeJson(String type_id) {
        JSONObject resultJsonObject = null;
        BannerTypeModel model = new BannerTypeModel();
        try {
            resultJsonObject = new JSONObject(type_id);
            if (resultJsonObject.has("id")) {
                model.setId(resultJsonObject.getString("id"));
            }
            if (resultJsonObject.has("url")) {
                model.setUrl(resultJsonObject.getString("url"));
            }
            if (resultJsonObject.has("tag")) {
                model.setTag(resultJsonObject.getString("tag"));
            }
            if (resultJsonObject.has(UserConstants.CATE_ID)) {
                model.setCate_id(resultJsonObject.getString(UserConstants.CATE_ID));
            }
            if (resultJsonObject.has(UserConstants.TID)) {
                model.setTid(resultJsonObject.getString(UserConstants.TID));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }
}
