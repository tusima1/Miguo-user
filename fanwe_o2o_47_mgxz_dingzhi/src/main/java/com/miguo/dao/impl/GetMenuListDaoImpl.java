package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.dao.GetMenuListDao;
import com.miguo.entity.MenuBean;
import com.miguo.view.BaseView;
import com.miguo.view.GetMenuListView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

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
        map.put("city_id", AppRuntimeWorker.getCity_id());
        map.put("menu_type", menu_type);
        map.put("terminal_type", terminal_type);
        OkHttpUtils.getInstance().get("", map, new MgCallback(MenuBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                MenuBean bean = (MenuBean)responseBody;
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        getListener().getMenuListSuccess(bean.getResult().get(0).getBody());
                    }else {
                        getListener().getMenuListError();
                    }
                }
            }
            @Override
            public void onErrorResponseOnMainThread(String message, String errorCode) {
                getListener().getMenuListError();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                 getListener().getMenuListError();
            }

        });
    }
}
