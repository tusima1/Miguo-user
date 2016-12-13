package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh on 2016/12/13.
 */

public class MemberInterestBean implements Serializable{

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
             * 等级 1初级 2高级
             */
            int level;
            /**
             * 是否可以提现 0否1是
             */
            int can_withdraw;
            /**
             * 代言数量
             */
            int represent_num;
            /**
             * 是否可以消费 0否1是
             */
            int can_consume;

            public boolean canWithdraw(){
                return getCan_withdraw() == 1;
            }

            public boolean canConsume(){
                return getCan_consume() == 1;
            }

            public int getCan_consume() {
                return can_consume;
            }

            public void setCan_consume(int can_consume) {
                this.can_consume = can_consume;
            }

            public int getCan_withdraw() {
                return can_withdraw;
            }

            public void setCan_withdraw(int can_withdraw) {
                this.can_withdraw = can_withdraw;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getRepresent_num() {
                return represent_num;
            }

            public void setRepresent_num(int represent_num) {
                this.represent_num = represent_num;
            }
        }

    }

}
