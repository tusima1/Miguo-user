package com.fanwe.work;

import android.text.TextUtils;

import com.fanwe.app.AppHelper;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.dao.InitActModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.model.CitylistModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class AppRuntimeWorker {

    public static Init_indexActModel getInitActModel() {
        return InitActModelDao.queryModel();
    }

    public static String getWx_app_key() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getWx_app_key();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }


    /**
     * 获得城市列表
     *
     * @return
     */
    public static List<CitylistModel> getCitylist() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            List<CitylistModel> content = model.getCitylist();
            return content;
        }
        return null;
    }

    /**
     * 获得热门城市列表
     *
     * @return
     */
    public static List<CitylistModel> getCitylistHot() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            List<CitylistModel> content = model.getHot_city();
            return content;
        }
        return null;
    }

    /**
     * 获得关于内容
     *
     * @return
     */
    public static int getAbout_info() {
        int noticeId = 0;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            noticeId = model.getAbout_info();
        }
        return noticeId;
    }

    /**
     * 获得客服电话
     *
     * @return
     */
    public static String getKf_phone() {
        String phone = null;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            phone = model.getKf_phone();
        }
        return phone;
    }

    /**
     * 获得客服邮箱
     *
     * @return
     */
    public static String getKf_email() {
        String email = null;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            email = model.getKf_email();
        }
        return email;
    }

    /**
     * 获得当前城市
     *
     * @return
     */
    public static String getCity_name() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getCity_name();
            return content;
        }
        return "";
    }

    public static String getCityNameInPy(){
        String name = getCity_name();
        return getCityPyByCityName(name);
    }

    /**
     * 设置当前城市，也会设置当前城市id
     *
     * @param cityName
     * @return
     */
    public static boolean setCity_name(String cityName) {
        String cityId = getCityIdByCityName(cityName);
        Init_indexActModel model = getInitActModel();
        model.setCity_name(cityName);
        model.setCity_id(cityId);
        boolean insertSuccess = InitActModelDao.insertOrUpdateModel(model);
        if (insertSuccess) {
            SDEventManager.post(EnumEventTag.CITY_CHANGE.ordinal());
        }
        return insertSuccess;
    }

    /**
     * 获得当前城市id
     *
     * @return
     */
    public static String getCity_id() {
        String cityId = "";
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            cityId = model.getCity_id();
        }
        return cityId;
    }

    /**
     * 根据城市名字获得城市id，如果未找到返回-1
     *
     * @return
     */
    public static String getCityIdByCityName(String cityName) {
        String cityId = "";
        if (!TextUtils.isEmpty(cityName)) {
            List<CitylistModel> listCity = getCitylist();
            if (listCity != null && listCity.size() > 0) {
                CitylistModel cityModel;
                for (int i = 0; i < listCity.size(); i++) {
                    cityModel = listCity.get(i);
                    if (cityModel != null) {
                        if (cityName.equals(cityModel.getName())) {
                            cityId = cityModel.getId();
                        }
                    }
                }
            }
        }
        return cityId;
    }

    /**
     * 根据城市名字获得城市id，如果未找到返回-1
     *
     * @return
     */
    public static String getCityPyByCityName(String cityName) {
        String cityPy = "";
        if (!TextUtils.isEmpty(cityName)) {
            List<CitylistModel> listCity = getCitylist();
            if (listCity != null && listCity.size() > 0) {
                CitylistModel cityModel;
                for (int i = 0; i < listCity.size(); i++) {
                    cityModel = listCity.get(i);
                    if (cityModel != null) {
                        if (cityName.equals(cityModel.getName())) {
                            cityPy = cityModel.getPy();
                        }
                    }
                }
            }
        }
        return cityPy;
    }
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

    /**
     * 邀请注册返利设置 0关闭 1购物车注册页都显示 2只在购物车页显示3只在注册页显示
     *
     * @return
     */
    public static int getRegister_rebate() {
        int register_rebate = 0;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            register_rebate = model.getRegister_rebate();
        }
        return register_rebate;
    }

    public static int getIs_fx() {
        int is_fx = 0;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            is_fx = model.getIs_fx();
        }
        return is_fx;
    }

    /**
     * 是否插件版外卖
     *
     * @return 0:否，1:是
     */
    public static int getIs_plugin_dc() {
        int is_dc = 0;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            is_dc = model.getIs_dc();
        }
        return is_dc;
    }
}
