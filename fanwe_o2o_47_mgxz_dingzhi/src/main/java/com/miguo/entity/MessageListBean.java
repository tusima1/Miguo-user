package com.miguo.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/20.
 * 二级消息列表
 */
public class MessageListBean implements Serializable{

    int statusCode;

    String message;

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
        int page_total;
        int page;
        int page_count;
        int page_size;

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_total() {
            return page_total;
        }

        public void setPage_total(int page_total) {
            this.page_total = page_total;
        }

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            /**
             * 发送时间
             */
            long create_time;
            /**
             * 已读状态 0 未读  1 已读
             */
            int read_status;
            /**
             * 消息分类 1 系统  2 用户
             */
            int message_type;
            /**
             * 消息id
             */
            String id;
            /**
             * 消息标题
             */
            String title;
            /**
             * 消息跳转类型
             */
            String list_jump_target;
            /**
             * 消息跳转类型值
             */
            String list_jump_paramater;
            /**
             * 系统消息id
             */
            String system_message_id;
            /**
             * 概要
             */
            String content;

            public String getTime(){
                try{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = new Date(getCreate_time());
                    return format.format(date);
                }catch (Exception e){
                    return "";
                }
            }

            public boolean hasRead(){
                return getRead_status() == 1;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getList_jump_paramater() {
                return list_jump_paramater;
            }

            public void setList_jump_paramater(String list_jump_paramater) {
                this.list_jump_paramater = list_jump_paramater;
            }

            public String getList_jump_target() {
                return list_jump_target;
            }

            public void setList_jump_target(String list_jump_target) {
                this.list_jump_target = list_jump_target;
            }

            public int getMessage_type() {
                return message_type;
            }

            public void setMessage_type(int message_type) {
                this.message_type = message_type;
            }

            public int getRead_status() {
                return read_status;
            }

            public void setRead_status(int read_status) {
                this.read_status = read_status;
            }

            public String getSystem_message_id() {
                return system_message_id;
            }

            public void setSystem_message_id(String system_message_id) {
                this.system_message_id = system_message_id;
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
