package com.miguo.entity;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/18
 * Description: 
 */

public class CityGroupListBean {

    private String message;
    private String token;
    private String statusCode;
    private long timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
            /**
             * uname : Chaozhou
             * name : 潮州
             * id : 002a6000-5c1e-4cda-b7f4-63c08c1d2463
             */

            private List<已开通城市Bean> 已开通城市;

            public List<已开通城市Bean> get已开通城市() {
                return 已开通城市;
            }

            public void set已开通城市(List<已开通城市Bean> 已开通城市) {
                this.已开通城市 = 已开通城市;
            }

            public  class 已开通城市Bean {
                private String uname;
                private String name;
                private String id;

                public String getUname() {
                    return uname;
                }

                public void setUname(String uname) {
                    this.uname = uname;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }
}
