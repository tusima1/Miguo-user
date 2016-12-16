package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh on 2016/12/14.
 * 获取用户升级信息，余额是否充足
 */
public class UserUpgradeOrderBean implements Serializable{

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
            double total_price;
            double user_account_money;

            public boolean canUpdate(){
                return getUser_account_money() > getTotal_price();
            }

            public double getTotal_price() {
                return total_price;
            }

            public void setTotal_price(double total_price) {
                this.total_price = total_price;
            }

            public double getUser_account_money() {
                return user_account_money;
            }

            public void setUser_account_money(double user_account_money) {
                this.user_account_money = user_account_money;
            }
        }

    }

}
