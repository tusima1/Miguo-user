package com.fanwe.user.model.getGroupBuyCoupon;

import java.io.Serializable;
import java.util.List;

/**
 * Created by didik on 2016/8/24.
 */
public class ModelGroupCoupon implements Serializable {
    /**
     * fx_user_id :
     * is_enable : 1
     * tuan_id : 32b8820d-13f6-4f14-9c24-355dae972523
     * icon :
     * confirm_account :
     * password : 623687990040
     * balance_memo :
     * coupon_price :
     * id : 1775dbb7-664e-417e-93f1-6626fe890824
     * refund_status : 0
     * coupon :
     * create_time : 1471956755889
     * use_time :
     * is_new : 0
     * end_time : 0
     * begin_time : 0
     * is_delete :
     * order_item_id : bc215691-9c9d-45a0-9894-d3f633ca0025
     * shop_id :
     * ent_id : 32b8820d-13f6-4f14-9c24-355dae972523
     * is_balance : 0
     * balance_price : 11.00
     * user_id : 88025143-194f-4705-991b-7f5a3587dc9c
     * balance_time :
     * name : 保定1号团购1
     * order_id : bc215691-9c9d-45a0-9894-d3f633ca0025
     * status : 1
     */
    private String fx_user_id;//分销用户ID
    private String is_enable;//是否启用，1启用0不启用
    private String tuan_id;//团购id
    private String icon;//团购图片地址url
    private String confirm_account;//验证团购券的商家帐号ID
    private String password;//团购券密码
    private String balance_memo;//管理员结算的备注
    private String coupon_price;//团购券的价格，用于退款时的计算，按件为单件价，按单为总价
    private String id;
    private String refund_status;//退款状态 0:无 1:用户申请退款 2:已确认 3:拒绝退款
    private String coupon;//团购券验证号
    private String create_time;//团购券生成时间
    private String use_time;//使用时间
    private String is_new;//新券标识 0:未被会员查看 1：已查看
    private String end_time;//团购券过期时间
    private String begin_time;//团购券生效时间
    private String is_delete;//删除标识
    private String order_item_id;
    private String shop_id;//消费的门店
    private String ent_id;//商户ID
    private String is_balance;//0:未结算 1:待结算 2:已结算
    private String balance_price;//生成团购券时由商品表中同步生成该值：结算单价
    private String user_id;//用户id
    private String balance_time;//结算时间
    private String name;//团购名称
    private String order_id;//订单id
    private String status;//状态，1为未使用，2为已使用
    private List<ModelShopInfo2> shop_list;

    public List<ModelShopInfo2> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<ModelShopInfo2> shop_list) {
        this.shop_list = shop_list;
    }

    public String getFx_user_id() {
        return fx_user_id;
    }

    public void setFx_user_id(String fx_user_id) {
        this.fx_user_id = fx_user_id;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getTuan_id() {
        return tuan_id;
    }

    public void setTuan_id(String tuan_id) {
        this.tuan_id = tuan_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getConfirm_account() {
        return confirm_account;
    }

    public void setConfirm_account(String confirm_account) {
        this.confirm_account = confirm_account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance_memo() {
        return balance_memo;
    }

    public void setBalance_memo(String balance_memo) {
        this.balance_memo = balance_memo;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUse_time() {
        return use_time;
    }

    public void setUse_time(String use_time) {
        this.use_time = use_time;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(String ent_id) {
        this.ent_id = ent_id;
    }

    public String getIs_balance() {
        return is_balance;
    }

    public void setIs_balance(String is_balance) {
        this.is_balance = is_balance;
    }

    public String getBalance_price() {
        return balance_price;
    }

    public void setBalance_price(String balance_price) {
        this.balance_price = balance_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBalance_time() {
        return balance_time;
    }

    public void setBalance_time(String balance_time) {
        this.balance_time = balance_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
