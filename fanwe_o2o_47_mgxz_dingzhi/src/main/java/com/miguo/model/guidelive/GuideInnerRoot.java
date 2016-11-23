package com.miguo.model.guidelive;

import java.util.List;

/**
 * Created by didik on 2016/11/22.
 */

public class GuideInnerRoot {

    private String message;
    private String token;
    private String statusCode;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public class ResultBean {
        private List<BodyBean> body;

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public class BodyBean {


            private List<GuideInnerGoods> goods_list;

            public List<GuideInnerGoods> getGoods_list() {
                return goods_list;
            }

            public void setGoods_list(List<GuideInnerGoods> goods_list) {
                this.goods_list = goods_list;
            }
        }
    }
}
