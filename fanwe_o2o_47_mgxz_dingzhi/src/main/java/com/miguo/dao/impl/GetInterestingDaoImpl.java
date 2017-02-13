package com.miguo.dao.impl;

import com.fanwe.base.Root;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.HiGetInterestingDao;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;
import com.miguo.view.GetInterestingView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/25.
 */

public class GetInterestingDaoImpl extends BaseDaoImpl implements HiGetInterestingDao {

    public GetInterestingDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getInteresting(String city_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("city_id", city_id);
        params.put("method", CommonConstants.INTERESTING);

        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String,String>>>() {}.getType();
                Gson gson = new Gson();
                Root<HashMap<String,String>> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map = (HashMap<String, String>) validateBody(root);
                    if (map != null) {
                        datas.add(map);
                    }
                    getListener().getInterestingSuccess(datas);
                } else {
                    getListener().getInterestingError();
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    @Override
    public GetInterestingView getListener() {
        return (GetInterestingView)baseView;
    }
}
