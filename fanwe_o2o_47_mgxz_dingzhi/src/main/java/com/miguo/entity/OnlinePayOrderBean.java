package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */
public class OnlinePayOrderBean implements Serializable{

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

    /**
     返回响应码 200 300 302 304 370 371
     370 买单优惠已无效  前端需要逻辑处理
     如果用户选择继续支付 则请添加参数is_continue=1 再次调用当前接口生成订单
     371 该门店未开通线上买单：门店未开通线上买单/开通了线上买单但是没有买单优惠数据 其实属于参数错误类别302
     */
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
             * 用户总余额
             */
            Double user_account_money;
            /**
             * 应付总金额
             */
            Double total_price;
            /**
             * 门店名称
             */
            String shop_name;
            /**
             * 门店id
             */
            String shop_id;
            /**
             * 订单id
             */
            String order_id;
            /**
             * 订单编号
             */
            String order_sn;

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

            public String getShop_name() {
                return shop_name;
            }

            public String getShop_id() {
                return shop_id;
            }

            public void setShop_id(String shop_id) {
                this.shop_id = shop_id;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public Double getTotal_price() {
                return total_price;
            }

            public void setTotal_price(Double total_price) {
                this.total_price = total_price;
            }

            public Double getUser_account_money() {
                return user_account_money;
            }

            public void setUser_account_money(Double user_account_money) {
                this.user_account_money = user_account_money;
            }
        }

    }

}
