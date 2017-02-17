package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OnlinePayOrderPaymentBean implements Serializable{

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
             * 分享信息
             */
            Share share_info;

            /**
             * 老板娘头像
             */
            String icon;

            /**
             * "新春快乐，万事如意",
             * 红包贺词
             */
            String content;
            /**
             * 订单信息
             */
            OrderInfo order_info;

            /**
             * 第三方支付回调信息
             */
            UserUpgradeOrderBean2.Result.Body.Config config;

            /**
             * 支付接口名
             */
            String class_name;

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public UserUpgradeOrderBean2.Result.Body.Config getConfig() {
                return config;
            }

            public void setConfig(UserUpgradeOrderBean2.Result.Body.Config config) {
                this.config = config;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public OrderInfo getOrder_info() {
                return order_info;
            }

            public void setOrder_info(OrderInfo order_info) {
                this.order_info = order_info;
            }

            public Share getShare_info() {
                return share_info;
            }

            public void setShare_info(Share share_info) {
                this.share_info = share_info;
            }

            public class Share implements Serializable{

                /**
                 * 点击链接
                 */
                String clickurl;

                /**
                 * 内容简介
                 */
                String summary;

                /**
                 * 分享图片显示
                 */
                String imageurl;
                /**
                 * 分享标题
                 */
                String title;

                public String getClickurl() {
                    return clickurl;
                }

                public void setClickurl(String clickurl) {
                    this.clickurl = clickurl;
                }

                public String getImageurl() {
                    return imageurl;
                }

                public void setImageurl(String imageurl) {
                    this.imageurl = imageurl;
                }

                public String getSummary() {
                    return summary;
                }

                public void setSummary(String summary) {
                    this.summary = summary;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

            public class OrderInfo implements Serializable{
                /**
                 * 订单状态
                 *
                 */
                int order_status;
                /**
                 * 第三方应付金额
                 */
                Double total_price;
                /**
                 * 用户id
                 */
                String user_id;
                /**
                 * 支付方式id
                 */
                String payment_id;
                /**
                 * 线上买单的佣金
                 */
                String salary;
                /**
                 * 订单id
                 */
                String order_id;
                /**
                 * 订单类型
                 */
                int order_type;
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

                public int getOrder_status() {
                    return order_status;
                }

                public void setOrder_status(int order_status) {
                    this.order_status = order_status;
                }

                public int getOrder_type() {
                    return order_type;
                }

                public void setOrder_type(int order_type) {
                    this.order_type = order_type;
                }

                public String getPayment_id() {
                    return payment_id;
                }

                public void setPayment_id(String payment_id) {
                    this.payment_id = payment_id;
                }

                public String getSalary() {
                    return salary;
                }

                public void setSalary(String salary) {
                    this.salary = salary;
                }

                public Double getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(Double total_price) {
                    this.total_price = total_price;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }
            }

        }

    }

}
