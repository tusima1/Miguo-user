package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/9.
 */
public class CheckCitySignBean implements Serializable{

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
            /**
             * 1有商家0没有商家
             */
            int is_sign;
            /**
             * 活动标题
             */
            String city_image_title;
            /**
             * 图片跳转链接
             */
            String city_image_link;
            /**
             * 图片链接
             */
            String city_image;
            public int getIs_sign() {
                return is_sign;
            }

            public void setIs_sign(int is_sign) {
                this.is_sign = is_sign;
            }

            public String getCity_image() {
                return city_image;
            }

            public void setCity_image(String city_image) {
                this.city_image = city_image;
            }

            public String getCity_image_link() {
                return city_image_link;
            }

            public void setCity_image_link(String city_image_link) {
                this.city_image_link = city_image_link;
            }

            public String getCity_image_title() {
                return city_image_title;
            }

            public void setCity_image_title(String city_image_title) {
                this.city_image_title = city_image_title;
            }
        }

    }

}
