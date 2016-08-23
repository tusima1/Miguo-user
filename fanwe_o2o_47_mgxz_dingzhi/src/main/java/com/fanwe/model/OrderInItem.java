package com.fanwe.model;

public class OrderInItem {
	
//	'order_sn': '2016020206300523',
//    'slname': '老家面',
//    'supplier_id': '805', //商家ID
//    'location_id': '855', //门店ID，跳转到门店详情界面时请以data_id名称传此参数
//    'fx_user_id': '123', //分销ID，如果是分销订单,请以uid的名称传该参数//xx
//    'cate_id': 8,
//    'id': '190',
//    'deal_id': 4592, //当商品ID为0时表示到店支付优惠买单
//    'deal_icon': 'http://mgxz.web.com/public/attachment/201510/27/11/562ef0b30252a_244x148.jpg',
//    'name': '【嵊州】老家面 仅售14.5元！价值17元的三鲜面一份，提供免费WiFi',
//    'sub_name': '老家面14.5元三鲜面',
//    'number': '1',
//    'unit_price': 14.5,
//    'total_price': 14.5,
//    'consume_count': 0, //消费数量，为0则是待消费
//    'dp_id': 0, //点评ID，如果没点评则为0
//    'delivery_status': 5,
//    'is_arrival': 0,
//    'is_refund': 1,//是否支持退款
//    'refund_status': 0 //0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结
	
	private String order_sn;
	private String slname;
	private String supplier_id;
	private String location_id;
	private String fx_user_id;
	private int cate_id;
	private String id;
	private int deal_id;
	private String deal_icon;
	private String name;
	private String sub_name;
	private String number;
	private float unit_price;
	private float total_price;
	private int consume_count;
	private int dp_id;
	private int delivery_status;
	private int is_arrival;
	private int is_refund;
	private int refund_status;
	private int refunding;
	private int refunded;

	//	'order_sn': '2016020206300523',
//    'slname': '老家面',
//    'location_id': '855', //门店ID，跳转到门店详情界面时请以data_id名称传此参数
//    'cate_id': 8,
//    'id': '190',
//    'deal_id': 4592, //当商品ID为0时表示到店支付优惠买单
//    'deal_icon': 'http://mgxz.web.com/public/attachment/201510/27/11/562ef0b30252a_244x148.jpg',
//    'name': '【嵊州】老家面 仅售14.5元！价值17元的三鲜面一份，提供免费WiFi',
//    'sub_name': '老家面14.5元三鲜面',
//    'number': '1',
//    'total_price': 14.5,
//    'consume_count': 0, //消费数量，为0则是待消费
//    'dp_id': 0, //点评ID，如果没点评则为0
//    'is_refund': 1,//是否支持退款
//    'refund_status': 0 //0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结
	
	public int getRefunding() {
		return refunding;
	}
	public void setRefunding(int refunding) {
		this.refunding = refunding;
	}
	public int getRefunded() {
		return refunded;
	}
	public void setRefunded(int refunded) {
		this.refunded = refunded;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getSlname() {
		return slname;
	}
	public void setSlname(String slname) {
		this.slname = slname;
	}
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public String getFx_user_id() {
		return fx_user_id;
	}
	public void setFx_user_id(String fx_user_id) {
		this.fx_user_id = fx_user_id;
	}
	public int getCate_id() {
		return cate_id;
	}
	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDeal_id() {
		return deal_id;
	}
	public void setDeal_id(int deal_id) {
		this.deal_id = deal_id;
	}
	public String getDeal_icon() {
		return deal_icon;
	}
	public void setDeal_icon(String deal_icon) {
		this.deal_icon = deal_icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public float getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(float unit_price) {
		this.unit_price = unit_price;
	}
	public float getTotal_price() {
		return total_price;
	}
	public void setTotal_price(float total_price) {
		this.total_price = total_price;
	}
	public int getConsume_count() {
		return consume_count;
	}
	public void setConsume_count(int consume_count) {
		this.consume_count = consume_count;
	}
	public int getDp_id() {
		return dp_id;
	}
	public void setDp_id(int dp_id) {
		this.dp_id = dp_id;
	}
	public int getDelivery_status() {
		return delivery_status;
	}
	public void setDelivery_status(int delivery_status) {
		this.delivery_status = delivery_status;
	}
	public int getIs_arrival() {
		return is_arrival;
	}
	public void setIs_arrival(int is_arrival) {
		this.is_arrival = is_arrival;
	}
	public int getIs_refund() {
		return is_refund;
	}
	public void setIs_refund(int is_refund) {
		this.is_refund = is_refund;
	}
	public int getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(int refund_status) {
		this.refund_status = refund_status;
	}

	
	
}
