package com.fanwe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 狗蛋哥/zlh on 2016/9/28.
 * 获取用户会员升级前的信息
 */
public class UserUpdateInfoBean implements Serializable{

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

        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            String total_price;
            String user_account_money;

            public Double getTotalPrice(){
                double total = Double.parseDouble(getTotal_price());
                BigDecimal bigDecimal = new BigDecimal(total);
                return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            public Double getUserAccountMoney(){
                double total = Double.parseDouble(getUser_account_money());
                BigDecimal bigDecimal = new BigDecimal(total);
                return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getUser_account_money() {
                return user_account_money;
            }

            public void setUser_account_money(String user_account_money) {
                this.user_account_money = user_account_money;
            }
        }

    }

}
