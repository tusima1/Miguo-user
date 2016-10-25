package com.miguo.entity;

import com.baidu.mapapi.utils.DistanceUtil;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.fanwe.utils.StringTool;

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
        private Share share; //分享信息
        private List<ShopImage> shop_images; //轮播图
        private ShopTags shop_tags;
        private CrowdTags crowd_tags; //门店适合人群id
        private List<Tuan> tuan_list; //团购列表
        private List<Live> live_list;
        private String geo_x;
        private String geo_y;
        private String tel;


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

        public Share getShare() {
            return share;
        }

        public void setShare(Share share) {
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

        public List<Live> getLive_list() {
            return live_list;
        }

        public void setLive_list(List<Live> live_list) {
            this.live_list = live_list;
        }

        public class Share implements Serializable{
            private String clickurl;

            private String summary;

            private String imageurl;

            private String title;

            public String getClickurl() {
                return clickurl;
            }

            public void setClickurl(String clickurl) {
                this.clickurl = clickurl;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getImageurl() {
                return imageurl;
            }

            public void setImageurl(String imageurl) {
                this.imageurl = imageurl;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            private List<TuanTag> tag_list;


            public String getLocation(){
                String area = getArea_name() == null || getArea_name().equals("") ? "" : StringTool.getStringFixed(getArea_name(), 5, "") + " | ";
                String disntance = SDDistanceUtil.getFormatDistance(DataFormat.toDouble(getDistance())) + "";
                String place = getCate_type_name() == null || getCate_type_name().equals("") ? "" : " | " + getCate_type_name();
                return area + disntance + place;
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
        }

        public class Live implements Serializable{
            private String create_time;

            private String live_type;

            private String title;

            private String file_size;

            private String cover;

            private String duration;

            private String vid;

            private String start_time;

            private String file_id;

            private String chat_room_id;

            private Host host;

            private String playset;

            private String id;

            private String av_room_id;

            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public boolean isLive(){
                return getLive_type().equals("1");
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getLive_type() {
                return live_type;
            }

            public void setLive_type(String live_type) {
                this.live_type = live_type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFile_size() {
                return file_size;
            }

            public void setFile_size(String file_size) {
                this.file_size = file_size;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getFile_id() {
                return file_id;
            }

            public void setFile_id(String file_id) {
                this.file_id = file_id;
            }

            public String getChat_room_id() {
                return chat_room_id;
            }

            public void setChat_room_id(String chat_room_id) {
                this.chat_room_id = chat_room_id;
            }

            public Host getHost() {
                return host;
            }

            public void setHost(Host host) {
                this.host = host;
            }

            public String getPlayset() {
                return playset;
            }

            public void setPlayset(String playset) {
                this.playset = playset;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAv_room_id() {
                return av_room_id;
            }

            public void setAv_room_id(String av_room_id) {
                this.av_room_id = av_room_id;
            }
        }

        public class Host implements Serializable{
            private String uid;

            private String host_user_id;

            private String nickname;

            private String avatar;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getHost_user_id() {
                return host_user_id;
            }

            public void setHost_user_id(String host_user_id) {
                this.host_user_id = host_user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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
