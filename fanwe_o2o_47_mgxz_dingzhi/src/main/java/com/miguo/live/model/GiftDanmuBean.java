package com.miguo.live.model;

import com.fanwe.utils.MGStringFormatter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh on 2016/9/18.
 * 弹幕花钻石回调
 */
public class GiftDanmuBean implements Serializable{

    String message;
    int statusCode;

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public class ResultBean implements Serializable{
        /**
         * gift_num : 3
         * userdiamond : 0.00
         */

        private List<BodyBean> body;

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public class BodyBean implements Serializable{
            private String gift_num;
            private String userdiamond;

            public int getGift_num() {
                return MGStringFormatter.getInt(gift_num);
            }

            public void setGift_num(String gift_num) {
                this.gift_num = gift_num;
            }

            public String getUserdiamond() {
                return userdiamond;
            }

            public void setUserdiamond(String userdiamond) {
                this.userdiamond = userdiamond;
            }
        }
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
}
