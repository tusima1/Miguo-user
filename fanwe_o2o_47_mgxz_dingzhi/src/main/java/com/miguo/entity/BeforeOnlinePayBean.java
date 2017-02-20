package com.miguo.entity;

import com.fanwe.utils.DataFormat;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/15.
 */

public class BeforeOnlinePayBean {
    int statusCode;
    String message;
    List<Result> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public class Result implements Serializable{
        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            /**
             * 门店名称
             * 常品打面馆
             */
            String shop_name;
            /**
             * 是否开通
             * 1是0否
             */
            int offline;
            /**
             * 买单优惠是否有效
             * 1有效0无效
             */
            int can_use_online_pay;
            /**
             * 08e30ea2-1e5b-4208-a150-2c5e51bd1abb
             */
            String online_pay_id;
            /**
             * 线上买单类型 0：原价 1：打折 2：满减
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
             * 50
             */
            Double max_discount_limit;
            /**
             * 可用期间，0：不限制，周一：1，周二:2 可多选，用‘,’分割
             */
            String available_week;
            /**
             * 可用时段开始，0：不限制
             * 0800 ->08:00
             */
            String available_time_start;
            /**
             * 可用时段结束，0：不限制
             */
            String available_time_end;
            /**
             * 不可用期间,默认：0 无限制,关联系统配置表（m_admin_config）的code，多个用','分割
             * qingrenjie,qixi,zhongqiujie
             */
            String unavailable_date;

            public boolean canPayFromOffline(){
                return offline == 1;
            }

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

            public boolean isIntegerForDouble(Double obj){
                return DataFormat.isIntegerForDouble(obj);
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

            public int getCan_use_online_pay() {
                return can_use_online_pay;
            }

            public void setCan_use_online_pay(int can_use_online_pay) {
                this.can_use_online_pay = can_use_online_pay;
            }

            public Double getDiscount() {
                return discount;
            }

            public Double getRealDiscount(){
                return getDiscount() / 10;
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

            public Double getMax_discount_limit() {
                return max_discount_limit;
            }

            public void setMax_discount_limit(Double max_discount_limit) {
                this.max_discount_limit = max_discount_limit;
            }

            public int getOffline() {
                return offline;
            }

            public void setOffline(int offline) {
                this.offline = offline;
            }

            public String getOnline_pay_id() {
                return online_pay_id;
            }

            public void setOnline_pay_id(String online_pay_id) {
                this.online_pay_id = online_pay_id;
            }

            public int getOnline_pay_type() {
                return online_pay_type;
            }

            public void setOnline_pay_type(int online_pay_type) {
                this.online_pay_type = online_pay_type;
            }

            public String getShop_name() {
                return shop_name;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public String getUnavailable_date() {
                return unavailable_date;
            }

            public void setUnavailable_date(String unavailable_date) {
                this.unavailable_date = unavailable_date;
            }
        }

    }

}
