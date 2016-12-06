package com.fanwe.user.model;

import com.fanwe.base.Body;

/**
 * "is_locked": "0",
 * "app_type": "1",
 * "login_port": "0",
 * "user_name": "李冰1111111",
 * "icon": "用户头像",
 * "plitical": "1",
 * "insert_time": "1463543598379",
 * "remark": "备注",
 * "nick": "昵称",
 * "offer": "职位",
 * "geo_y": "0",
 * "geo_x": "0",
 * "habit": "用户嗜好",
 * "user_type": "2",
 * "personality": "个性签名",
 * "recommend_id": "bc215691-9c9d-45a0-9894-d3f633ca0025",
 * "is_proxy": "0",
 * "sex": "1",
 * "photo": "头像路径",
 * "work_add": "工作地点",
 * "version": "1",
 * "locked_time": "0",
 * "user_id": "ee5672e7-b517-498d-8a91-ab481352fb12",
 * "work_tel": "工作电话",
 * "login_time": "1463645838425",
 * "work_fax": "工作传真",
 * "imei": "123456",
 * "is_fx": "1",
 * "web_site": "个人主页"
 * <p/>
 * Created by Administrator on 2016/7/27.
 */
public class UserInfoNew extends Body {


    private String is_locked;


    private String app_type;


    private String login_port;


    private String user_name;


    private String browser_type;


    private String icon;


    private String plitical;


    private String insert_time;


    private String remark;


    private String is_host;


    private String nick;


    private String offer;


    private String geo_y;


    private String geo_x;


    private String habit;


    private String user_type;


    private String personality;


    private String recommend_id;


    private String employer;


    private String is_proxy;


    private String browser_ver;


    private String proxy_ip;


    private String salt;


    private String os;


    private String sex;


    private String born;


    private String mobile;


    private String photo;


    private String work_add;


    private String version;


    private String login_ip;


    private String locked_time;


    private String user_id;


    private String work_tel;


    private String login_time;


    private String work_fax;


    private String imei;


    private String is_fx;


    private String pwd;


    private String web_site;


    private String city_id;

    private String fx_level;

    /**
     * 腾讯的签名
     */
    private String useSign;

    /**
     * 用户当前TOKEN.
     */
    public String token;


