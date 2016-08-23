package com.fanwe.user.model.getUserRedpackets;

/**
 * Created by didik on 2016/8/22.
 */
public class ModelUserRedPacket {
    private String tencent_room_id;//房间id
    private String available_time_end;//可用时间段结束
    private String red_packets_create_date;//红包生成日期
    private String red_packet_name;//红包名称
    private String deduction_limit;//最高抵多少元
    private String event_end;//有效期结束
    private String event_flag;//红包过期标志
    private String available_period;//不可用期间类型 ???
    private String id;//商家id
    private String spokesman_id;//代言人编号
    private String only_one;//是否每次消费只能用一个
    private String red_packet_type;// 红包类型 1:打折  2:现金券
    private String special_note;//特别说明
    private String red_packet_id;//红包id
    private String red_packet_amount;//红包金额/折扣
    private String is_used;//是否已使用
    private String use_area_restrictions;//使用区域限制
    private String use_store_restrictions;//使用店铺限制
    private String event_start;//有效期开始
    private String available_time_start;//可用时间段开始
    private String user_id;//用户id
    private String amount_limit;//金额限制
    private String is_preferential_overlay;//是否可以与其他优惠叠加使用
    private String order_sn;//使用红包订单ID
    private String available_period_data;//不可用期间 ???
//    private String is_query;//用户红包查看标记
    /**
     * 为选择红包操作立flag-
     */
    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTencent_room_id() {
        return tencent_room_id;
    }

    public void setTencent_room_id(String tencent_room_id) {
        this.tencent_room_id = tencent_room_id;
    }

    public String getAvailable_time_end() {
        return available_time_end;
    }

    public void setAvailable_time_end(String available_time_end) {
        this.available_time_end = available_time_end;
    }

    public String getRed_packets_create_date() {
        return red_packets_create_date;
    }

    public void setRed_packets_create_date(String red_packets_create_date) {
        this.red_packets_create_date = red_packets_create_date;
    }

    public String getRed_packet_name() {
        return red_packet_name;
    }

    public void setRed_packet_name(String red_packet_name) {
        this.red_packet_name = red_packet_name;
    }

    public String getDeduction_limit() {
        return deduction_limit;
    }

    public void setDeduction_limit(String deduction_limit) {
        this.deduction_limit = deduction_limit;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }

    public String getEvent_flag() {
        return event_flag;
    }

    public void setEvent_flag(String event_flag) {
        this.event_flag = event_flag;
    }

    public String getAvailable_period() {
        return available_period;
    }

    public void setAvailable_period(String available_period) {
        this.available_period = available_period;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpokesman_id() {
        return spokesman_id;
    }

    public void setSpokesman_id(String spokesman_id) {
        this.spokesman_id = spokesman_id;
    }

    public String getOnly_one() {
        return only_one;
    }

    public void setOnly_one(String only_one) {
        this.only_one = only_one;
    }

    public String getRed_packet_type() {
        return red_packet_type;
    }

    public void setRed_packet_type(String red_packet_type) {
        this.red_packet_type = red_packet_type;
    }

    public String getSpecial_note() {
        return special_note;
    }

    public void setSpecial_note(String special_note) {
        this.special_note = special_note;
    }

    public String getRed_packet_id() {
        return red_packet_id;
    }

    public void setRed_packet_id(String red_packet_id) {
        this.red_packet_id = red_packet_id;
    }

    public String getRed_packet_amount() {
        return red_packet_amount;
    }

    public void setRed_packet_amount(String red_packet_amount) {
        this.red_packet_amount = red_packet_amount;
    }

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public String getUse_area_restrictions() {
        return use_area_restrictions;
    }

    public void setUse_area_restrictions(String use_area_restrictions) {
        this.use_area_restrictions = use_area_restrictions;
    }

    public String getUse_store_restrictions() {
        return use_store_restrictions;
    }

    public void setUse_store_restrictions(String use_store_restrictions) {
        this.use_store_restrictions = use_store_restrictions;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getAvailable_time_start() {
        return available_time_start;
    }

    public void setAvailable_time_start(String available_time_start) {
        this.available_time_start = available_time_start;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount_limit() {
        return amount_limit;
    }

    public void setAmount_limit(String amount_limit) {
        this.amount_limit = amount_limit;
    }

    public String getIs_preferential_overlay() {
        return is_preferential_overlay;
    }

    public void setIs_preferential_overlay(String is_preferential_overlay) {
        this.is_preferential_overlay = is_preferential_overlay;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAvailable_period_data() {
        return available_period_data;
    }

    public void setAvailable_period_data(String available_period_data) {
        this.available_period_data = available_period_data;
    }
}
