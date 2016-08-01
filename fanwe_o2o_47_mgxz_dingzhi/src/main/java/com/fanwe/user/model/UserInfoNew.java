package com.fanwe.user.model;

import com.fanwe.base.Body;

/**
 *
 *
 *  "is_locked": "0",
 "app_type": "1",
 "login_port": "0",
 "user_name": "李冰1111111",
 "icon": "用户头像",
 "plitical": "1",
 "insert_time": "1463543598379",
 "remark": "备注",
 "nick": "昵称",
 "offer": "职位",
 "geo_y": "0",
 "geo_x": "0",
 "habit": "用户嗜好",
 "user_type": "2",
 "personality": "个性签名",
 "recommend_id": "bc215691-9c9d-45a0-9894-d3f633ca0025",
 "is_proxy": "0",
 "sex": "1",
 "photo": "头像路径",
 "work_add": "工作地点",
 "version": "1",
 "locked_time": "0",
 "user_id": "ee5672e7-b517-498d-8a91-ab481352fb12",
 "work_tel": "工作电话",
 "login_time": "1463645838425",
 "work_fax": "工作传真",
 "imei": "123456",
 "is_fx": "1",
 "web_site": "个人主页"

 * Created by Administrator on 2016/7/27.
 */
public class UserInfoNew extends Body {


        /**
         * 当前用户是否被锁定
         */
        private String is_locked;


        private String app_type;


        private String login_port;
        /**
         * user_name
         */
        private String user_name;

        /**
         * 用户头像
         */
        private String icon;


        private String plitical;


        private String insert_time;


        private String remark;

        /**
         * 昵称.
         */
        private String nick;

        /**
         * 职位
         */
        private String offer;


        private String geo_y;


        private String geo_x;

        /**
         * 兴趣爱好 。
         */
        private String habit;


        private String user_type;

        /**
         * 个性签名。
         */
        private String personality;


        private String recommend_id;


        private String is_proxy;

        /**
         * 性别
         */
        private String sex;


        private String photo;


        private String work_add;


        private String version;


        private String locked_time;


        private String user_id;


        private String work_tel;


        private String login_time;


        private String work_fax;


        private String imei;


        private String is_fx;

        /**
         * 个人主页地址。
         */
        private String web_site;
    /**
     * MD5后的密码。
     */
    private String pwd;



        public void setIs_locked(String is_locked){

            this.is_locked = is_locked;

        }

        public String getIs_locked(){

            return this.is_locked;

        }

        public void setApp_type(String app_type){

            this.app_type = app_type;

        }

        public String getApp_type(){

            return this.app_type;

        }

        public void setLogin_port(String login_port){

            this.login_port = login_port;

        }

        public String getLogin_port(){

            return this.login_port;

        }

        public void setUser_name(String user_name){

            this.user_name = user_name;

        }

        public String getUser_name(){

            return this.user_name;

        }

        public void setIcon(String icon){

            this.icon = icon;

        }

        public String getIcon(){

            return this.icon;

        }

        public void setPlitical(String plitical){

            this.plitical = plitical;

        }

        public String getPlitical(){

            return this.plitical;

        }

        public void setInsert_time(String insert_time){

            this.insert_time = insert_time;

        }

        public String getInsert_time(){

            return this.insert_time;

        }

        public void setRemark(String remark){

            this.remark = remark;

        }

        public String getRemark(){

            return this.remark;

        }

        public void setNick(String nick){

            this.nick = nick;

        }

        public String getNick(){

            return this.nick;

        }

        public void setOffer(String offer){

            this.offer = offer;

        }

        public String getOffer(){

            return this.offer;

        }

        public void setGeo_y(String geo_y){

            this.geo_y = geo_y;

        }

        public String getGeo_y(){

            return this.geo_y;

        }

        public void setGeo_x(String geo_x){

            this.geo_x = geo_x;

        }

        public String getGeo_x(){

            return this.geo_x;

        }

        public void setHabit(String habit){

            this.habit = habit;

        }

        public String getHabit(){

            return this.habit;

        }

        public void setUser_type(String user_type){

            this.user_type = user_type;

        }

        public String getUser_type(){

            return this.user_type;

        }

        public void setPersonality(String personality){

            this.personality = personality;

        }

        public String getPersonality(){

            return this.personality;

        }

        public void setRecommend_id(String recommend_id){

            this.recommend_id = recommend_id;

        }

        public String getRecommend_id(){

            return this.recommend_id;

        }

        public void setIs_proxy(String is_proxy){

            this.is_proxy = is_proxy;

        }

        public String getIs_proxy(){

            return this.is_proxy;

        }

        public void setSex(String sex){

            this.sex = sex;

        }

        public String getSex(){

            return this.sex;

        }

        public void setPhoto(String photo){

            this.photo = photo;

        }

        public String getPhoto(){

            return this.photo;

        }

        public void setWork_add(String work_add){

            this.work_add = work_add;

        }

        public String getWork_add(){

            return this.work_add;

        }

        public void setVersion(String version){

            this.version = version;

        }

        public String getVersion(){

            return this.version;

        }

        public void setLocked_time(String locked_time){

            this.locked_time = locked_time;

        }

        public String getLocked_time(){

            return this.locked_time;

        }

        public void setUser_id(String user_id){

            this.user_id = user_id;

        }

        public String getUser_id(){

            return this.user_id;

        }

        public void setWork_tel(String work_tel){

            this.work_tel = work_tel;

        }

        public String getWork_tel(){

            return this.work_tel;

        }

        public void setLogin_time(String login_time){

            this.login_time = login_time;

        }

        public String getLogin_time(){

            return this.login_time;

        }

        public void setWork_fax(String work_fax){

            this.work_fax = work_fax;

        }

        public String getWork_fax(){

            return this.work_fax;

        }

        public void setImei(String imei){

            this.imei = imei;

        }

        public String getImei(){

            return this.imei;

        }

        public void setIs_fx(String is_fx){

            this.is_fx = is_fx;

        }

        public String getIs_fx(){

            return this.is_fx;

        }

        public void setWeb_site(String web_site){

            this.web_site = web_site;

        }

        public String getWeb_site(){

            return this.web_site;

        }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
