package com.fanwe.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 狗蛋哥/zlh on 2016/9/23.
 * 首页直播列表下面的团购列表
 */
public class CommandGroupBuyBean {
    String message;
    int statusCode;
    String token;
    List<Result> result;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result implements Serializable{
        int page_total;
        int page;
        List<Body> body;

        public int getPage_total() {
            return page_total;
        }

        public void setPage_total(int page_total) {
            this.page_total = page_total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        /**
         *
         * "tuan_price":"0.01",    // 团购价格
         "buy_count":"23",       // 销量
         "name":"[嵊州]【知味阁】62元A套餐（2~3人）",    // 团购名称
         "icon":"http://oc16x1ls4.bkt.clouddn.com/2016/08/Fh3-oUUzPIiCtcxcc9ViWuVY_idI",   // 小图
         "short_name":"知味阁62元A套餐",   // 团购短名称
         "id":"00088ad1-3b27-45f2-be37-8580692fcd99",    // id
         "brief": "【知味阁】62元A套餐（2~3人）",        // 团购简介
         "origin_price": "5.5",                           // 团购原价
         "tuan_property": "2",                            // 团购属性标签     1.0元抽奖2.免预约3.多套餐4.可订座5.代金券6.过期退7.随时退8.七天退9.免运费10.满立减
         "tuan_property_name": "免预约"                   // 团购属性标签名
         "distance": "45616.116"                        //距离
         */
        public class Body implements Serializable{

            String tuan_price;
            String buy_count;
            String name;
            String icon;
            String short_name;
            String id;
            String brief;
            String origin_price;
            String tuan_property;
            String tuan_property_name;
            String distance;

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getTuan_property() {
                return tuan_property;
            }

            public void setTuan_property(String tuan_property) {
                this.tuan_property = tuan_property;
            }

            public String getTuan_property_name() {
                return tuan_property_name;
            }

            public void setTuan_property_name(String tuan_property_name) {
                this.tuan_property_name = tuan_property_name;
            }

            public String getTuan_price() {
                return tuan_price;
            }

            public void setTuan_price(String tuan_price) {
                this.tuan_price = tuan_price;
            }

            public String getBuy_count() {
                return buy_count;
            }

            public void setBuy_count(String buy_count) {
                this.buy_count = buy_count;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
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

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(String origin_price) {
                this.origin_price = origin_price;
            }
        }

    }

}
