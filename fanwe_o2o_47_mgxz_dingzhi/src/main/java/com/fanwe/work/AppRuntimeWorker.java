package com.fanwe.work;

import android.content.Intent;
import android.text.TextUtils;

import com.fanwe.AppWebViewActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.GoodsListActivity;
import com.fanwe.HoltelListActivity;
import com.fanwe.NearbyVipActivity;
import com.fanwe.NoticeDetailActivity;
import com.fanwe.NoticeListActivity;
import com.fanwe.ScoresListActivity;
import com.fanwe.StoreListActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.TuanListActivity;
import com.fanwe.YouHuiDetailActivity;
import com.fanwe.YouHuiListActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.constant.Constant.IndexType;
import com.fanwe.dao.InitActModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.ScoresListFragment;
import com.fanwe.fragment.StoreListFragment;
import com.fanwe.fragment.TuanListFragment;
import com.fanwe.fragment.YouHuiListFragment;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.model.AdvsDataModel;
import com.fanwe.model.CitylistModel;
import com.fanwe.model.InitActNewslistModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.QuansModel;
import com.miguo.app.HiShopDetailActivity;
import com.sunday.eventbus.SDEventManager;
import com.fanwe.seller.views.GoodsDetailActivity;

import java.util.List;

public class AppRuntimeWorker {

    public static Init_indexActModel getInitActModel() {
        return InitActModelDao.queryModel();
    }

