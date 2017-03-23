package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 */

public class UnReadMessageCountBean {

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

            /**
             * 系统未读消息数量
             */
            int system_message_count;
            /**
             * 佣金未读消息数量
             */
            int money_message_count;

            public int getMoney_message_count() {
                return money_message_count;
            }

            public void setMoney_message_count(int money_message_count) {
                this.money_message_count = money_message_count;
            }

            public int getSystem_message_count() {
                return system_message_count;
            }

            public void setSystem_message_count(int system_message_count) {
                this.system_message_count = system_message_count;
            }
        }

    }

}
