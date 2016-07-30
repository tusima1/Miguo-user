package com.miguo.live.model;

import java.util.List;

/**
 * Created by didik on 2016/7/28.
 */
public class TestModel {

    /**
     * result : [{"body":[{"is_locked":"0","app_type":"1","login_port":"0",
     * "user_name":"李冰1111111","browser_type":"","icon":"用户头像","plitical":"1",
     * "insert_time":"1463543598379","remark":"备注","nick":"昵称","offer":"职位","geo_y":"0",
     * "geo_x":"0","habit":"用户嗜好","user_type":"2","personality":"个性签名",
     * "recommend_id":"bc215691-9c9d-45a0-9894-d3f633ca0025","employer":"","is_proxy":"0",
     * "browser_ver":"","proxy_ip":"","os":"","sex":"1","born":"","photo":"头像路径",
     * "work_add":"工作地点","version":"1","login_ip":"","locked_time":"0",
     * "user_id":"ee5672e7-b517-498d-8a91-ab481352fb12","work_tel":"工作电话",
     * "login_time":"1463645838425","work_fax":"工作传真","imei":"123456","is_fx":"1",
     * "web_site":"个人主页","city_id":""}],"title":"UserInfo"}]
     * message : 登录成功
     * token : b978aa9531f9931519b9bd7c5aa20d6c
     * statusCode : 210
     */

    private String message;
    private String token;
    private String statusCode;
    /**
     * body : [{"is_locked":"0","app_type":"1","login_port":"0","user_name":"李冰1111111",
     * "browser_type":"","icon":"用户头像","plitical":"1","insert_time":"1463543598379",
     * "remark":"备注","nick":"昵称","offer":"职位","geo_y":"0","geo_x":"0","habit":"用户嗜好",
     * "user_type":"2","personality":"个性签名",
     * "recommend_id":"bc215691-9c9d-45a0-9894-d3f633ca0025","employer":"","is_proxy":"0",
     * "browser_ver":"","proxy_ip":"","os":"","sex":"1","born":"","photo":"头像路径",
     * "work_add":"工作地点","version":"1","login_ip":"","locked_time":"0",
     * "user_id":"ee5672e7-b517-498d-8a91-ab481352fb12","work_tel":"工作电话",
     * "login_time":"1463645838425","work_fax":"工作传真","imei":"123456","is_fx":"1",
     * "web_site":"个人主页","city_id":""}]
     * title : UserInfo
     */

    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String title;
        /**
         * is_locked : 0
         * app_type : 1
         * login_port : 0
         * user_name : 李冰1111111
         * browser_type :
         * icon : 用户头像
         * plitical : 1
         * insert_time : 1463543598379
         * remark : 备注
         * nick : 昵称
         * offer : 职位
         * geo_y : 0
         * geo_x : 0
         * habit : 用户嗜好
         * user_type : 2
         * personality : 个性签名
         * recommend_id : bc215691-9c9d-45a0-9894-d3f633ca0025
         * employer :
         * is_proxy : 0
         * browser_ver :
         * proxy_ip :
         * os :
         * sex : 1
         * born :
         * photo : 头像路径
         * work_add : 工作地点
         * version : 1
         * login_ip :
         * locked_time : 0
         * user_id : ee5672e7-b517-498d-8a91-ab481352fb12
         * work_tel : 工作电话
         * login_time : 1463645838425
         * work_fax : 工作传真
         * imei : 123456
         * is_fx : 1
         * web_site : 个人主页
         * city_id :
         */

        private List<BodyBean> body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public static class BodyBean {
            private String is_locked;
            private String app_type;
            private String login_port;
            private String user_name;
            private String browser_type;
            private String icon;
            private String plitical;
            private String insert_time;
            private String remark;
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
            private String os;
            private String sex;
            private String born;
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
            private String web_site;
            private String city_id;

            public String getIs_locked() {
                return is_locked;
            }

            public void setIs_locked(String is_locked) {
                this.is_locked = is_locked;
            }

            public String getApp_type() {
                return app_type;
            }

            public void setApp_type(String app_type) {
                this.app_type = app_type;
            }

            public String getLogin_port() {
                return login_port;
            }

            public void setLogin_port(String login_port) {
                this.login_port = login_port;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getBrowser_type() {
                return browser_type;
            }

            public void setBrowser_type(String browser_type) {
                this.browser_type = browser_type;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getPlitical() {
                return plitical;
            }

            public void setPlitical(String plitical) {
                this.plitical = plitical;
            }

            public String getInsert_time() {
                return insert_time;
            }

            public void setInsert_time(String insert_time) {
                this.insert_time = insert_time;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getOffer() {
                return offer;
            }

            public void setOffer(String offer) {
                this.offer = offer;
            }

            public String getGeo_y() {
                return geo_y;
            }

            public void setGeo_y(String geo_y) {
                this.geo_y = geo_y;
            }

            public String getGeo_x() {
                return geo_x;
            }

            public void setGeo_x(String geo_x) {
                this.geo_x = geo_x;
            }

            public String getHabit() {
                return habit;
            }

            public void setHabit(String habit) {
                this.habit = habit;
            }

            public String getUser_type() {
                return user_type;
            }

            public void setUser_type(String user_type) {
                this.user_type = user_type;
            }

            public String getPersonality() {
                return personality;
            }

            public void setPersonality(String personality) {
                this.personality = personality;
            }

            public String getRecommend_id() {
                return recommend_id;
            }

            public void setRecommend_id(String recommend_id) {
                this.recommend_id = recommend_id;
            }

            public String getEmployer() {
                return employer;
            }

            public void setEmployer(String employer) {
                this.employer = employer;
            }

            public String getIs_proxy() {
                return is_proxy;
            }

            public void setIs_proxy(String is_proxy) {
                this.is_proxy = is_proxy;
            }

            public String getBrowser_ver() {
                return browser_ver;
            }

            public void setBrowser_ver(String browser_ver) {
                this.browser_ver = browser_ver;
            }

            public String getProxy_ip() {
                return proxy_ip;
            }

            public void setProxy_ip(String proxy_ip) {
                this.proxy_ip = proxy_ip;
            }

            public String getOs() {
                return os;
            }

            public void setOs(String os) {
                this.os = os;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getBorn() {
                return born;
            }

            public void setBorn(String born) {
                this.born = born;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getWork_add() {
                return work_add;
            }

            public void setWork_add(String work_add) {
                this.work_add = work_add;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getLogin_ip() {
                return login_ip;
            }

            public void setLogin_ip(String login_ip) {
                this.login_ip = login_ip;
            }

            public String getLocked_time() {
                return locked_time;
            }

            public void setLocked_time(String locked_time) {
                this.locked_time = locked_time;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getWork_tel() {
                return work_tel;
            }

            public void setWork_tel(String work_tel) {
                this.work_tel = work_tel;
            }

            public String getLogin_time() {
                return login_time;
            }

            public void setLogin_time(String login_time) {
                this.login_time = login_time;
            }

            public String getWork_fax() {
                return work_fax;
            }

            public void setWork_fax(String work_fax) {
                this.work_fax = work_fax;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getIs_fx() {
                return is_fx;
            }

            public void setIs_fx(String is_fx) {
                this.is_fx = is_fx;
            }

            public String getWeb_site() {
                return web_site;
            }

            public void setWeb_site(String web_site) {
                this.web_site = web_site;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }
        }
    }
}