    /**
     * 1：有配送地区选择项；0：无
     *
     * @return
     */
    public static int getHas_region() {
        int hasRegion = -1;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            hasRegion = model.getHas_region();
        }
        return hasRegion;
    }

    public static String getSina_app_key() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getSina_app_key();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static String getSina_app_secret() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getSina_app_secret();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static String getSina_redirectUrl() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getSina_bind_url();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static String getQq_app_key() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getQq_app_key();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static String getQq_app_secret() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getQq_app_secret();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
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

    public static String getWx_app_secret() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            String content = model.getWx_app_secret();
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    /**
     * 获得商圈list（马尾区，仓山区，台江区等）
     *
     * @return
     */
    public static List<QuansModel> getQuanlist() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            List<QuansModel> content = model.getQuanlist();
            return content;
        }
        return null;
    }

    /**
     * 获得公告列表
     *
     * @return
     */
    public static List<InitActNewslistModel> getNewslist() {
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            List<InitActNewslistModel> content = model.getNewslist();
            return content;
        }
        return null;
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

    public static int getServerRegionVersion() {
        int regionVersion = -1;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            regionVersion = model.getRegion_version();
        }
        return regionVersion;
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

    public static Intent createIntentByType(int type, AdvsDataModel data, boolean createDefaultIntent) {
        Intent intent = null;
        switch (type) {
            case IndexType.URL:
                if (data != null && !TextUtils.isEmpty(data.getUrl())) {
                    intent = new Intent(App.getApplication(), AppWebViewActivity.class);
                    intent.putExtra(AppWebViewActivity.EXTRA_URL, data.getUrl());
                }
                break;
            case IndexType.TUAN_LIST:
                intent = new Intent(App.getApplication(), TuanListActivity.class);
                if (data != null) {
                    intent.putExtra(TuanListFragment.EXTRA_CATE_ID, data.getCate_id());
                    intent.putExtra(TuanListFragment.EXTRA_TID, data.getTid());
                    intent.putExtra(TuanListFragment.EXTRA_QID, data.getQid());
                }
                break;
            case IndexType.GOODS_LIST:
                intent = new Intent(App.getApplication(), GoodsListActivity.class);
                if (data != null) {
                    intent.putExtra(HoltelListActivity.EXTRA_CATE_ID, data.getCate_id());
                    intent.putExtra(HoltelListActivity.EXTRA_BID, data.getBid());
                }
                break;
            case IndexType.SCORE_LIST:
                intent = new Intent(App.getApplication(), ScoresListActivity.class);
                if (data != null) {
                    intent.putExtra(ScoresListFragment.EXTRA_CATE_ID, data.getCate_id());
                    intent.putExtra(ScoresListFragment.EXTRA_BID, data.getBid());
                }
                break;
            case IndexType.EVENT_LIST:

                break;
            case IndexType.YOUHUI_LIST:
                intent = new Intent(App.getApplication(), YouHuiListActivity.class);
                if (data != null) {
                    intent.putExtra(YouHuiListFragment.EXTRA_CATE_ID, data.getCate_id());
                    intent.putExtra(YouHuiListFragment.EXTRA_TID, data.getTid());
                    intent.putExtra(YouHuiListFragment.EXTRA_QID, data.getQid());
                }
                break;
            case IndexType.STORE_LIST:
                intent = new Intent(App.getApplication(), StoreListActivity.class);
                if (data != null) {
                    intent.putExtra(StoreListFragment.EXTRA_CATE_ID, data.getCate_id());
                    intent.putExtra(StoreListFragment.EXTRA_TID, data.getTid());
                    intent.putExtra(StoreListFragment.EXTRA_QID, data.getQid());
                }
                break;
            case IndexType.NOTICE_LIST:
                intent = new Intent(App.getApplication(), NoticeListActivity.class);
                break;
            case IndexType.DEAL_DETAIL:
                if (data != null) {
                    intent = new Intent(App.getApplication(), GoodsDetailActivity.class);
                    intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, data.getData_id());
                }
                break;
            case IndexType.EVENT_DETAIL:
                if (data != null) {
//                    intent = new Intent(App.getApplication(), EventDetailActivity.class);
//                    intent.putExtra(EventDetailActivity.EXTRA_EVENT_ID, data.getData_id());
                }
                break;
            case IndexType.YOUHUI_DETAIL:
                if (data != null) {
                    intent = new Intent(App.getApplication(), YouHuiDetailActivity.class);
                    intent.putExtra(YouHuiDetailActivity.EXTRA_YOUHUI_ID, data.getData_id());
                }
                break;
            case IndexType.STORE_DETAIL:
                if (data != null) {
                    intent = new Intent(App.getApplication(), HiShopDetailActivity.class);
                    intent.putExtra(HiShopDetailActivity.EXTRA_MERCHANT_ID, data.getData_id());
                }
                break;
            case IndexType.NOTICE_DETAIL:
                if (data != null) {
                    intent = new Intent(App.getApplication(), NoticeDetailActivity.class);
                    intent.putExtra(NoticeDetailActivity.EXTRA_NOTICE_ID, data.getData_id());
                }
                break;
            case IndexType.SCAN:
                SDEventManager.post(SDActivityManager.getInstance().getLastActivity().getClass(), EnumEventTag.START_SCAN_QRCODE.ordinal());
                break;
            case IndexType.NEARUSER:
                intent = new Intent(App.getApplication(), NearbyVipActivity.class);
                break;
            case IndexType.DISTRIBUTION_STORE:
                intent = new Intent(App.getApplication(), DistributionStoreWapActivity.class);
                break;
        /*case IndexType.DISTRIBUTION_MANAGER:
            intent = new Intent(App.getApplication(), DistributionManageActivity.class);
			break;*/

            case IndexType.TAKEAWAY_LIST:
                // TODO 跳到外卖主界面，定位到外卖列表
                // intent = new Intent(App.getApplication(), MainActivity_dc.class);
                // intent.putExtra(MainActivity_dc.EXTRA_SELECT_INDEX, 0);
                break;
            case IndexType.RESERVATION_LIST:
                // TODO 跳到外卖主界面，定位到订座列表
                // intent = new Intent(App.getApplication(), MainActivity_dc.class);
                // intent.putExtra(MainActivity_dc.EXTRA_SELECT_INDEX, 1);
                break;
            default:
                if (createDefaultIntent) {
                    intent = new Intent(App.getApplication(), TuanListActivity.class);
                }
                break;
        }
        return intent;
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

    /**
     * 是否显示提现功能
     *
     * @return 0:否，1:是
     */
    public static int getMenu_user_withdraw() {
        int user_menu_withdraw = 0;
        Init_indexActModel model = getInitActModel();
        if (model != null) {
            user_menu_withdraw = model.getMenu_user_withdraw();
        }
        return user_menu_withdraw;
    }

}
