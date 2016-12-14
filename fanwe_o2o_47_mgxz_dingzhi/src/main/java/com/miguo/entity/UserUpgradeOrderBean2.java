package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh on 2016/12/14.
 * 用户升级信息（回调支付信息）
 */
public class UserUpgradeOrderBean2 implements Serializable{

    String message;
    int statusCode;
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

            Config config;
            String class_name;
            OrderInfo order_info;

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public Config getConfig() {
                return config;
            }

            public void setConfig(Config config) {
                this.config = config;
            }

            public OrderInfo getOrder_info() {
                return order_info;
            }

            public void setOrder_info(OrderInfo order_info) {
                this.order_info = order_info;
            }

            public class Config implements Serializable{
                String payment_type;
                String out_trade_no;
                String partner;
                String service;
                String subject;
                String total_fee;
                String sign;
                String body;
                String notify_url;
                String sign_type;
                String seller_id;

                public String getBody() {
                    return body;
                }

                public void setBody(String body) {
                    this.body = body;
                }

                public String getNotify_url() {
                    return notify_url;
                }

                public void setNotify_url(String notify_url) {
                    this.notify_url = notify_url;
                }

                public String getOut_trade_no() {
                    return out_trade_no;
                }

                public void setOut_trade_no(String out_trade_no) {
                    this.out_trade_no = out_trade_no;
                }

                public String getPartner() {
                    return partner;
                }

                public void setPartner(String partner) {
                    this.partner = partner;
                }

                public String getPayment_type() {
                    return payment_type;
                }

                public void setPayment_type(String payment_type) {
                    this.payment_type = payment_type;
                }

                public String getSeller_id() {
                    return seller_id;
                }

                public void setSeller_id(String seller_id) {
                    this.seller_id = seller_id;
                }

                public String getService() {
                    return service;
                }

                public void setService(String service) {
                    this.service = service;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }

                public String getSign_type() {
                    return sign_type;
                }

                public void setSign_type(String sign_type) {
                    this.sign_type = sign_type;
                }

                public String getSubject() {
                    return subject;
                }

                public void setSubject(String subject) {
                    this.subject = subject;
                }

                public String getTotal_fee() {
                    return total_fee;
                }

                public void setTotal_fee(String total_fee) {
                    this.total_fee = total_fee;
                }
            }

            public class OrderInfo implements Serializable{
                /**
                 * >= 3 成功
                 */
                int order_status;
                String sub_name;
                String total_price;
                String payment_id;
                String name;
                String order_id;
                String order_type;
                String order_sn;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public String getOrder_sn() {
                    return order_sn;
                }

                public void setOrder_sn(String order_sn) {
                    this.order_sn = order_sn;
                }

                public int getOrder_status() {
                    return order_status;
                }

                public void setOrder_status(int order_status) {
                    this.order_status = order_status;
                }

                public String getOrder_type() {
                    return order_type;
                }

                public void setOrder_type(String order_type) {
                    this.order_type = order_type;
                }

                public String getPayment_id() {
                    return payment_id;
                }

                public void setPayment_id(String payment_id) {
                    this.payment_id = payment_id;
                }

                public String getSub_name() {
                    return sub_name;
                }

                public void setSub_name(String sub_name) {
                    this.sub_name = sub_name;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }
            }

        }

    }

}
