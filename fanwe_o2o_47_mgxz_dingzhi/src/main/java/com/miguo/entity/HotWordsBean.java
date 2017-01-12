package com.miguo.entity;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/12
 * Description: 
 */

public class HotWordsBean {

    /**
     * result : [{"body":{"hotkey":"热门搜索1,热门搜索2,热门搜索3,热门搜索4"}}]
     * message : 操作成功
     * token :
     * statusCode : 200
     * timestamp : 1484186258269
     */

    private String message;
    private String token;
    private String statusCode;
    private long timestamp;
    /**
     * body : {"hotkey":"热门搜索1,热门搜索2,热门搜索3,热门搜索4"}
     */

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

    public static class ResultBean {
        /**
         * hotkey : 热门搜索1,热门搜索2,热门搜索3,热门搜索4
         */

        private BodyBean body;

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public static class BodyBean {
            private String hotkey;

            public String getHotkey() {
                return hotkey;
            }

            public void setHotkey(String hotkey) {
                this.hotkey = hotkey;
            }
        }
    }
}
