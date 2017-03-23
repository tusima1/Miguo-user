package com.miguo.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/21.
 */

public class JpushMessageBean implements Serializable{

    /**
     * 1 系统消息
     * 2 佣金-代言-钱款
     */
    int message_type;

    /**
     * 跳转类型
     * TARGET_MONEY_LIST：    佣金-代言-钱款消息列表
     * TARGET_SYSTEM_MESSAGE：系统消息详情
     */
    String push_jump_target;
    /**
     * 跳转目的地传递的值
     */
    String push_jump_paramater;

    /**
     * 系统消息的id
     * 用户已读消息控制
     */
    String system_message_id;

    /**
     * 消息标题
     */
    String title;

    /**
     * 消息详情
     */
    String message;

    long create_time;

    public String getTime(){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(getCreate_time());
            return format.format(date);
        }catch (Exception e){
            return "";
        }
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getPush_jump_paramater() {
        return push_jump_paramater;
    }

    public void setPush_jump_paramater(String push_jump_paramater) {
        this.push_jump_paramater = push_jump_paramater;
    }

    public String getPush_jump_target() {
        return push_jump_target;
    }

    public void setPush_jump_target(String push_jump_target) {
        this.push_jump_target = push_jump_target;
    }
}
