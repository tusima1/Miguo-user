package com.fanwe.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
/**
{
        "result":[
        {
        "user_num":"11", //限购数量（保留字段）
        "count_down":"0",//倒计时（负数时为开始倒计时，正数时为结束倒计时，0表示过期）
        "name":"测试",//特买名称（保留字段）
        "time_begin":"1470844800000",//开始时间
        "page":"0",//分页（0或者不设定时取前6条）
        "time_end":"1472572800000",//结束时间
        "body":[
        {
        "ent_id":"01d5f59a-17cc-477c-862c-837637804f98",//商家ID
        "special_icon":"http://q.qlogo.cn/qqapp/1104698158/1501BACFDBF4962F755F3F533181822F/100",//特惠封面
        "distance":"9135.605",//距离
        "special_price":"11.00",//特惠价格
        "buy_count":"23",//已销售数量
        "special_dec":"知味阁62元A套餐",//特惠商品说明
        "tuan_id":"00088ad1-3b27-45f2-be37-8580692fcd99",
        "origin_price":"108.00",//原价
        "special_name":"知味阁"//特惠商品标题
        },
        {
        "ent_id":"01d5f59a-17cc-477c-862c-837637804f98",
        "special_icon":"http://q.qlogo.cn/qqapp/1104698158/1501BACFDBF4962F755F3F533181822F/101",
        "distance":"9135.605",
        "special_price":"22.00",
        "buy_count":"74",
        "special_dec":"香溪美容趐趐烫28元",
        "tuan_id":"000c348f-fffa-45f0-8e76-9992f272dc92",
        "origin_price":"118.00",
        "special_name":"香溪美容"
        }
        ],
        "title":"GetSpecialList",
        "all_num":"22",
        "page_size":"10"
        }
        ]
        }
*/
public class SpecialListModel implements Serializable{

    String message;
    int statusCode;
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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result implements Serializable{
        String user_num;
        String count_down;
        String name;
        String time_begin;
        String page;
        String time_end;
        List<Body> body;

        public String getUser_num() {
            return user_num;
        }

        public void setUser_num(String user_num) {
            this.user_num = user_num;
        }

        public String getCount_down() {
            return count_down;
        }

        public void setCount_down(String count_down) {
            this.count_down = count_down;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime_begin() {
            return time_begin;
        }

        public void setTime_begin(String time_begin) {
            this.time_begin = time_begin;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            String ent_id;
            String special_icon;
            String distance;
            String special_price;
            String tuan_price;
            String buy_count;
            String special_dec;
            String tuan_id;
            String origin_price;
            String special_name;
            String count_down;

            public String getCount_down() {
                return count_down;
            }

            public void setCount_down(String count_down) {
                this.count_down = count_down;
            }

            public String getEnt_id() {
                return ent_id;
            }

            public void setEnt_id(String ent_id) {
                this.ent_id = ent_id;
            }

            public String getSpecial_icon() {
                return special_icon;
            }

            public void setSpecial_icon(String special_icon) {
                this.special_icon = special_icon;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getSpecial_price() {
                return special_price;
            }

            public void setSpecial_price(String special_price) {
                this.special_price = special_price;
            }

            public String getBuy_count() {
                return buy_count;
            }

            public void setBuy_count(String buy_count) {
                this.buy_count = buy_count;
            }

            public String getSpecial_dec() {
                return special_dec;
            }

            public void setSpecial_dec(String special_dec) {
                this.special_dec = special_dec;
            }

            public String getTuan_id() {
                return tuan_id;
            }

            public void setTuan_id(String tuan_id) {
                this.tuan_id = tuan_id;
            }

            public String getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(String origin_price) {
                this.origin_price = origin_price;
            }

            public String getSpecial_name() {
                return special_name;
            }

            public void setSpecial_name(String special_name) {
                this.special_name = special_name;
            }

            public String getTuan_price() {
                return tuan_price;
            }

            public void setTuan_price(String tuan_price) {
                this.tuan_price = tuan_price;
            }
        }

    }

}
