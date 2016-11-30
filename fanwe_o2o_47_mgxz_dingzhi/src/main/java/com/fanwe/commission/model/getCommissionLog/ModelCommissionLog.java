package com.fanwe.commission.model.getCommissionLog;

/**
 * Created by didik on 2016/8/28.
 */
public class ModelCommissionLog {
//    "money": "45.00",              操作金额，提现为负
//    "user_id": "292ca354-2637-4546-b19f-21750de0cbd8",          用户id
//    "description": "会员升级金额增加:二级下线",    日志说明
//    "insert_time": "1472016372071",
//    "money_type": "1",      资金类型：0：获得佣金,1：获得会员升级费用,2：用户余额充值，3:佣金消费，4:佣金提现，5:会员升级费用消费，6:会员升级费用提现，7:余额消费，8：余额提现，9:在线支付记录,10:用户余额退款,11:用户佣金退款,12:会员升级费用退款，13：在线支付退款
//    "dist_id_from": "4118fb9f-0ea7-4d81-98b6-ab14646d0ec8",        分销来源的用户id
//    ----------------------------------20160826 增加响应参数----------------------------------
//    "mobile": "10000000000": 分销来源的用户的手机号码
//    -----------------------------------------------------------------------------------------
//    "id": "21974238-5ba0-424a-9e08-2ab74c0952aa"


    //    "money":"0.15",
//            "user_id":"21070107-7cba-4fc6-8df6-f54509f13bca",
//            "before_money":"1000.00",
//            "mobile":"",-----------//来源人：188****5484
//            "description":"生成订单时会员升级费用支付",
//            "insert_time":"1473664112240",
//            "money_type":"会员升级费用消费",------//money_type
//            "dist_id_from":"",
//            "id":"92e796ff-e56d-42e8-a3bd-28de4e0d0f38",
//            "order_id":"1885173e-da76-4491-9c66-1e287b28d51f"
    //title;item
    private int type;
    private String money;
    private String user_id;
    private String mobile;
    private String description;
    private String insert_time;
    private String money_type;
    private String dist_id_from;
    private String id;
    private String order_id;
    private String order_sn;
    private String before_money;
    private String jump_id;
    private String order_id_item;
    private int jump_type;

    public boolean isJumpToGoodsDetail(){
        return getJump_type() == 1;
    }

    public String getJump_id() {
        return jump_id;
    }

    public void setJump_id(String jump_id) {
        this.jump_id = jump_id;
    }

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getOrder_id_item() {
        return order_id_item;
    }

    public void setOrder_id_item(String order_id_item) {
        this.order_id_item = order_id_item;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBefore_money() {
        return before_money;
    }

    public void setBefore_money(String before_money) {
        this.before_money = before_money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getMoney_type() {
        return money_type;
    }

    public void setMoney_type(String money_type) {
        this.money_type = money_type;
    }

    public String getDist_id_from() {
        return dist_id_from;
    }

    public void setDist_id_from(String dist_id_from) {
        this.dist_id_from = dist_id_from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

}
