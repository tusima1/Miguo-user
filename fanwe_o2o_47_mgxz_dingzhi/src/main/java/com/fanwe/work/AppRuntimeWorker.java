package com.fanwe.work;

import android.text.TextUtils;

import com.fanwe.app.AppHelper;
import com.fanwe.cache.CacheUtil;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.event.EnumEventTag;
import com.fanwe.model.LocalUserModel;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class AppRuntimeWorker {
    private static String currCityId = "";
    private static String currCityName = "";
    private static ModelCityList currCityModel = new ModelCityList();

    /**
     * 获得城市列表
     *
     * @return
     */
    public static List<ModelCityList> getCitylist() {
        return CacheUtil.getInstance().getCityList();
    }

    /**
     * 获得热门城市列表
     *
     * @return
     */
    public static List<ModelCityList> getCitylistHot() {
        return CacheUtil.getInstance().getCityListHot();
    }

    /**
     * 获得当前城市名称
     *
     * @return
     */
    public static String getCity_name() {
        return currCityName;
    }

    /**
     * 获得当前城市id
     *
     * @return
     */
    public static String getCity_id() {
        return currCityId;
    }


    /**
     * 设置当前城市，也会设置当前城市id
     *
     * @param cityName
     * @return
     */
    public static void setCityName(String cityName) {
        ModelCityList bean = getCityByCityName(cityName);
        currCityName = bean.getName();
        currCityId = bean.getId();
        currCityModel = bean;
        if (bean != null) {
            CacheUtil.getInstance().saveCityCurr(bean);
            SDEventManager.post(EnumEventTag.CITY_CHANGE.ordinal());
        }
    }

    /**
     * 设置当前城市，也会设置当前城市id
     *
     * @return
     */
    public static void setCityNameByModel(ModelCityList bean) {
        if (bean == null) {
            return;
        }
        currCityName = bean.getName();
        currCityId = bean.getId();
        currCityModel = bean;
        if (bean != null) {
            CacheUtil.getInstance().saveCityCurr(bean);
            SDEventManager.post(EnumEventTag.CITY_CHANGE.ordinal());
        }
    }


    /**
     * 根据城市名字获得城市id，如果未找到返回-1
     *
     * @return
     */
    public static ModelCityList getCityByCityName(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            List<ModelCityList> listCity = getCitylist();
            if (listCity != null && listCity.size() > 0) {
                ModelCityList cityModel;
                for (int i = 0; i < listCity.size(); i++) {
                    cityModel = listCity.get(i);
                    if (cityModel != null) {
                        if (cityName.equals(cityModel.getName())) {
                            return cityModel;
                        }
                    }
                }
            }
        }
        return (new ModelCityList());
    }


    /**
     * 根据城市名字获得城市id，如果未找到返回-1
     *
     * @return
     */
    public static String getCityIdByCityName(String cityName) {
        return getCityByCityName(cityName).getId();
    }

    /**
     * 根据城市名字获得城市id，如果未找到返回-1
     *
     * @return
     */
    public static ModelCityList getCityCurr() {
        return currCityModel;
    }

    ///////////////////////////////////////////城市相关模块 End///////////////////////////////////////////////////

    public static EnumLoginState getLoginState() {
        EnumLoginState state;

        LocalUserModel user = AppHelper.getLocalUser();
        if (user != null) {

            String mobile = user.getUser_mobile();
            if (user.getIs_tmp() == 1) {
                if (TextUtils.isEmpty(mobile)) {
                    // TODO 绑定手机号
                    state = EnumLoginState.LOGIN_NEED_BIND_PHONE;
                } else {
                    state = EnumLoginState.LOGIN_NEED_VALIDATE;
                }
            } else {
                if (TextUtils.isEmpty(mobile)) {
                    state = EnumLoginState.LOGIN_EMPTY_PHONE;
                } else {
                    state = EnumLoginState.LOGIN_NEED_VALIDATE;
                }
            }
        } else {
            state = EnumLoginState.UN_LOGIN;
        }
        return state;
    }

}
