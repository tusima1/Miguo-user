package com.fanwe.sellTemp.model.postRedPacket;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ModelPostRedPacket {
    /**
     * 商家ID
     */
    private String id;
    /**
     * 特别说明
     */
    private String special_note;
    /**
     * 金额限制
     */
    private String amount_limit;
    /**
     * 是否可以与其他优惠叠加使用
     */
    private String is_preferential_overlay;
    /**
     * 可用时段开始
     */
    private String available_time_start;
    /**
     * 可用时段结束
     */
    private String available_time_end;
    /**
     * 使用区域限制
     */
    private String use_area_restrictions;
    /**
     * 有效期结束
     */
    private String event_end;
    /**
     * 不可用期间（不可用期间类型为其他时间时，参数格式：20160606，20160707，20160808）
     */
    private String available_period_data;
    /**
     * 不可用期间类型
     */
    private String available_period;
    /**
     * 使用店铺限制
     */
    private String use_store_restrictions;
    /**
     * 是否每次消费只能用一个
     */
    private String only_one;
    /**
     * 红包名称
     */
    private String red_packet_name;
    /**
     * 有效期开始
     */
    private String event_start;
    /**
     * 类型,折扣,个数;类型,折扣,个数;类型,折扣,个数 1,1,10;2,2,20;3,3,10.5		红包信息
     */
    private String red_packets;
    /**
     * 最高抵多少元
     */
    private String deduction_limit;

    public String getSpecial_note() {
        return special_note;
    }

    public void setSpecial_note(String special_note) {
        this.special_note = special_note;
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

    public String getAvailable_time_start() {
        return available_time_start;
    }

    public void setAvailable_time_start(String available_time_start) {
        this.available_time_start = available_time_start;
    }

    public String getAvailable_time_end() {
        return available_time_end;
    }

    public void setAvailable_time_end(String available_time_end) {
        this.available_time_end = available_time_end;
    }

    public String getUse_area_restrictions() {
        return use_area_restrictions;
    }

    public void setUse_area_restrictions(String use_area_restrictions) {
        this.use_area_restrictions = use_area_restrictions;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }

    public String getAvailable_period_data() {
        return available_period_data;
    }

    public void setAvailable_period_data(String available_period_data) {
        this.available_period_data = available_period_data;
    }

    public String getAvailable_period() {
        return available_period;
    }

    public void setAvailable_period(String available_period) {
        this.available_period = available_period;
    }

    public String getUse_store_restrictions() {
        return use_store_restrictions;
    }

    public void setUse_store_restrictions(String use_store_restrictions) {
        this.use_store_restrictions = use_store_restrictions;
    }

    public String getOnly_one() {
        return only_one;
    }

    public void setOnly_one(String only_one) {
        this.only_one = only_one;
    }

    public String getRed_packet_name() {
        return red_packet_name;
    }

    public void setRed_packet_name(String red_packet_name) {
        this.red_packet_name = red_packet_name;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getRed_packets() {
        return red_packets;
    }

    public void setRed_packets(String red_packets) {
        this.red_packets = red_packets;
    }

    public String getDeduction_limit() {
        return deduction_limit;
    }

    public void setDeduction_limit(String deduction_limit) {
        this.deduction_limit = deduction_limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
