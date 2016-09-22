package com.fanwe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Uc_orderModelParcelable implements Parcelable{

	private  int id;
	private String order_sn;
	
	
	private String create_time; // 下单时间
	
	private int c; // 商品总量
	
	private String status; // 未支付 string 订单状态
	
	private int payment_id;
	
	// add
	private String total_priceFormat;
	private String pay_amountFormat;
	
	
	public int getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTotal_priceFormat() {
		return total_priceFormat;
	}
	public void setTotal_priceFormat(String total_priceFormat) {
		this.total_priceFormat = total_priceFormat;
	}
	public String getPay_amountFormat() {
		return pay_amountFormat;
	}
	public void setPay_amountFormat(String pay_amountFormat) {
		this.pay_amountFormat = pay_amountFormat;
	}
	
	@Override
	public int describeContents() {
		
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(order_sn);
		
		dest.writeString(create_time);
		
		dest.writeInt(c);
		dest.writeString(status);
		
		dest.writeString(total_priceFormat);
		dest.writeString(pay_amountFormat);
		
		
		
	}
	 public static final Parcelable.Creator<Uc_orderModelParcelable> CREATOR 
     = new Parcelable.Creator<Uc_orderModelParcelable>()
     {
	 // From Parcelable.Creator
	 @Override
	 public Uc_orderModelParcelable createFromParcel(Parcel in)
	 {
		 Uc_orderModelParcelable brief = new Uc_orderModelParcelable();
	     
	     // 从包裹中读出数据
		 brief.id = in.readInt();
	     brief.order_sn = in.readString();
	     
	     brief.create_time = in.readString();
	     
	     brief.c = in.readInt();
	     brief.status = in.readString();
	    
	     brief.total_priceFormat = in.readString();
	     brief.pay_amountFormat = in.readString();
	     
	     return brief;
	 }
	
	
	
	 
	 @Override
	 public Uc_orderModelParcelable[] newArray(int size)
	 {
	     return new Uc_orderModelParcelable[size];
	 }
	};

}