    public UserInfoNew() {
        this.app_type = "";
        this.born = "";
        this.browser_type = "";
        this.browser_ver = "";
        this.city_id = "";
        this.employer = "";
        this.fx_level = "";
        this.geo_x = "";
        this.geo_y = "";
        this.habit = "";
        this.icon = "";
        this.imei = "";
        this.insert_time = "";
        this.is_fx = "";
        this.is_host = "";
        this.is_locked = "";
        this.is_proxy = "";
        this.locked_time = "";
        this.login_ip = "";
        this.login_port = "";
        this.login_time = "";
        this.mobile = "";
        this.nick = "";
        this.offer = "";
        this.os = "";
        this.personality = "";
        this.photo = "";
        this.plitical = "";
        this.proxy_ip = "";
        this.pwd = "";
        this.recommend_id = "";
        this.remark = "";
        this.salt = "";
        this.sex = "";
        this.token = "";
        this.user_id = "";
        this.user_name = "";
        this.user_type = "";
        this.useSign = "";
        this.version = "";
        this.web_site = "";
        this.work_add = "";
        this.work_fax = "";
        this.work_tel = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUseSign() {
        return useSign;
    }

    public void setUseSign(String useSign) {
        this.useSign = useSign;
    }

    public String getFx_level() {
        return fx_level;
    }

    public void setFx_level(String fx_level) {
        this.fx_level = fx_level;
    }

    public void setIs_locked(String is_locked) {

        this.is_locked = is_locked;

    }

    public String getIs_locked() {

        return this.is_locked;

    }

    public void setApp_type(String app_type) {

        this.app_type = app_type;

    }

    public String getApp_type() {

        return this.app_type;

    }

    public void setLogin_port(String login_port) {

        this.login_port = login_port;

    }

    public String getLogin_port() {

        return this.login_port;

    }

    public void setUser_name(String user_name) {

        this.user_name = user_name;

    }

    public String getUser_name() {

        return this.user_name;

    }

    public void setBrowser_type(String browser_type) {

        this.browser_type = browser_type;

    }

    public String getBrowser_type() {

        return this.browser_type;

    }

    public void setIcon(String icon) {

        this.icon = icon;

    }

    public String getIcon() {

        return this.icon;

    }

    public void setPlitical(String plitical) {

        this.plitical = plitical;

    }

    public String getPlitical() {

        return this.plitical;

    }

    public void setInsert_time(String insert_time) {

        this.insert_time = insert_time;

    }

    public String getInsert_time() {

        return this.insert_time;

    }

    public void setRemark(String remark) {

        this.remark = remark;

    }

    public String getRemark() {

        return this.remark;

    }

    public void setIs_host(String is_host) {

        this.is_host = is_host;

    }

    public String getIs_host() {

        return this.is_host;

    }

    public void setNick(String nick) {

        this.nick = nick;

    }

    public String getNick() {
        return (this.nick == null || this.nick.equals("")) ?  this.mobile : this.nick;

    }

    public void setOffer(String offer) {

        this.offer = offer;

    }

    public String getOffer() {

        return this.offer;

    }

    public void setGeo_y(String geo_y) {

        this.geo_y = geo_y;

    }

    public String getGeo_y() {

        return this.geo_y;

    }

    public void setGeo_x(String geo_x) {

        this.geo_x = geo_x;

    }

    public String getGeo_x() {

        return this.geo_x;

    }

    public void setHabit(String habit) {

        this.habit = habit;

    }

    public String getHabit() {

        return this.habit;

    }

    public void setUser_type(String user_type) {

        this.user_type = user_type;

    }

    public String getUser_type() {

        return this.user_type;

    }

    public void setPersonality(String personality) {

        this.personality = personality;

    }

    public String getPersonality() {

        return this.personality;

    }

    public void setRecommend_id(String recommend_id) {

        this.recommend_id = recommend_id;

    }

    public String getRecommend_id() {

        return this.recommend_id;

    }

    public void setEmployer(String employer) {

        this.employer = employer;

    }

    public String getEmployer() {

        return this.employer;

    }

    public void setIs_proxy(String is_proxy) {

        this.is_proxy = is_proxy;

    }

    public String getIs_proxy() {

        return this.is_proxy;

    }

    public void setBrowser_ver(String browser_ver) {

        this.browser_ver = browser_ver;

    }

    public String getBrowser_ver() {

        return this.browser_ver;

    }

    public void setProxy_ip(String proxy_ip) {

        this.proxy_ip = proxy_ip;

    }

    public String getProxy_ip() {

        return this.proxy_ip;

    }

    public void setSalt(String salt) {

        this.salt = salt;

    }

    public String getSalt() {

        return this.salt;

    }

    public void setOs(String os) {

        this.os = os;

    }

    public String getOs() {

        return this.os;

    }

    public void setSex(String sex) {

        this.sex = sex;

    }

    public String getSex() {

        return this.sex;

    }

    public void setBorn(String born) {

        this.born = born;

    }

    public String getBorn() {

        return this.born;

    }

    public void setMobile(String mobile) {

        this.mobile = mobile;

    }

    public String getMobile() {

        return this.mobile;

    }

    public void setPhoto(String photo) {

        this.photo = photo;

    }

    public String getPhoto() {

        return this.photo;

    }

    public void setWork_add(String work_add) {

        this.work_add = work_add;

    }

    public String getWork_add() {

        return this.work_add;

    }

    public void setVersion(String version) {

        this.version = version;

    }

    public String getVersion() {

        return this.version;

    }

    public void setLogin_ip(String login_ip) {

        this.login_ip = login_ip;

    }

    public String getLogin_ip() {

        return this.login_ip;

    }

    public void setLocked_time(String locked_time) {

        this.locked_time = locked_time;

    }

    public String getLocked_time() {

        return this.locked_time;

    }

    public void setUser_id(String user_id) {

        this.user_id = user_id;

    }

    public String getUser_id() {

        return this.user_id;

    }

    public void setWork_tel(String work_tel) {

        this.work_tel = work_tel;

    }

    public String getWork_tel() {

        return this.work_tel;

    }

    public void setLogin_time(String login_time) {

        this.login_time = login_time;

    }

    public String getLogin_time() {

        return this.login_time;

    }

    public void setWork_fax(String work_fax) {

        this.work_fax = work_fax;

    }

    public String getWork_fax() {

        return this.work_fax;

    }

    public void setImei(String imei) {

        this.imei = imei;

    }

    public String getImei() {

        return this.imei;

    }

    public void setIs_fx(String is_fx) {

        this.is_fx = is_fx;

    }

    public String getIs_fx() {

        return this.is_fx;

    }

    public void setPwd(String pwd) {

        this.pwd = pwd;

    }

    public String getPwd() {

        return this.pwd;

    }

    public void setWeb_site(String web_site) {

        this.web_site = web_site;

    }

    public String getWeb_site() {

        return this.web_site;

    }

    public void setCity_id(String city_id) {

        this.city_id = city_id;

    }

    public String getCity_id() {

        return this.city_id;

    }
}
