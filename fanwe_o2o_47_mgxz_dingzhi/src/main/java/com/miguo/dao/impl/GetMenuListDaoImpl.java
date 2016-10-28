package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.dao.GetMenuListDao;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.MenuBean;
import com.miguo.view.BaseView;
import com.miguo.view.GetMenuListView;

import java.util.TreeMap;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页菜单列表接口实现类
 */
public class GetMenuListDaoImpl extends BaseDaoImpl implements GetMenuListDao{

    public GetMenuListDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public GetMenuListView getListener() {
        return (GetMenuListView)baseView;
    }

    @Override
    public void getMenuList(String terminal_type, String menu_type) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "GetMenuList");
        map.put("menu_type", menu_type);
        map.put("terminal_type", terminal_type);
        OkHttpUtils.getInstance().get("", map, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                MenuBean bean = new Gson().fromJson(responseBody, MenuBean.class);
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        getListener().getMenuListSuccess(bean.getResult().get(0).getBody());
                    }else {
                        getListener().getMenuListError();
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
            }

        });
    }
}
