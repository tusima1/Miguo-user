package com.miguo.entity;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.fanwe.utils.StringTool;
import com.miguo.live.model.getLiveListNew.ModelRoom;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HiShopDetailBean implements Serializable{

    List<Result> result;
    String message;
    int statusCode;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public class Result implements Serializable{
        private String shop_name;//门店名字
        private String address; //门店地址
        private String trade_day; //营业时间
        private String distance; //距离
        private String has_flow; //商家是否有流量包标识  1 有 0没有
        private String is_collect; //是否点赞(收藏)   1已收藏 0未收藏
        private String collect_number; //收藏人数（点赞） 人气值
        private String ent_id; //商家id
        private String id; //门店id
        private String index_img; //门店首页图
        private String is_endorsement; //是否代言标识  1已代言 0未代言
        private String ref_avg_price; //人均消费
        private String mobile_brief;
        private ShareInfoBean share; //分享信息
        private List<ShopImage> shop_images; //轮播图
        private ShopTags shop_tags;
        private CrowdTags crowd_tags; //门店适合人群id
        private List<Tuan> tuan_list; //团购列表
        private List<ModelRoom> live_list;
        private String geo_x;
        private String geo_y;
        private String tel;
        List<Offline> offline_list;
        /**
         * 是否开通线上买单  1是0否
         */
        int offline;

        public boolean canPayFromOffline(){
            return offline == 1;
        }

        public int getOffline() {
            return offline;
        }

        public void setOffline(int offline) {
            this.offline = offline;
        }

        public Offline getOfflineInfo(){
            return SDCollectionUtil.isEmpty(getOffline_list()) ? null : getOffline_list().get(0);
        }

        public List<Offline> getOffline_list() {
            return offline_list;
        }

        public void setOffline_list(List<Offline> offline_list) {
            this.offline_list = offline_list;
        }

        public boolean isEndorsement(){
            return getIs_endorsement().equals("1");
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getGeo_x() {
            return geo_x;
        }

        public void setGeo_x(String geo_x) {
            this.geo_x = geo_x;
        }

        public String getGeo_y() {
            return geo_y;
        }

        public void setGeo_y(String geo_y) {
            this.geo_y = geo_y;
        }

        public String getDistanceFormat(){
            return SDDistanceUtil.getFormatDistance(DataFormat.toDouble(getDistance()));
        }

        public boolean isCollect(){
            return getIs_collect().equals("1");
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTrade_day() {
            return trade_day;
        }

        public void setTrade_day(String trade_day) {
            this.trade_day = trade_day;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getHas_flow() {
            return has_flow;
        }

        public void setHas_flow(String has_flow) {
            this.has_flow = has_flow;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
        }

        public String getCollect_number() {
            return collect_number;
        }

        public void setCollect_number(String collect_number) {
            this.collect_number = collect_number;
        }

        public String getEnt_id() {
            return ent_id;
        }

        public void setEnt_id(String ent_id) {
            this.ent_id = ent_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIndex_img() {
            return index_img;
        }

        public void setIndex_img(String index_img) {
            this.index_img = index_img;
        }

        public String getIs_endorsement() {
            return is_endorsement;
        }

        public void setIs_endorsement(String is_endorsement) {
            this.is_endorsement = is_endorsement;
        }

        public String getRef_avg_price() {
            return ref_avg_price;
        }

        public void setRef_avg_price(String ref_avg_price) {
            this.ref_avg_price = ref_avg_price;
        }

        public String getMobile_brief() {
            return mobile_brief;
        }

        public void setMobile_brief(String mobile_brief) {
            this.mobile_brief = mobile_brief;
        }

        public ShareInfoBean getShare() {
            return share;
        }

        public void setShare(ShareInfoBean share) {
            this.share = share;
        }

        public List<ShopImage> getShop_images() {
            return shop_images;
        }

        public void setShop_images(List<ShopImage> shop_images) {
            this.shop_images = shop_images;
        }

        public ShopTags getShop_tags() {
            return shop_tags;
        }

        public void setShop_tags(ShopTags shop_tags) {
            this.shop_tags = shop_tags;
        }

        public CrowdTags getCrowd_tags() {
            return crowd_tags;
        }

        public void setCrowd_tags(CrowdTags crowd_tags) {
            this.crowd_tags = crowd_tags;
        }

        public List<Tuan> getTuan_list() {
            return tuan_list;
        }

        public void setTuan_list(List<Tuan> tuan_list) {
            this.tuan_list = tuan_list;
        }

        public List<ModelRoom> getLive_list() {
            return live_list;
        }

        public void setLive_list(List<ModelRoom> live_list) {
            this.live_list = live_list;
        }

        public class Offline implements Serializable{

            /**
             * 原价
             */
            public static final int ORIGINAL = 0;
            /**
             * 折扣
             */
            public static final int DISCOUNT = 1;
            /**
             * 满减
             */
            public static final int DECREASE = 2;

            /**
             * 线上买单类型 0：原价 1：打折 2：满减
             * 1
             */
            int online_pay_type;
            /**
             * 折扣，类型为打折时使用
             * 9.7
             */
            Double discount;
            /**
             * 满减金额限制，类型为满减时使用
             * 100.00
             */
            Double full_amount_limit;
            /**
             * 满减优惠金额，类型为满减时使用
             * 5.00
             */
            Double full_discount;
            /**
             * 最高优惠金额，0不限制，类型为满减时使用
             * 50.00
             */
            Double max_discount_limit;
            /**
             * 可用期间，0：不限制，周一：1，周二:2 可多选，用‘,’分割
             * 1,2,4
             */
            String available_week;
            /**
             * 可用时段开始，0：不限制
             * 0800
             */
            String available_time_start;
            /**
             *  可用时段结束，0：不限制
             *  1130
             */
            String available_time_end;
            /**
             * 不可用期间,默认：0 无限制,关联系统配置表（m_admin_config）的code，多个用','分割
             * qingrenjie,qixi,zhongqiujie
             */
            String unavailable_date;
            /**
             * 买单ID
             * 08e30ea2-1e5b-4208-a150-2c5e51bd1abb
             */
            String id;

            /**
             * 获取可用时间，周一，周二，周三，周四，周五，周六，周日
             * 如果连续就 周x - 周y
             * 不连续就用逗号分隔
             * @return
             */
            public String getAvailableWeek(){
                try{
                    String[] WEEKS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                    String[] weeks = getAvailable_week().split("\\,");
                    String week = "";
                    boolean match = true;
                    int preMatch = 0;
                    for(int i = 0; i < weeks.length; i++){
                        if(i!=0){
                            int currentMatch = Integer.parseInt(weeks[i]) - Integer.parseInt(weeks[i - 1]);
                            match = preMatch == 0 ? true : currentMatch == preMatch ? true : false;
                            preMatch = currentMatch;
                            if(!match){
                                break;
                            }
                        }
                    }
                    if(match){
                        week = WEEKS[0] + "到" + WEEKS[Integer.parseInt(weeks[weeks.length - 1]) - 1];
                        return week;
                    }
                    for(int i = 0; i<weeks.length; i++){
                        week = week + (i == 0 ? WEEKS[Integer.parseInt(weeks[i]) - 1] : "，" + WEEKS[Integer.parseInt(weeks[i]) - 1]);
                    }
                    return week;
                }catch (Exception e){
                    return "";
                }
            }

            public String getAvailableTime(){
                String availableTime;
                try{
                    String start = getAvailable_time_start().substring(0, 2) + ":" + getAvailable_time_start().substring(2,getAvailable_time_start().length());
                    String end = getAvailable_time_end().substring(0, 2) + ":" + getAvailable_time_end().substring(2,getAvailable_time_end().length());
                    availableTime = start + "—" + end;
                }catch (Exception e){
                    availableTime = "00:00—24:00";
                }
                return availableTime;
            }

            public String getDiscountText(){
                return getDiscount() + "折";
            }

            public String getDecreaseText(){
                return "每满" + getFullAmountLimit()  + "元减" + getFullDiscount() + "元" + getMaxDiscount();
            }

            public String getFullAmountLimit(){
                return isIntegerForDouble(getFull_amount_limit()) ? getFull_amount_limit().intValue() + "" : getFull_amount_limit() + "";
            }

            public String getFullDiscount(){
                return isIntegerForDouble(getFull_discount()) ? getFull_discount().intValue() + "" : getFull_discount() + "";
            }

            public String getMaxDiscount(){
                return getMax_discount_limit() == 0 ? "" :  "，最高减" + (isIntegerForDouble(getMax_discount_limit()) ? getMax_discount_limit().intValue() + "" : getMax_discount_limit() + "") + "元";
            }

            public boolean isIntegerForDouble(Double obj){
                return DataFormat.isIntegerForDouble(obj);
            }

            public String getAvailable_time_end() {
                return available_time_end;
            }

            public void setAvailable_time_end(String available_time_end) {
                this.available_time_end = available_time_end;
            }

            public String getAvailable_time_start() {
                return available_time_start;
            }

            public void setAvailable_time_start(String available_time_start) {
                this.available_time_start = available_time_start;
            }

            public String getAvailable_week() {
                return available_week;
            }

            public void setAvailable_week(String available_week) {
                this.available_week = available_week;
            }

            public Double getDiscount() {
                return discount;
            }

            public void setDiscount(Double discount) {
                this.discount = discount;
            }

            public Double getFull_amount_limit() {
                return full_amount_limit;
            }

            public void setFull_amount_limit(Double full_amount_limit) {
                this.full_amount_limit = full_amount_limit;
            }

            public Double getFull_discount() {
                return full_discount;
            }

            public void setFull_discount(Double full_discount) {
                this.full_discount = full_discount;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Double getMax_discount_limit() {
                return max_discount_limit;
            }

            public void setMax_discount_limit(Double max_discount_limit) {
                this.max_discount_limit = max_discount_limit;
            }

            public int getOnline_pay_type() {
                return online_pay_type;
            }

            public void setOnline_pay_type(int online_pay_type) {
                this.online_pay_type = online_pay_type;
            }

            public String getUnavailable_date() {
                return unavailable_date;
            }

            public void setUnavailable_date(String unavailable_date) {
                this.unavailable_date = unavailable_date;
            }
        }

        public class ShopTags implements Serializable{
            String tabset_id;
            String title;
            String[] tags;

            public String getTabset_id() {
                return tabset_id;
            }

            public void setTabset_id(String tabset_id) {
                this.tabset_id = tabset_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String[] getTags() {
                return tags;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }
        }

        public class CrowdTags implements Serializable{
            private String tabset_id;

            private String title;

            private String[] tags ;

            public String getTabset_id() {
                return tabset_id;
            }

            public void setTabset_id(String tabset_id) {
                this.tabset_id = tabset_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String[] getTags() {
                return tags;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }
        }

        public class Tuan implements Serializable{
            private String img;

            private String tuan_introdution;

            private String buy_count;

            private String is_multi_plan;

            private String is_first;

            private String salary;

            private String is_anytime_refund;

            private String is_rsvp_not_requred;

            private String tuan_price;

            private String unit;

            private String is_reservable;

            private String cate_type_name;

            private String name;

            private String short_name;

            private String id;

            private String origin_price;

            private String is_expire_refund;

            private String distance;

            private String area_name;
            /**
             * 格式化后的团购价格和单位 。
             */
            private String tuanPriceFormat;


            private List<TuanTag> tag_list;


            public String getLocation(){
                String area = getArea_name() == null || getArea_name().equals("") ? ".. | " : StringTool.getStringFixed(getArea_name(), 5, "") + " | ";
                String distance = getDistance() == null || getDistance().equals("") ? ".. | " : SDDistanceUtil.getMGDistance(DataFormat.toDouble(getDistance())) + " | ";
                String place = getCate_type_name() == null || getCate_type_name().equals("") ? ".." :  getCate_type_name();
                return area + distance + place;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getArea_name() {
                return area_name;
            }

            public void setArea_name(String area_name) {
                this.area_name = area_name;
            }

            public List<TuanTag> getTag_list() {
                return tag_list;
            }

            public void setTag_list(List<TuanTag> tag_list) {
                this.tag_list = tag_list;
            }

            public class TuanTag implements Serializable{

                String title;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTuan_introdution() {
                return tuan_introdution;
            }

            public void setTuan_introdution(String tuan_introdution) {
                this.tuan_introdution = tuan_introdution;
            }

            public String getBuy_count() {
                return buy_count;
            }

            public void setBuy_count(String buy_count) {
                this.buy_count = buy_count;
            }

            public String getIs_multi_plan() {
                return is_multi_plan;
            }

            public void setIs_multi_plan(String is_multi_plan) {
                this.is_multi_plan = is_multi_plan;
            }

            public String getIs_first() {
                return is_first;
            }

            public void setIs_first(String is_first) {
                this.is_first = is_first;
            }

            public String getSalary() {
                return salary;
            }

            public void setSalary(String salary) {
                this.salary = salary;
            }

            public String getIs_anytime_refund() {
                return is_anytime_refund;
            }

            public void setIs_anytime_refund(String is_anytime_refund) {
                this.is_anytime_refund = is_anytime_refund;
            }

            public String getIs_rsvp_not_requred() {
                return is_rsvp_not_requred;
            }

            public void setIs_rsvp_not_requred(String is_rsvp_not_requred) {
                this.is_rsvp_not_requred = is_rsvp_not_requred;
            }

            public String getTuan_price() {
                return tuan_price;
            }

            public void setTuan_price(String tuan_price) {
                this.tuan_price = tuan_price;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getIs_reservable() {
                return is_reservable;
            }

            public void setIs_reservable(String is_reservable) {
                this.is_reservable = is_reservable;
            }

            public String getCate_type_name() {
                return cate_type_name;
            }

            public void setCate_type_name(String cate_type_name) {
                this.cate_type_name = cate_type_name;
            }



            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(String origin_price) {
                this.origin_price = origin_price;
            }

            public String getIs_expire_refund() {
                return is_expire_refund;
            }

            public void setIs_expire_refund(String is_expire_refund) {
                this.is_expire_refund = is_expire_refund;
            }

            public String getTuanPriceFormat() {
                return tuanPriceFormat;
            }

            public void setTuanPriceFormat(String tuanPriceFormat) {
                this.tuanPriceFormat = tuanPriceFormat;
            }
        }

        /**
         * 轮播图
         */
        public class ShopImage implements Serializable{
            String image_url;

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }
        }

    }

}
