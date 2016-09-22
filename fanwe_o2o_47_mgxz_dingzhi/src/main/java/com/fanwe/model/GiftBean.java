package com.fanwe.model;

import com.fanwe.utils.MGStringFormatter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GiftBean implements Serializable{
    /**
     * result : [{"body":[{"gift_num":"3","userdiamond":"0.00"}]}]
     * message : 操作成功
     * statusCode : 200
     * token : 32d32493c3fbfb58f0115e03c49b1242
     */

    private String message;
    private String statusCode;
    private String token;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return MGStringFormatter.getInt(statusCode);
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

//    int statusCode;
//    String token;
//    String message;
//    List<Result> result;
//
//    public int getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(int statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public List<Result> getResult() {
//        return result;
//    }
//
//    public void setResult(List<Result> result) {
//        this.result = result;
//    }
//
//    public class Result implements Serializable{
//        List<Body> body;
//
//        public List<Body> getBody() {
//            return body;
//        }
//
//        public void setBody(List<Body> body) {
//            this.body = body;
//        }
//
//        public class Body implements Serializable{
//            int gift_num;
//
//            public int getGift_num() {
//                return gift_num;
//            }
//
//            public void setGift_num(int gift_num) {
//                this.gift_num = gift_num;
//            }
//        }
//
//
//    }



}
