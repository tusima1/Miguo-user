package com.fanwe.model;




import java.util.List;



import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.utils.SDFormatUtil;

/**
 * 会员中心订单实体
 * 
 * @author Administrator
 * 
 */

public class Uc_orderModel
{

	
	
	private  int id;
	private String order_sn;
	private int order_status; // 订单状态 0:未结单 1:结单(将出现删除订单按钮)
	private int pay_status; // 支付状态 0:未支付(出现取消订单按钮) 1:已支付
	private String create_time; // 下单时间
	private String pay_amount; // 已付金额
	private String total_price; // 应付金额
	private int c; // 商品总量
	private int payment_id;
	private  List<Uc_orderGoodsModel> deal_order_item;
	private String status; // 未支付 string 订单状态
	private int refund_status;
	private int status_value;//0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结
	
	
	// add
	private String total_priceFormat;
	private String pay_amountFormat;
	
//	 'item': [
//	          {
//	            'id': '164',
//	            'order_sn': '2016020206300523',
//	            'order_status': '0',
//	            'pay_status': '2',
//	            'payment_id': '0', //0,15余额支付; 16,17,20,21支付宝; 23,25微信；
//	            'refund_status': '0',
//	            'create_time': '2016-02-02 18:30:05',
//	            'pay_amount': 14.5, //已支付总额
//	            'total_price': 14.5, //订单总额
//	            'c': 1, //包含商品总数
//	            'deal_order_item': [
//	              {
//	                'order_sn': '2016020206300523',
//	                'slname': '老家面',
//	                'supplier_id': '805', //商家ID
//	                'location_id': '855', //门店ID，跳转到门店详情界面时请以data_id名称传此参数
//	                'fx_user_id': '123', //分销ID，如果是分销订单,请以uid的名称传该参数
//	                'cate_id': 8,
//	                'id': '190',
//	                'deal_id': 4592, //当商品ID为0时表示到店支付优惠买单
//	                'deal_icon': 'http://mgxz.web.com/public/attachment/201510/27/11/562ef0b30252a_244x148.jpg',
//	                'name': '【嵊州】老家面 仅售14.5元！价值17元的三鲜面一份，提供免费WiFi',
//	                'sub_name': '老家面14.5元三鲜面',
//	                'number': '1',
//	                'unit_price': 14.5,
//	                'total_price': 14.5,
//	                'consume_count': 0, //消费数量，为0则是待消费
//	                'dp_id': 0, //点评ID，如果没点评则为0
//	                'delivery_status': 5,
//	                'is_arrival': 0,
//	                'is_refund': 1,
//	                'refund_status': 0 //0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结
//	              }
//	            ],
//	            'status': '已支付',
//	            'status_value': 2 //0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结,6:已取消
//	          }
//	        ],

	
	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}

	public int getStatus_value() {
		return status_value;
	}

	public void setStatus_value(int status_value) {
		this.status_value = status_value;
	}

	public int getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(int refund_status) { 
		this.refund_status = refund_status;
	}

	public boolean hasCancelButton()
	{
		boolean has = false;
		if (order_status == 1)
		{
			has = true;
		} else
		{
			if (pay_status == 0)
			{
				has = true;
			}
		}
		return has;
	}

	public String getCancelButtonText()
	{
		String text = null;
		if (order_status == 1)
		{
			text = "删除订单";
		} else
		{
			if (pay_status == 0)
			{
				text = "取消订单";
			}
		}
		return text;
	}

	public String getPay_amountFormat()
	{
		return pay_amountFormat;
	}

	public void setPay_amountFormat(String pay_amountFormat)
	{
		this.pay_amountFormat = pay_amountFormat;
	}

	public String getTotal_priceFormat()
	{
		return total_priceFormat;
	}

	public void setTotal_priceFormat(String total_priceFormat)
	{
		this.total_priceFormat = total_priceFormat;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getOrder_sn()
	{
		return order_sn;
	}

	public void setOrder_sn(String order_sn)
	{
		this.order_sn = order_sn;
	}

	public int getOrder_status()
	{
		return order_status;
	}

	public void setOrder_status(int order_status)
	{
		this.order_status = order_status;
		updateGoodsStatus();
	}

	public int getPay_status()
	{
		return pay_status;
	}

	public void setPay_status(int pay_status)
	{
		this.pay_status = pay_status;
		updateGoodsStatus();
	}

	public String getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}

	public String getPay_amount()
	{
		return pay_amount;
	}

	public void setPay_amount(String pay_amount)
	{
		this.pay_amount = pay_amount;
		double payAmount = SDTypeParseUtil.getDouble(pay_amount);
		if (payAmount > 0)
		{
			this.pay_amountFormat = ",已支付" + SDFormatUtil.formatMoneyChina(pay_amount);
		}
	}

	public String getTotal_price()
	{
		return total_price;
	}

	public void setTotal_price(String total_price)
	{
		this.total_price = total_price;
		this.total_priceFormat = SDFormatUtil.formatMoneyChina(total_price);
	}

	public int getC()
	{
		return c;
	}

	public void setC(int c)
	{
		this.c = c;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public List<Uc_orderGoodsModel> getDeal_order_item()
	{
		return deal_order_item;
	}

	public void setDeal_order_item(List<Uc_orderGoodsModel> deal_order_item)
	{
		this.deal_order_item = deal_order_item;
		updateGoodsStatus();
	}

	public void updateGoodsStatus()
	{
		if (!SDCollectionUtil.isEmpty(deal_order_item))
		{
			for (Uc_orderGoodsModel model : deal_order_item)
			{
				model.setOrder_status(order_status);
				model.setPay_status(pay_status);
			}
		}
	}
	
}
	
