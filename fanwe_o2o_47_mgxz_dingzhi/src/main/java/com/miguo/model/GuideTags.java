package com.miguo.model;

import java.util.List;

/**
 * Created by didik on 2016/11/17.
 */

public class GuideTags {

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
        /**
         * tab_id :
         * title : 默认
         */

        private List<GuideTagInfo> body;

        public List<GuideTagInfo> getBody() {
            return body;
        }

        public void setBody(List<GuideTagInfo> body) {
            this.body = body;
        }

        public class GuideTagInfo {
            private String tab_id;
            private String title;

            public String getTab_id() {
                return tab_id;
            }

            public void setTab_id(String tab_id) {
                this.tab_id = tab_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
