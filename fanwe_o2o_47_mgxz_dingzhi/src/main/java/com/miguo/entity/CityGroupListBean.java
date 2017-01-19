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
        /**
         * group_name : 已开通城市
         * group_item : [{"uname":"Chaozhou","name":"潮州",
         * "id":"002a6000-5c1e-4cda-b7f4-63c08c1d2463"},{"uname":"Shengzhou","name":"嵊州",
         * "id":"fc9ebab9-7aa1-49d5-8c56-2bddc7d92ded"}]
         */

        private List<BodyBean> body;

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public class BodyBean {
            private String group_name;
            /**
             * uname : Chaozhou
             * name : 潮州
             * id : 002a6000-5c1e-4cda-b7f4-63c08c1d2463
             */

            private List<GroupItemBean> group_item;

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public List<GroupItemBean> getGroup_item() {
                return group_item;
            }

            public void setGroup_item(List<GroupItemBean> group_item) {
                this.group_item = group_item;
            }

            public class GroupItemBean {
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
